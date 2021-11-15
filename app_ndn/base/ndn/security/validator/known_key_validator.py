import abc
from Cryptodome.Hash import SHA256, HMAC
from Cryptodome.PublicKey import ECC, RSA
from Cryptodome.Signature import DSS, pkcs1_15
from ...encoding import FormalName, BinaryStr, NonStrictName, SignaturePtrs, Name, SignatureType
from ...types import Validator
from ...app_support.security_v2 import parse_certificate


def verify_ecdsa(pub_key: ECC.EccKey, sig_ptrs: SignaturePtrs) -> bool:
    verifier = DSS.new(pub_key, 'fips-186-3', 'der')
    h = SHA256.new()
    for content in sig_ptrs.signature_covered_part:
        h.update(content)
    try:
        verifier.verify(h, bytes(sig_ptrs.signature_value_buf))
        return True
    except ValueError:
        return False


def verify_rsa(pub_key: RSA.RsaKey, sig_ptrs: SignaturePtrs) -> bool:
    verifier = pkcs1_15.new(pub_key)
    h = SHA256.new()
    for content in sig_ptrs.signature_covered_part:
        h.update(content)
    try:
        verifier.verify(h, bytes(sig_ptrs.signature_value_buf))
        return True
    except ValueError:
        return False


def verify_hmac(key: BinaryStr, sig_ptrs: SignaturePtrs) -> bool:
    h = HMAC.new(key, digestmod=SHA256)
    for content in sig_ptrs.signature_covered_part:
        h.update(content)
    try:
        h.verify(sig_ptrs.signature_value_buf)
        return True
    except ValueError:
        return False


class KnownChecker(abc.ABC):
    @classmethod
    @abc.abstractmethod
    def _verify(cls, pub_key_bits, sig_ptrs) -> bool:
        pass

    @classmethod
    def from_key(cls, key_name: NonStrictName, pub_key_bits: BinaryStr) -> Validator:
        key_name = Name.normalize(key_name)

        async def validator(_name: FormalName, sig_ptrs: SignaturePtrs) -> bool:
            if not sig_ptrs.signature_info or not sig_ptrs.signature_info.key_locator:
                return False
            if not sig_ptrs.signature_info.key_locator.name:
                return False
            if not Name.is_prefix(key_name, sig_ptrs.signature_info.key_locator.name):
                return False
            return cls._verify(pub_key_bits, sig_ptrs)

        return validator

    @classmethod
    def from_cert(cls, certificate: BinaryStr) -> Validator:
        cert = parse_certificate(certificate)
        key_name = cert.name[:-2]
        key_bits = cert.content
        return cls.from_key(key_name, key_bits)


class EccChecker(KnownChecker):
    @classmethod
    def _verify(cls, pub_key_bits, sig_ptrs) -> bool:
        if sig_ptrs.signature_info.signature_type != SignatureType.SHA256_WITH_ECDSA:
            return False
        pub_key = ECC.import_key(bytes(pub_key_bits))
        return verify_ecdsa(pub_key, sig_ptrs)


class RsaChecker(KnownChecker):
    @classmethod
    def _verify(cls, pub_key_bits, sig_ptrs) -> bool:
        if sig_ptrs.signature_info.signature_type != SignatureType.SHA256_WITH_RSA:
            return False
        pub_key = RSA.import_key(bytes(pub_key_bits))
        return verify_rsa(pub_key, sig_ptrs)


class HmacChecker(KnownChecker):
    @classmethod
    def _verify(cls, pub_key_bits, sig_ptrs) -> bool:
        if sig_ptrs.signature_info.signature_type != SignatureType.HMAC_WITH_SHA256:
            return False
        return verify_hmac(pub_key_bits, sig_ptrs)

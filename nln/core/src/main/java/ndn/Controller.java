package ndn;

import net.named_data.jndn.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnInterestCallback;
import net.named_data.jndn.OnRegisterFailed;
import net.named_data.jndn.OnTimeout;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.WireFormat;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SafeBag;
import net.named_data.jndn.security.SecurityException;
import net.named_data.jndn.security.KeyType;
import net.named_data.jndn.security.identity.IdentityManager;
import net.named_data.jndn.security.identity.MemoryIdentityStorage;
import net.named_data.jndn.security.identity.MemoryPrivateKeyStorage;
import net.named_data.jndn.security.pib.Pib;
import net.named_data.jndn.security.pib.PibImpl;
import net.named_data.jndn.security.tpm.TpmBackEnd;
import net.named_data.jndn.security.v2.CertificateV2;
import net.named_data.jndn.sync.detail.PSyncSegmentPublisher;
import net.named_data.jndn.util.Blob;

public class Controller {
    Face face;
    KeyChain keyChain;
    Name certificateName;

    private Boolean endFlag = false;

    public final static Long TIMEOUT_MS = 60*1000L;

    public Controller() {
        this.face = new Face("localhost");
        init();
    }

    public Controller(String host) {
        this.face = new Face(host);
        init();
    }

    public Controller(Face face) {
        this.face = face;
        init();
    }

    private void init() {
        setupKeychainV2();
        face.setInterestLoopbackEnabled(true);
    }

    private void setupKeyChain() {
        MemoryIdentityStorage identityStorage = new MemoryIdentityStorage();
        MemoryPrivateKeyStorage privateKeyStorage = new MemoryPrivateKeyStorage();
        IdentityManager identityManager = new IdentityManager(identityStorage, privateKeyStorage);
        KeyChain keyChain = new KeyChain(identityManager);
        String nodeId = "default";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            nodeId = addr.getHostName() + addr.getAddress();
        } catch (UnknownHostException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        //keyName: different between nodes.
        Name keyName = new Name("/nln/" + nodeId);
        Name certificateName = keyName.getSubName(0, keyName.size() - 1).append("KEY").append(keyName.get(-1))
                .append("ID-CERT").append("0");
        try {
            identityStorage.addKey(keyName, KeyType.RSA, new Blob(RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER, false));
            privateKeyStorage.setKeyPairForKeyName(keyName, KeyType.RSA, RSA_Key.DEFAULT_RSA_PUBLIC_KEY_DER,
                    RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER);
        } catch (SecurityException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        face.setCommandSigningInfo(keyChain, certificateName);
        this.keyChain = keyChain;
        this.certificateName = certificateName;
        Logger.getGlobal().log(Level.INFO, "Face signed: node HOST->" + nodeId);
    }

    private void setupKeychainV2() {
        try {
            KeyChain keyChain = new KeyChain("pib-memory", "tpm-memory");
            String nodeId = "default";
            try {
                InetAddress addr = InetAddress.getLocalHost();
                nodeId = addr.getHostName() + addr.getAddress();
            } catch (UnknownHostException e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
            Name keyName = new Name("/nln/" + nodeId);
            Name certificateName = keyName.getSubName(0, keyName.size() - 1).append("KEY").append(keyName.get(-1))
                    .append("ID-CERT").append("0");
            keyChain.importSafeBag(new SafeBag(
                    new Name("/nln/KEY/123"),
                    new Blob(RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER, false),
                    new Blob(RSA_Key.DEFAULT_RSA_PUBLIC_KEY_DER, false)
            ));
            face.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());

            this.keyChain = keyChain;
            this.certificateName = keyChain.getDefaultCertificateName();
        } catch (KeyChain.Error e) {
            e.printStackTrace();
        } catch (PibImpl.Error e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateV2.Error e) {
            e.printStackTrace();
        } catch (TpmBackEnd.Error e) {
            e.printStackTrace();
        } catch (Pib.Error e) {
            e.printStackTrace();
        }
    }

    public void runLoop() {
        while (!endFlag) {
            try {
                if (face == null) break;
                face.processEvents();
                Thread.sleep(100);
            } catch (Exception e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    public void endLoop() {
        endFlag = true;
    }

    public static void setDaemonThread(Runnable block) {
        Thread thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }

    public static Interest initTsfInterest(Name name, Boolean isLatest) {
        LocalDateTime time = LocalDateTime.now();
        String ss = isLatest ? "ss" : "";
        String tsf = time.format(DateTimeFormatter.ofPattern("yyyyMMHHmm" + ss));
        Name uri = new Name(name.toString() + "/TSF" + tsf);
        Interest tsfInterest = new Interest(uri);
        return tsfInterest;
    }

    public static String getTsf(Name of) {
        String tsf = Arrays.stream(of.toString().split("/")).filter(i -> i.startsWith("TSF")).collect(Collectors.joining("/"));
        return tsf;
    }

    public static Name getOriginName(Name prefix) {
        String originName = Arrays.stream(prefix.toString().split("/")).filter(i -> !i.startsWith("TSF")).collect(Collectors.joining("/"));
//        Interest originInterest = new Interest(interest);
//        originInterest.setName(new Name(originName));
        return new Name(originName);
    }

    public void responseSegment(Interest interest, Face face, Blob param) {
        Logger.getGlobal().log(Level.INFO, "Segmented response: " + interest.getName());
        PSyncSegmentPublisher pSyncSegmentPublisher = new PSyncSegmentPublisher(face, this.keyChain);
        try {
            pSyncSegmentPublisher.publish(interest.getName(), interest.getName(), param, 1);
        } catch (EncodingException e) {
            e.printStackTrace();
        } catch (TpmBackEnd.Error e) {
            e.printStackTrace();
        } catch (PibImpl.Error e) {
            e.printStackTrace();
        } catch (KeyChain.Error e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void responseParam(Interest interest, Face face, Blob param) {
        Data responseData = new Data();
        responseData.setName(interest.getName());
        responseData.setContent(param);

        try {
            face.putData(responseData);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    public static void responseNack(Interest interest, Face face, NetworkNack networkNack) {
        try {
            face.putNack(interest, networkNack);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    public void register(String name, OnInterestCallback onInterestCallback, OnRegisterFailed onRegisterFailed, OnRegisterSuccess onRegisterSuccess) {
        try {
            face.registerPrefix(new Name(name), (prefix, tsfInterest, face, interestFilterId, filter) -> {
                Logger.getGlobal().log(Level.INFO, "Interest coming: " + tsfInterest.getName() + ", filterBy: " + filter.getPrefix());
//                Interest originInterest = getOriginInterest(interest);
                onInterestCallback.onInterest(getOriginName(tsfInterest.getName()), tsfInterest, face, interestFilterId, filter);
            }, prefix -> {
                Logger.getGlobal().log(Level.INFO, "Register failed: " + prefix);
                onRegisterFailed.onRegisterFailed(prefix);
            }, (prefix, registeredPrefixId) -> {
                Logger.getGlobal().log(Level.INFO, "Register success: " + prefix + ", ID: " + registeredPrefixId);
                onRegisterSuccess.onRegisterSuccess(prefix, registeredPrefixId);
            });
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }

    public void interest(String name, Boolean isPreferredLatest, Boolean isLatest, OnData onData, OnTimeout onTimeout, OnNetworkNack onNetworkNack) {
        Interest.setDefaultCanBePrefix(true);
        Interest interest = isPreferredLatest ? initTsfInterest(new Name(name), isLatest) : new Interest(new Name(name));
        interest.setMustBeFresh(false); // trueにすると何故かDataを受け取れないのTimeStampFieldをNameにつける
        // Consumerの(now + 5) > ルーターのnow の時に転送させる
        interest.setInterestLifetimeMilliseconds(TIMEOUT_MS);
        interest.setApplicationParameters(new Blob(String.valueOf(LocalTime.now().plusNanos(TIMEOUT_MS*100000).toNanoOfDay())));
//            controller.keyChain.sign(interest, controller.certificateName);
        this.interest(interest, onData, onTimeout, onNetworkNack);
    }

    public void interest(Interest interest, OnData onData, OnTimeout onTimeout, OnNetworkNack onNetworkNack) {
        long timeout = Long.parseLong(interest.getApplicationParameters().toString());
        if (timeout < LocalTime.now().toNanoOfDay()) {
            Logger.getGlobal().log(Level.INFO, "Timeout by HOP_LIMIT TIME");
            onTimeout.onTimeout(interest);
            return;
        }
        int[] responseCount = {1};
        try {
            final Boolean[] responseFlag = {false};
//            keyChain.sign(interest, certificateName);
            Logger.getGlobal().log(Level.INFO, "Interest sending: " + interest.getName());
            face.expressInterest(interest, (tsfInterest, data) -> {
                Logger.getGlobal().log(Level.INFO, "Receive data: " + tsfInterest.getName() + ", data: " + data.getContent());
                responseCount[0]--;
                if (!(responseCount[0] > 0)) {
                    responseFlag[0] = true;
                    onData.onData(tsfInterest, data);
                }

            }, tsfInterest -> {
                Logger.getGlobal().log(Level.INFO, "Timeout interest: " + tsfInterest.getName());
                responseFlag[0] = true;
                onTimeout.onTimeout(tsfInterest);
            }, (tsfInterest, networkNack) -> {
                Logger.getGlobal().log(Level.INFO, "Network nack: " + tsfInterest.getName() + ", Reason: " + networkNack.getReason());
                responseFlag[0] = true;
                onNetworkNack.onNetworkNack(tsfInterest, networkNack);
            });
            while (!responseFlag[0]) {
                face.processEvents();
                Thread.sleep(10);
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
//        face.shutdown();
    }
}

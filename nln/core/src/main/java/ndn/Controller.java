package ndn;

import net.named_data.jndn.*;
import net.named_data.jndn.impl.*;
import net.named_data.jndn.lp.*;
import net.named_data.jndn.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.InterestFilter;
import net.named_data.jndn.Name;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnInterestCallback;
import net.named_data.jndn.OnRegisterFailed;
import net.named_data.jndn.OnTimeout;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SafeBag;
import net.named_data.jndn.security.SecurityException;
import net.named_data.jndn.security.KeyType;
import net.named_data.jndn.security.SecurityException;
import net.named_data.jndn.security.identity.IdentityManager;
import net.named_data.jndn.security.identity.MemoryIdentityStorage;
import net.named_data.jndn.security.identity.MemoryPrivateKeyStorage;
import net.named_data.jndn.util.Blob;
import net.named_data.jndn.util.Common;

public class Controller implements OnInterestCallback, OnData, OnTimeout, OnRegisterSuccess, OnRegisterFailed, OnNetworkNack {
    Face face;
    KeyChain keyChain;
    Name certificateName;

    public Controller() {
        this.face = new Face("localhost");
        setupKeyChain();
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
            e.printStackTrace();
        }
        System.out.println(nodeId);
        //keyName: different between nodes.
        Name keyName = new Name("/nln/" + nodeId);
        Name certificateName = keyName.getSubName(0, keyName.size() - 1).append("KEY").append(keyName.get(-1))
                .append("ID-CERT").append("0");
        try {
            identityStorage.addKey(keyName, KeyType.RSA, new Blob(RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER, false));
            privateKeyStorage.setKeyPairForKeyName(keyName, KeyType.RSA, RSA_Key.DEFAULT_RSA_PUBLIC_KEY_DER,
                    RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER);
        } catch (SecurityException e) {
            System.out.println("exception: " + e.getMessage());
        }
        face.setCommandSigningInfo(keyChain, certificateName);
        this.keyChain = keyChain;
        this.certificateName = certificateName;
        System.out.println(certificateName);
//        keyChain = new KeyChain("pib-memory:", "tpm-memory:");
//        keyChain.importSafeBag(new SafeBag(
//                new Name("/example/KEY/0"),
//                new Blob(RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER, false),
//                new Blob(RSA_Key.DEFAULT_RSA_PUBLIC_KEY_DER, false)
//        ));
    }

    public void register() {
        try {
            face.registerPrefix(new Name("/nln"), this, (OnRegisterFailed) this, this);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void runLoop() {
        while (true) {
            try {
                face.processEvents();
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void interest(String name, OnData onData, OnTimeout onTimeout, OnNetworkNack onNetworkNack) {
        Controller controller = new Controller();
        try {
            Interest.setDefaultCanBePrefix(true);
            Name uri = new Name(name);
            Interest interest = new Interest(uri);
            interest.setMustBeFresh(true);
            interest.setInterestLifetimeMilliseconds(5000);
            interest.refreshNonce();
            controller.keyChain.sign(interest, controller.certificateName);
            controller.face.setInterestLoopbackEnabled(true);
            controller.face.expressInterest(uri, onData, onTimeout);
            controller.face.expressInterest(interest, controller, controller, controller);
            while (true) {
                controller.face.processEvents();
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            controller.face.shutdown();
        }
    }

    @Override
    public void onInterest(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
        System.out.println("OnInterest");
        Logger.getGlobal().log(Level.INFO, "Interest coming: " + interest.getName());
        try {
            Data data = new Data(interest.getName());
            String content = "Echo " + interest.getName().toUri();
            data.setContent(new Blob(content));
//            keyChain.sign(data, certificateName);
            face.putData(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onData(Interest interest, Data data) {
        System.out.println("OnData");
        System.out.println(data.getName().toString());
    }

    @Override
    public void onTimeout(Interest interest) {
        System.out.println("Timeout");
    }

    @Override
    public void onNetworkNack(Interest interest, NetworkNack networkNack) {
        System.out.println("OnNetworkNack");
    }

    @Override
    public void onRegisterFailed(Name prefix) {
        System.out.println("OnRegisterFailed");
    }

    @Override
    public void onRegisterSuccess(Name prefix, long registeredPrefixId) {
        System.out.println("OnRegisterSuccess");
    }
}

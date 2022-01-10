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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

public class Controller {
    Face face;
    KeyChain keyChain;
    Name certificateName;

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
        setupKeyChain();
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

    public void runLoop() {
        while (true) {
            try {
                face.processEvents();
                Thread.sleep(100);
            } catch (Exception e) {
                Logger.getGlobal().log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void setDaemonThread(Runnable block) {
        Thread thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }

    public static Interest initTsfInterest(Name name) {
        Date today = Calendar.getInstance().getTime();
        String tsf = new SimpleDateFormat("yyyyMMHHmm").format(today);
        Name uri = new Name(name.toString() + "/TSF" + tsf);
        Interest tsfInterest = new Interest(uri);
        return tsfInterest;
    }

    public static Name getOriginName(Interest interest) {
        String originName = Arrays.stream(interest.getName().toString().split("/")).filter(i -> !i.startsWith("TSF")).collect(Collectors.joining("/"));
//        Interest originInterest = new Interest(interest);
//        originInterest.setName(new Name(originName));
        return new Name(originName);
    }

    public void register(String name, OnInterestCallback onInterestCallback, OnRegisterFailed onRegisterFailed, OnRegisterSuccess onRegisterSuccess) {
        try {
            face.registerPrefix(new Name(name), (prefix, tsfInterest, face, interestFilterId, filter) -> {
                Logger.getGlobal().log(Level.INFO, "Interest coming: " + tsfInterest.getName() + ", filterBy: " + filter.getPrefix());
//                Interest originInterest = getOriginInterest(interest);
                onInterestCallback.onInterest(getOriginName(tsfInterest), tsfInterest, face, interestFilterId, filter);
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

    public void interest(String name, OnData onData, OnTimeout onTimeout, OnNetworkNack onNetworkNack) {
        try {
            Interest.setDefaultCanBePrefix(true);
            Interest interest = initTsfInterest(new Name(name));
            interest.setMustBeFresh(false); // trueにすると何故かDataを受け取れないのTimeStampFieldをNameにつける
            interest.setInterestLifetimeMilliseconds(5000.0);
//            controller.keyChain.sign(interest, controller.certificateName);
            Logger.getGlobal().log(Level.INFO, "Interest sending: " + interest.getName());

            face.expressInterest(interest, (tsfInterest, data) -> {
                Logger.getGlobal().log(Level.INFO, "Receive data: " + tsfInterest.getName() + ", data: " + data.getContent());
                onData.onData(tsfInterest, data);
            }, tsfInterest -> {
                Logger.getGlobal().log(Level.INFO, "Timeout interest: " + tsfInterest.getName());
                onTimeout.onTimeout(tsfInterest);
            }, (tsfInterest, networkNack) -> {
                Logger.getGlobal().log(Level.INFO, "Network nack: " + tsfInterest.getName() + ", Reason: " + networkNack.getReason());
                onNetworkNack.onNetworkNack(tsfInterest, networkNack);
            });
            while (true) {
                face.processEvents();
                Thread.sleep(10);
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        } finally {
            face.shutdown();
        }
    }
}

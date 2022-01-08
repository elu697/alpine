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
import net.named_data.jndn.util.Blob;
import net.named_data.jndn.util.Common;
import ndn.RSA_Key;

public class Controller implements OnInterestCallback, OnData, OnTimeout, OnRegisterSuccess, OnRegisterFailed, OnNetworkNack {
    Face face;
    int activeInterestCount = 0;
    KeyChain keyChain;

    public Controller() {
        this.face = new Face("localhost");
        try {
            keyChain = new KeyChain("pib-memory:", "tpm-memory:");
            keyChain.importSafeBag(new SafeBag(
                    new Name("/example/KEY/0"),
                    new Blob(RSA_Key.DEFAULT_RSA_PRIVATE_KEY_DER, false),
                    new Blob(RSA_Key.DEFAULT_RSA_PUBLIC_KEY_DER, false)
            ));
            this.face.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void register() {
        try {
            this.face.registerPrefix(new Name("/example"), this, (OnRegisterFailed) this, this);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void runLoop() {
        while (true) {
            try {
                this.face.processEvents();
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
            controller.face.expressInterest(uri, controller, controller, controller);
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
        try {
            Data data = new Data();
            data.setName(new Name(interest.getName()));
            data.setContent(new Blob(new int[9]));
            keyChain.sign(data, keyChain.getDefaultCertificateName());
            face.putData(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onData(Interest interest, Data data) {
        System.out.println("OnData");
        System.out.println(data.getName().toString());
        this.activeInterestCount--;
    }

    @Override
    public void onTimeout(Interest interest) {
        System.out.println("Timeout");
        this.activeInterestCount--;
    }

    @Override
    public void onNetworkNack(Interest interest, NetworkNack networkNack) {
        System.out.println("OnNetworkNack");
        this.activeInterestCount--;
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

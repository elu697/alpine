package ndn;

import net.named_data.jndn.*;
import net.named_data.jndn.impl.*;
import net.named_data.jndn.lp.*;
import net.named_data.jndn.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class Controller implements OnInterestCallback, OnData, OnTimeout, OnRegisterSuccess, OnRegisterFailed, OnNetworkNack {
    Face face;
    int activeInterestCount = 0;

    public Controller() {
        this.face = new Face("133.28.131.222", 6970);
    }

    public static void interest(String name, OnData onData) {
        Controller controller = new Controller();
        try {
            System.out.println(controller.face);
            Interest.setDefaultCanBePrefix(true);
            Name uri = new Name(name);
//            Interest interest = new Interest(uri);
//            interest.setInterestLifetimeMilliseconds(10000);
//            controller.face.makeCommandInterest(interest);
            controller.face.expressInterest(uri, onData, new OnTimeout() {
                @Override
                public void onTimeout(Interest interest) {
                    System.out.println("Timeout");
                    controller.activeInterestCount--;
                }
            });
            controller.activeInterestCount++;
            while (controller.activeInterestCount > 0) {
                controller.face.processEvents();
                Thread.sleep(10);
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
    }

    @Override
    public void onData(Interest interest, Data data) {
        System.out.println("OnData");
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

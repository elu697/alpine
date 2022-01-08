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
import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import ndn.Controller;

public class main {
    public static void main(String[] args) {
        System.out.println(args);
        Controller.interest("/test", (interest, data) -> {
            System.out.println("DateComming");
        }, interest -> {
            System.out.println("InterestTimeout");
        }, (interest, networkNack) -> {
            System.out.println("NetworkError");
        });
//        if (Objects.equals(args[0], "C")) {
//            Controller.interest("/test", (interest, data) -> {
//                System.out.println("DateComming");
//            }, interest -> {
//                System.out.println("InterestTimeout");
//            }, (interest, networkNack) -> {
//                System.out.println("NetworkError");
//            });
//        } else {
//            Controller controller = new Controller();
//            controller.register();
//            controller.runLoop();
////            setDaemonThread(controller::runLoop);
//        }
    }

    public static void setDaemonThread(Runnable block) {
        Thread thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }
}

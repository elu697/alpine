import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.CoderMalfunctionError;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import ndn.Controller;
import net.named_data.jndn.*;
import net.named_data.jndn.util.Blob;

public class main {
    public static void main(String[] args) {
        String name = "/nln/";
        if (Objects.equals(args[0], "C")) {
            Controller.interest(name, (interest, data) -> {
                System.out.println("Date Coming");
                System.out.println(name);
                System.out.println(interest.getName());
                System.out.println(data.getContent());
            }, interest -> {
                System.out.println("Interest Timeout");
            }, (interest, networkNack) -> {
                System.out.println("NetworkError");
            });
        } else {
            Controller controller = new Controller();
            controller.register(name, (prefix, interest, face, interestFilterId, filter) -> {
                System.out.println("Interest Coming");
                System.out.println(prefix);
                Data data = new Data(interest.getName());
                data.setContent(new Blob("Echo" + interest.getName().toString()));
                try {
                    face.putData(data);
                } catch (Exception e) {

                }
            }, prefix -> {
                System.out.println("Register failed");
                System.out.println(prefix);
            }, (prefix, registeredPrefixId) -> {
                System.out.println("Register success");
                System.out.println(prefix);
            });
            controller.runLoop();
        }
    }

    public static void setDaemonThread(Runnable block) {
        Thread thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }
}

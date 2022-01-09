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
import ndn.Controller;

public class main {
    public static void main(String[] args) {

        if (Objects.equals(args[0], "C")) {
            Controller.interest("/examples", (interest, data) -> {
                System.out.println("DateComming");
                System.out.println(data.getContent());
            }, interest -> {
                System.out.println("InterestTimeout");
            }, (interest, networkNack) -> {
                System.out.println("NetworkError");
            });
        } else {
            Controller controller = new Controller();
            controller.register();
            controller.runLoop();
        }
    }

    public static void setDaemonThread(Runnable block) {
        Thread thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }
}

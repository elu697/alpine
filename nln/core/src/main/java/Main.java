import common.MIB;
import controller.ForwardController;
import controller.InterestController;
import http.HttpClient;
import http.HttpServer;
import net.named_data.jndn.Face;
import net.named_data.jndn.Name;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Arg:\n" +
                    "consumer {uri} - nln\n" +
                    "router1 {uri} {forward_ip} {forward_ip2} ... - nln\n" +
                    "router2 {uri} {data_name} - nln\n" +
                    "client {url} - http\n" +
                    "server {context} {port}- http\n");
            return;
        }

        String mode = args[0];
        printIP();
        switch (mode) {
            case "consumer":
                Consumer(args[1]);
                break;
            case "router1":
                Router1(args[1], args);
                break;
            case "router2":
                Router2(args[1], args[2]);
                break;
            case "client":
                Client(args[1]);
                break;
            case "server":
                Server(args[1], Integer.parseInt(args[2]));
                break;
        }
    }

    public static void printIP() {
        try {
            Logger.getGlobal().log(Level.INFO, "LOCALHOST IP_ADDRESS: " + InetAddress.getLocalHost());
            Logger.getGlobal().log(Level.INFO, "LOOPBACK IP_ADDRESS: " + InetAddress.getLoopbackAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void Consumer(String uri) {
        InterestController interestController = new InterestController();
        interestController.request(uri, responseData -> {
            System.out.println(responseData.getPojo().getName());
            for (int i = 0; i < responseData.getPojo().getLearningInfo().size(); i++) {
                System.out.println(responseData.getPojo().getLearningInfo().get(i).getUid());
                System.out.println(responseData.getPojo().getLearningInfo().get(i).getProgress());
                System.out.println(responseData.getPojo().getLearningInfo().get(i).getBase64Data());
            }
            for (int i = 0; i < responseData.getPojo().getDatasetInfo().size(); i++) {
                System.out.println(responseData.getPojo().getDatasetInfo().get(i).getUid());
                System.out.println(responseData.getPojo().getDatasetInfo().get(i).getBase64Data());
            }
        });
    }

    private static void Router1(String uri, String[] forwardIps) {
        ForwardController forwardController = new ForwardController();
        for (int i = 2; i < forwardIps.length; i++) {
            MIB.shard.set(new Name(uri), new Face(forwardIps[i]));
        }

        forwardController.listen(uri);
        forwardController.loop();
    }

    private static void Router2(String uri, String modelName) {
        ForwardController forwardController = new ForwardController();
        MIB.shard.set(new Name(uri), new Name(modelName));
        forwardController.listen(uri);
        forwardController.loop();
    }

    private static void Client(String url) {
        HttpClient httpClient = new HttpClient();
        httpClient.request(url, responseData -> {
            System.out.println(responseData.getPojo().getName());
            for (int i = 0; i < responseData.getPojo().getLearningInfo().size(); i++) {
                System.out.println(responseData.getPojo().getLearningInfo().get(i).getUid());
                System.out.println(responseData.getPojo().getLearningInfo().get(i).getProgress());
                System.out.println(responseData.getPojo().getLearningInfo().get(i).getBase64Data());
            }
            for (int i = 0; i < responseData.getPojo().getDatasetInfo().size(); i++) {
                System.out.println(responseData.getPojo().getDatasetInfo().get(i).getUid());
                System.out.println(responseData.getPojo().getDatasetInfo().get(i).getBase64Data());
            }
        });
    }

    private static void Server(String context, int port) {
        HttpServer httpServer = new HttpServer();
        httpServer.listen(context, port);
    }
}

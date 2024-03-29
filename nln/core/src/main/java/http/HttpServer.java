package http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.LearningController;
import datastore.Dataset;
import datastore.Model;
import model.LearningInfo;
import model.ResponseData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HttpServer {
    private ArrayList<String> forwardIps = new ArrayList<>();

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.addForwardIp("111.11.12");
        httpServer.addForwardIp("111.11.13");

        String prefix = "";
        if (args.length == 0) {
            prefix = "/model/";
        } else {
            prefix = args[0];
        }
        try {
            httpServer.listen(prefix, 9000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void addForwardIp(String ip) {
        forwardIps.add(ip);
    }

    public void listen(String prefix, int port) {
        try {
            com.sun.net.httpserver.HttpServer httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext(prefix, new RequestHandler(forwardIps));
            System.out.println("Listen: " + prefix);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // HTTP リクエストを処理するために呼び出されるハンドラ
    private class RequestHandler implements HttpHandler {
        private ArrayList<String> forwardIps = new ArrayList<>();

        // HTTP リクエストを処理する
        public RequestHandler(ArrayList<String> forwardIps) {
            this.forwardIps = forwardIps;
        }

        public void handle(HttpExchange t) throws IOException {
            System.out.println("**************************************************");
            System.out.println("Request: " + t.getRequestURI());
            String resBody = "";

            if (t.getRequestURI().getPath().endsWith("server")) {
                String ips = "";
                ips = forwardIps.toString();
                resBody = "{'server_ip':"+ ips + "}";
            } else {
                try {
                    String prefix = t.getRequestURI().getPath();
                    LearningInfo learningInfo = learning(prefix);
                    ResponseData.POJO pojo = new ResponseData.POJO();
                    pojo.setName(prefix);
                    pojo.addLearningInfo(learningInfo);
                    ResponseData responseDataObject = new ResponseData(pojo);
                    resBody = responseDataObject.toJsonObj().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // レスポンスボディを構築
            // (ここでは Java 14 から正式導入された Switch Expressions と
            //  Java 14 でプレビュー機能として使えるヒアドキュメント的な Text Blocks 機能を使ってみる)


            // Content-Length 以外のレスポンスヘッダを設定
            Headers resHeaders = t.getResponseHeaders();
            resHeaders.set("Content-Type", "application/json");
            resHeaders.set("Last-Modified",
                    ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME));
            resHeaders.set("Server",
                    "MyServer (" +
                            System.getProperty("java.vm.name") + " " +
                            System.getProperty("java.vm.vendor") + " " +
                            System.getProperty("java.vm.version") + ")");

            // レスポンスヘッダを送信
            int statusCode = 200;
            long contentLength = resBody.getBytes(StandardCharsets.UTF_8).length;
            t.sendResponseHeaders(statusCode, contentLength);

            // レスポンスボディを送信
            OutputStream os = t.getResponseBody();
            os.write(resBody.getBytes());
            os.close();
            System.out.println("Response: " + resBody);
            System.out.println("**************************************************");
        }
    }

    private static LearningInfo learning(String modelName) {
        Model model = Model.initModel();
        LearningInfo learningInfo = LearningController.shard.simpleLearning(modelName, model, Dataset.initMnist());
        return learningInfo;
    }
}

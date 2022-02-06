package http;

import controller.LearningController;
import datastore.Dataset;
import datastore.Model;
import model.LearningInfo;
import model.ResponseData;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpClient {
    private java.net.http.HttpClient httpClient;

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        int port = 9000;
        String url = "http://172.20.0.4"+ ":" + port + "/model/A";
        httpClient.request(url, responseData -> {
            System.out.println("Receive response");
            System.out.println(responseData.toJsonObj());
        });
    }

    public void request(String url, Consumer<ResponseData> responseDataConsumer) {
        httpClient = java.net.http.HttpClient.newBuilder()
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        System.out.println("Request: " + url);

        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    ResponseData responseData = new ResponseData(res.body());
                    System.out.println("Receive response");
                    System.out.println(responseData.toJsonObj());
                    responseDataConsumer.accept(responseData);
                });
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

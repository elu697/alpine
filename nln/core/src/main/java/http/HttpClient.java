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

public class HttpClient {
    private java.net.http.HttpClient httpClient;

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        int port = 9000;
        String url = "http://localhost"+ ":" + port + "/model/A";
        httpClient.request(url, responseData -> {
            System.out.println(responseData.toJsonObj().toString());
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
                    responseDataConsumer.accept(responseData);
                });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

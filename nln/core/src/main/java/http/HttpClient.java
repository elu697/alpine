package http;

import model.ResponseData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class HttpClient {
    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        int port = 9000;
        String url = "http://172.20.0.4"+ ":" + port + "/model/A";
        httpClient.request(url, responseData -> {
            System.out.println("Receive response");
            System.out.println(responseData.toJsonObj());
        });
    }

    public static void simpleRequest(String url, Consumer<String> responseDataConsumer) {
        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder()
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        System.out.println("Request: " + url);

        try {
            var res = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            responseDataConsumer.accept(res.body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void request(String url, Consumer<ResponseData> responseDataConsumer) {
        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder()
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        System.out.println("Request: " + url);

        try {
            var res = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ResponseData responseData = new ResponseData(res.body());
            System.out.println("Receive response");
            System.out.println(responseData.toJsonObj());
            responseDataConsumer.accept(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

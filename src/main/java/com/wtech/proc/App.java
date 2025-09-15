package com.wtech.proc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class App {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private enum HTTPMethods {
        GET, POST
    }

    public static void main(String[] args) throws IOException {
        System.out.println("####### Calling public API TJMG - Proc Num, results on the console...");

        String processNumber = "13668284120218130024";

        callWebservice(HTTPMethods.GET, processNumber);

        callWebservice(HTTPMethods.POST, processNumber);
    }

    private static void callWebservice(HTTPMethods method, String processNumber) throws IOException {

        System.out.println("\nCalling webservice - method: " + method);
        OkHttpClient client = new OkHttpClient();

        String url = "https://api-publica.datajud.cnj.jus.br/api_publica_tjmg/_search";
        System.out.println("URL: " + url);

        Request request = createRequest(url, method, processNumber);

        Response response = client.newCall(request).execute();

        System.out.println(response);

        printJsonResponseBody(response);
    }

    @NotNull
    private static Request createRequest(String url, HTTPMethods method, String processNumber) {

        // JSON data
        String json = "{\"query\": {\"match\": {\"numeroProcesso\": \"" + processNumber + "\"}}}";

        RequestBody body = RequestBody.create(json, JSON);

        String apiKey = "APIKey cDZHYzlZa0JadVREZDJCendQbXY6SkJlTzNjLV9TRENyQk1RdnFKZGRQdw==";

        if (method == HTTPMethods.POST) {
            return new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", apiKey)
                    .build();
        } else {
            return new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", apiKey)
                    .build();
        }
    }

    private static void printJsonResponseBody(Response response) throws IOException {
        // Format the response body as pretty JSON
        if (response.body() != null) {
            String responseBody = response.body().string();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(JsonParser.parseString(responseBody));

            System.out.println("\nPRINTING JSON RESPONSE...");
            System.out.println(prettyJson);
        } else {
            System.out.println("####### No response body #######");
        }
    }
}

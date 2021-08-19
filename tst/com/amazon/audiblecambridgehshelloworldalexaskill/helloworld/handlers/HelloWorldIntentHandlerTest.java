package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HelloWorldIntentHandlerTest {


    private HelloWorldIntentHandler handler;

    /**
     * This is called before each test.  Do shared setup work here.
     * @throws Exception
     */
    @org.junit.Before // this annotation makes this method run before each test
    public void setUp() throws Exception {
        handler = new HelloWorldIntentHandler();
    }

    @org.junit.Test // same, this is a test
    public void handle() throws Exception {
        // from https://www.mbta.com/developers/v3-api
        String apiEndpoint = "https://api-v3.mbta.com/routes";
        String query = "";

        // lets get some data from it
        HttpURLConnection urlc = (HttpURLConnection) new URL(apiEndpoint).openConnection();

        urlc.setRequestMethod("GET");
        urlc.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (urlc.getInputStream())));

        StringBuilder sb = new StringBuilder();
        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        String result = sb.toString();
        urlc.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> stuff = mapper.readValue(result.getBytes(), Map.class);
        System.out.println(stuff);
    }
}
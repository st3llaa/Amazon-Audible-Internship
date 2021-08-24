package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import model.Book;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class OpenLibraryRepository {

    public Book searchBook(String searchKey) {
        // calls buildHttpGetRequest method with input search key to build http request.
        HttpURLConnection httpURLConnection = buildHttpGetRequest(searchKey);;
        // calls executeHttpRequest method with httpURLConnection to execute the request.
        InputStream responseStream = executeHttpRequest(httpURLConnection);
        // calls parseHttpResponse method with responseStream returned from previous call to get data stored in YourObjectModel
        return parseHttpResponse(responseStream);
    }

    // This method takes input searchKey, builds and returns a HTTP request
    private HttpURLConnection buildHttpGetRequest(String searchKey) {
        try {
            String encodedSearchKey = URLEncoder.encode(searchKey,"UTF-8");
            String urlString = "http://openlibrary.org/search.json?title="+encodedSearchKey+"&fields=title,author_name,key&limit=1"; // TODO: build request url with query, see https://openlibrary.org/dev/docs/api/search for url examples

            URL url = new URL(urlString);
            //log.info("url query: " + url.getQuery()); // TODO: change to your logging method. Used for debugging purpose.

            //gets a connection to the remote object referred to by the url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (IOException e) {
            //log.error("Error in buildHttpGetRequest: ", e); // TODO: change to your logging method
            throw new RuntimeException(e);
        }
    }

    // This method executes HTTP request (conn), and returns HTTP response
    private InputStream executeHttpRequest(HttpURLConnection conn) {
        try {
            // If the response code doesn't indicate success, throw an exception
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code :  " + conn.getResponseCode());
            }
            return conn.getInputStream();
        } catch (IOException e) {
            //log.error("Error in executeHttpRequest: ", e); // TODO: change to your logging method
            throw new RuntimeException(e);
        }
    }

    // This method parse the HTTP response (inputStream), and returns a objectModel which carries data
    // You need to add json library using Maven in order to use JSONObject and XML.toJSONObject (remove "//" when pasting to pom.xml)
    // <dependency>
    //      <groupId>org.json</groupId>
    //      <artifactId>json</artifactId>
    //      <version>20180813</version>
    //  </dependency>

    private Book parseHttpResponse(final InputStream inputStream) {
        try {
            // convert input stream object to JSON object
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String responseStr = bufferedReader.lines().collect(Collectors.joining("\n"));
            JSONObject responseJson = new JSONObject(responseStr);
            bufferedReader.close();

            // Or, use IOUtils to convert input stream object to string.
            // You need to add Apache commons library in order to use IOUtils, or you can use other ways to convert InputStream object to String
            // <dependency>
            //      <groupId>commons-io</groupId>
            //      <artifactId>commons-io</artifactId>
            //      <version>2.5</version>
            // </dependency>
            // JSONObject responseJson = new JSONObject(IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8.toString()));


            // parses responseJson and extract data that you need
            //String name = responseJson.getString("searchKey");
            JSONObject docObject = responseJson.getJSONArray("docs").optJSONObject(0);
            String authorName = docObject.getJSONArray("author_name").optString(0);
            String bookTitle = docObject.getString("title");
            System.out.println(bookTitle + " " + authorName);
            // builds an object of YourObjectModel which carries data and return it to client
            // return the result
            return new Book(bookTitle, authorName);
        } catch (Exception e) {
            //log.error("Error in readHttpResponse: ", e); // TODO: change to your logging method
        }
        return null;
    }
    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId().toString(),
                new Date(),
                message);
    }
}
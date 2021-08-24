package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import database.AlexaSessionDynamoDBHandler;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
public class SearchByAuthorHandler implements RequestHandler {
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("SearchByAuthorIntent"));
    }
    public Optional<Response> handle(HandlerInput input) {
        log(input, "Starting Search by Author request");
        logSlots(input);

        Map<String, Slot> slots = getSlots(input);
        String speechText = "";

        if(slots.containsKey("AuthorNameSlot") && null != slots.get("AuthorNameSlot").getValue()) {
            String authorName = slots.get("AuthorNameSlot").getValue();
            OpenLibraryRepoAuthor search = new OpenLibraryRepoAuthor();
            if(search.searchBook(authorName) != null){
                String title = search.searchBook(authorName).getTitle();
                String speechTextWithBook = "<speak> I've found %s by %s </speak>";
                speechText = String.format(speechTextWithBook, title, slots.get("AuthorNameSlot").getValue());
            }
        }
        else {
            speechText = "Author not found";
        }

        log(input, "Speech text response is " + speechText);

        // response object with a card (shown on devices with a screen) and speech (what alexa says)
        return input.getResponseBuilder()
                .withSpeech(speechText) // alexa says this
                .withSimpleCard("HelloWorld", speechText) // alexa will show this on a screen
                .build();
    }
    /**
     * Get the slots passed into the request
     * @param input The input object
     * @return Map of slots
     */
    Map<String, Slot> getSlots(HandlerInput input) {
        // this chunk of code gets the slots
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        return Collections.unmodifiableMap(intent.getSlots());
    }

    void logSlots(HandlerInput input) {
        Map<String, Slot> slots = getSlots(input);
        // log slot values including request id and time for debugging
        for(String key : slots.keySet()) {
            log(input, String.format("Slot value key=%s, value = %s", key, slots.get(key).toString()));
        }
    }

    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId().toString(),
                new Date(),
                message);
    }
}

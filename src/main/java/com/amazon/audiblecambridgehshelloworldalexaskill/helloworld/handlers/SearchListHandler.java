package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import database.AlexaSessionDynamoDBHandler;
import model.Book;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class SearchListHandler implements RequestHandler {
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("SearchListIntent"));
    }
    public Optional<Response> handle(HandlerInput input) {
        log(input, "Starting Search List request");
        logSlots(input);

        Map<String, Slot> slots = getSlots(input);
        String speechText = "";

        if(slots.containsKey("BookNameSlot") && null != slots.get("BookNameSlot").getValue() && slots.containsKey("ListNameSlot") && null != slots.get("ListNameSlot").getValue()) {
            String title = slots.get("BookNameSlot").getValue();
            String list = slots.get("ListNameSlot").getValue();
            Book book = new Book(title);
            if(AlexaSessionDynamoDBHandler.searchList(input, title, list)){
                String speechTextWithBook = "<speak> I've found %s by %s in %s </speak>";
                speechText = String.format(speechTextWithBook, slots.get("BookNameSlot").getValue(), book.getAuthor(), slots.get("ListNameSlot").getValue());
            }
            else {
                String speechTextWithBook = "<speak> %s not found in %s </speak>";
                speechText = String.format(speechTextWithBook, slots.get("BookNameSlot").getValue(), slots.get("ListNameSlot").getValue());
            }
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

    /**
     * Log slots for easier debugging
     * @param input Input passed to handle
     */
    void logSlots(HandlerInput input) {
        Map<String, Slot> slots = getSlots(input);
        // log slot values including request id and time for debugging
        for(String key : slots.keySet()) {
            log(input, String.format("Slot value key=%s, value = %s", key, slots.get(key).toString()));
        }
    }

    /**
     * Logs debug messages in an easier to search way
     * You can also use system.out, but it'll be harder to work with
     */
    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId().toString(),
                new Date(),
                message);
    }
}

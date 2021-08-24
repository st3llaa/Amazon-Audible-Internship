package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import database.AlexaSessionDynamoDBHandler;
import model.ReadingList;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class RemoveBookHandler implements RequestHandler {

    /**
     * Determine if this handler can handle the intent (but doesn't actually handle it)
     *
     * This is called by the ASK framework.
     *
     * @param input
     * @return
     */
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("RemoveBookIntent"));
    }

    /**
     * Actually handle the event here.
     *
     * This is called by the ASK framework.
     *
     * @param input
     * @return
     */
    @Override
    public Optional<Response> handle(HandlerInput input) {
        log(input, "Starting remove book request");
        logSlots(input);

        Map<String, Slot> slots = getSlots(input);
        String speechText = "Book not found";

        if(slots.containsKey("BookNameSlot") && slots.containsKey("ListNameSlot") && null != slots.get("BookNameSlot").getValue() && null != slots.get("ListNameSlot")) {
            String bookTitle = slots.get("BookNameSlot").getValue();
            String listName = slots.get("ListNameSlot").getValue();

            if(AlexaSessionDynamoDBHandler.removeBook(input, listName, bookTitle)){
                String speechTextWithBook = "<speak> I've removed %s from your %s list </speak>";
                speechText = String.format(speechTextWithBook, bookTitle, listName);
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

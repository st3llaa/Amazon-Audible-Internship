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
import model.Book;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class ReadListHandler implements RequestHandler {

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
        return input.matches(intentName("ReadListIntent"));
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
        log(input, "Starting read list request");
        //logSlots(input);
        Map<String, Slot> slots = getSlots(input);
        String listName = slots.get("ListNameSlot").getValue();

        StringBuilder readingList = AlexaSessionDynamoDBHandler.getAllSessionAttributes(input, listName);
        //ReadingList readingListAttributes = AlexaSessionDynamoDBHandler.getAllReadingLists(input, listName);
        String speechTextWithBook = "<speak> Your reading list has %s </speak>";
        String speechText = String.format(speechTextWithBook, readingList);

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
        System.out.println(intentRequest);
        System.out.println(intent);
        return Collections.unmodifiableMap(intent.getSlots());
    }

    /**
     * Log slots for easier debugging
     * @param input Input passed to handle
     */
//    void logSlots(HandlerInput input) {
//        Map<String, Slot> slots = getSlots(input);
//        // log slot values including request id and time for debugging
//        for(String key : slots.keySet()) {
//            log(input, String.format("Slot value key=%s, value = %s", key, slots.get(key).toString()));
//        }
//    }

    /**
     * Logs debug messages in an easier to search way
     * You can also use system.out, but it'll be harder to work with
     */
    void log(HandlerInput input, String message) {
        System.out.printf("[%s] [%s] : %s]\n",
                input.getRequestEnvelope().getRequest().getRequestId(),
                new Date(),
                message);
    }
}

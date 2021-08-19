package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FindBookHandler implements RequestHandler {

        private final String speechTextWithBook = "<speak> I found a book called %s by %s </speak>";
        private final String speechTextNoBookName = "Book not found";

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
            return input.matches(intentName("FindBookIntent"));
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
            log(input, "Starting find book request");
            logSlots(input);

            Map<String, Slot> slots = getSlots(input);
            Map<String, String> bookAuthorsMap = new HashMap<>();
            bookAuthorsMap.put("Harry Potter", "J.K.Rowling");
            bookAuthorsMap.put("the bluest eye", "Toni Morrison");
            bookAuthorsMap.put("fences", "August Wilson");
            bookAuthorsMap.put("perfume", "Patrick Süskind");
            String speechText = "";

            if(slots.containsKey("BookNameSlot") && null != slots.get("BookNameSlot").getValue()) {
                String bookTitle = slots.get("BookNameSlot").getValue();
                OpenLibraryRepository test = new OpenLibraryRepository();
                if(test.searchBook(bookTitle) != null){
                    String author = test.searchBook(bookTitle).getAuthor();
                    speechText = String.format(speechTextWithBook, slots.get("BookNameSlot").getValue(), author);
                }
//                if(bookAuthorsMap.containsKey(bookTitle)){
//                    String author = bookAuthorsMap.get(bookTitle);
//                    speechText = String.format(speechTextWithBook, slots.get("BookNameSlot").getValue(), author);
//                }
            }
            else {
                speechText = speechTextNoBookName;
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

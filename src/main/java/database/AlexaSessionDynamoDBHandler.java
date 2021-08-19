package database;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers.Book;

import java.util.*;

public class AlexaSessionDynamoDBHandler {

    public static void saveSessionAttributes(HandlerInput input, String key, String value) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());

//            ArrayList<String> list1books = new ArrayList<>();
//            list1books.add("Harry Potter, J.K. Rowling");
//            persistentAttributes.put("List1", list1books);

            persistentAttributes.put(key, value);
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static String getSessionAttributes(HandlerInput input, String key){
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            return persistentAttributes.get(key).toString();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static StringBuilder getAllSessionAttributes(HandlerInput input, String listName){
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            StringBuilder books = new StringBuilder();
            input.getAttributesManager().savePersistentAttributes();
            if (!persistentAttributes.isEmpty()){
                System.out.println(persistentAttributes.keySet().toString());
                System.out.println(listName);
                ArrayList<String> keys = (ArrayList<String>)(persistentAttributes.get(listName));
                boolean first = true;
                for(String key: keys){ //TODO: make the code more efficient, see line 46
                    books.append(first ? "": ", ").append(key);
                    first = false;
                }
            }
            return books;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static void newList(HandlerInput input, String listName, String bookTitle) {
        //TODO: fix default case
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ArrayList<String> newList = new ArrayList<>();
            Book currentBook = new Book(bookTitle);
            newList.add(currentBook.toString());
            persistentAttributes.put(listName, newList);
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static boolean search(HandlerInput input, String listName){
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            if(persistentAttributes.containsKey(listName)){
                String key = persistentAttributes.get(listName).toString();
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
        return false;
    }
    public static void append(HandlerInput input, String listName, String bookTitle){
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ArrayList<String> listDynamo = (ArrayList<String>) persistentAttributes.get(listName);
            Book currentBook = new Book(bookTitle);
            listDynamo.add(currentBook.toString());
            persistentAttributes.put(listName, listDynamo);
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static void clear(HandlerInput input, String listName) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ArrayList<String> listDynamo = (ArrayList<String>) persistentAttributes.get(listName);
            //TODO: clear the lists
            persistentAttributes.clear();
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static int numLists(HandlerInput input) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            return persistentAttributes.keySet().size();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static StringBuilder listNames(HandlerInput input) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            Set<String> results = persistentAttributes.keySet();
            StringBuilder statement = new StringBuilder();
            boolean first = true;
            for(Object current: results){
                if (first) {
                    statement.append(current.toString());
                } else {
                    statement.append(", ").append(current.toString());
                }
                first = false;
            }
            return statement;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static void removeBook(HandlerInput input, String listName, String bookName) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ArrayList<String> listDynamo = (ArrayList<String>) persistentAttributes.get(listName);
            //TODO: search all list for the book and store listName and in the listDynamo array, then remove the book from the listDynamo a
            persistentAttributes.put(listName, listDynamo);
            persistentAttributes.clear();
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
}
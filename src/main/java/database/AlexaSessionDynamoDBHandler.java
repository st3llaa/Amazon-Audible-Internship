package database;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers.OpenLibraryRepoAuthor;
import model.Book;
import model.ReadingList;

import java.lang.reflect.Array;
import java.util.*;

public class AlexaSessionDynamoDBHandler {

    public static void saveSessionAttributes(HandlerInput input, String key, String value) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            persistentAttributes.put(key, value);
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static void saveReadingList(HandlerInput input, String listName, ReadingList list) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            List<Map<String, String>> books = new ArrayList<>();
            if(!AlexaSessionDynamoDBHandler.search(input, listName)){
              persistentAttributes.put(listName, list.getListName());
            }
            else{
                books = (List<Map<String, String>>) persistentAttributes.get(listName);
            }
            for(Book book : list.getBooks()){
                Map<String, String> b = new HashMap<>();
                b.put("title", book.getTitle());
                b.put("author", book.getAuthor());
                books.add(b);
            }
            persistentAttributes.put(listName, books);
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
    public static StringBuilder getAllSessionAttributes (HandlerInput input, String listName){
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            input.getAttributesManager().savePersistentAttributes();
            StringBuilder allAttributes = new StringBuilder();
            OpenLibraryRepoAuthor libraryAuthor = new OpenLibraryRepoAuthor();
            if (!persistentAttributes.isEmpty()){
                List<Map<String, String>> keys = (List<Map<String, String>>) persistentAttributes.get(listName);
                System.out.println(keys);
                boolean first = true;
                for (Map<String, String> key : keys) {
                    for (Object o : key.entrySet()) {
                        Map.Entry pair = (Map.Entry) o;
                        if(pair.getKey().toString().equals("title")){
                            String title = (String) pair.getValue();
                            Book current = new Book(title);
                            if (first) {
                                allAttributes.append(current.toString()).append(", ");
                            } else {
                                allAttributes.append(", ").append(current.toString());
                            }
                            first = false;
                        }
                    }
                }
            }
            return allAttributes;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
    public static ReadingList getAllReadingLists(HandlerInput input, String listName){
        try {
            //TODO: fix method
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ReadingList list = new ReadingList(listName);
            //input.getAttributesManager().savePersistentAttributes();
            if (!persistentAttributes.isEmpty()){
                ArrayList<String> keys = (ArrayList<String>)(persistentAttributes.get(listName));
                Book currentBook;
                for(String key: keys){
                    currentBook = new Book(key);
                    list.add(currentBook);
                }
            }
            return list;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
//    public static void newList(HandlerInput input, String listName, String bookTitle) {
//        try {
//            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
//            ArrayList<String> newList = new ArrayList<>();
//            Book currentBook = new Book(bookTitle);
//            newList.add(currentBook.toString());
//            persistentAttributes.put(listName, newList);
//            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
//            input.getAttributesManager().savePersistentAttributes();
//        } catch (Exception ex) {
//            System.out.println(ex.toString());
//            throw ex;
//        }
//    }
    public static void newReadingList(HandlerInput input, String listName, String bookTitle) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ReadingList newList = new ReadingList(listName);
            Book currentBook = new Book(bookTitle);
            newList.add(currentBook);
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
//    public static void append(HandlerInput input, String listName, String bookTitle){
//        try {
//            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
//            ArrayList<String> listDynamo = (ArrayList<String>) persistentAttributes.get(listName);
//            Book currentBook = new Book(bookTitle);
//            listDynamo.add(currentBook.getTitle());
//            listDynamo.add(currentBook.getAuthor());
//            persistentAttributes.put(listName, listDynamo);
//            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
//            input.getAttributesManager().savePersistentAttributes();
//        } catch (Exception ex) {
//            System.out.println(ex.toString());
//            throw ex;
//        }
//    }
    public static void clear(HandlerInput input, String listName) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            persistentAttributes.remove(listName);
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
    public static boolean removeBook(HandlerInput input, String listName, String bookName) {
        try {
            Map<String, Object> persistentAttributes = new HashMap<>(input.getAttributesManager().getPersistentAttributes());
            ArrayList<String> listDynamo = (ArrayList<String>) persistentAttributes.get(listName);
            boolean containsBook;
            Book currentBook = new Book(bookName);
            String author = currentBook.getAuthor();
            Map<String, String> bookMap = new HashMap<>();
            bookMap.put("author", author);
            bookMap.put("title", bookName);
            if(listDynamo.contains(bookMap)){
                listDynamo.remove(bookMap);
                containsBook = true;
            }
            else{
                containsBook = false;
            }
            persistentAttributes.put(listName, listDynamo);
            input.getAttributesManager().setPersistentAttributes(persistentAttributes);
            input.getAttributesManager().savePersistentAttributes();
            return containsBook;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            throw ex;
        }
    }
}
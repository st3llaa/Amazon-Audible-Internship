package model;

import java.util.ArrayList;
import java.util.HashSet;

public class ReadingList implements java.io.Serializable {

    private HashSet<Book> books;
    private String listName;

    public ReadingList(String name){
        this.books = new HashSet<>();
        this.listName = name;
    }
    public ReadingList(){
        this.books = new HashSet<>();
        this.listName = "reading list";
    }
    public HashSet<Book> getBooks(){ return books; }
    public String getListName(){ return listName; }
    @Override
    public String toString() { return listName + ": " + books; }
    public void add(Book bookToAdd){ books.add(bookToAdd); }
}

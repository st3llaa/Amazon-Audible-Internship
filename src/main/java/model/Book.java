package model;

import com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers.OpenLibraryRepoAuthor;
import com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers.OpenLibraryRepository;

import java.io.InputStream;

public class Book implements java.io.Serializable {
    private final String title;
    private final String author;
    private String narrator;
    private OpenLibraryRepository library = new OpenLibraryRepository();

    public Book(String t, String a) {
        title = t;
        author = a;
    }
    public Book(String t){
        title = t;
        author = library.searchBook(title).getAuthor();
    }

    public String getAuthor() {
        return author;
    }
    public String getTitle() { return title; }
    public String toString(){ return title + ", by " + author; }
    public boolean equals(Object anotherObject){
        Book anotherBook = (Book)anotherObject;
        return this.title.equalsIgnoreCase(anotherBook.title);
    }
}

package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import java.io.InputStream;

public class Book {
    private String title;
    private String author;
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
}

package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import org.junit.Test;

import static org.junit.Assert.*;

public class OpenLibraryRepositoryTest {

    @Test
    public void searchBook(){
        OpenLibraryRepository test = new OpenLibraryRepository();
        System.out.println(test.searchBook("Perfume"));
    }
}
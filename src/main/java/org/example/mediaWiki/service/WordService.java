package org.example.mediaWiki.service;

import org.example.mediaWiki.modal.Word;

import java.util.List;

public interface WordService {
    void addWord(Word word);
    void removeWord(int id);
    boolean existingById(int id);
    String existingByTitle(String title);
    Word getWordById(int id);
    void editWord(Word word);
    List<Word> getAllUserWords();
}


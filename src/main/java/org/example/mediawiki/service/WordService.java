package org.example.mediawiki.service;

import org.example.mediawiki.modal.Word;

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


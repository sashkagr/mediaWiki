package org.example.service;

import org.example.modal.Word;

import java.util.List;

public interface WordService {
    void addWord(Word word);
    void removeWord(Word word);
    void editWord(Word word);
    List<Word> getAllUserWords();
}

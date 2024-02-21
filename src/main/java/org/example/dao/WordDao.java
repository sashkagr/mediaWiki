package org.example.dao;

import org.example.modal.Word;

import java.util.List;

public interface WordDao {
    void addWord(Word word);
    void removeWord(Word word);
    void editWord(Word word);
    List<Word> getUserWords();
}

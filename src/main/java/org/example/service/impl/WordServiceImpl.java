package org.example.service.impl;

import org.example.dao.WordDao;
import org.example.dao.impl.WordDaoImpl;
import org.example.modal.Word;
import org.example.service.WordService;

import java.util.List;

public enum WordServiceImpl implements WordService {
    INSTANCE;

    WordDao wordDao = WordDaoImpl.INSTANCE;

    @Override
    public void addWord(Word word) {
        wordDao.addWord(word);
    }

    @Override
    public void removeWord(Word word) {
        wordDao.removeWord(word);
    }

    @Override
    public void editWord(Word word) {
        wordDao.editWord(word);
    }

    @Override
    public List<Word> getAllUserWords() {
        return wordDao.getUserWords();
    }
}

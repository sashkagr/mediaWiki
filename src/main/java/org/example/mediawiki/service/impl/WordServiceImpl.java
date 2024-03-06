package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.WordRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class WordServiceImpl implements Service<Word> {

    @Autowired
    private WordRepository wordRepository;

    public boolean existingById(Long id) {
        return wordRepository.existsById(id);
    }

    public List<Word> existingBySearch(Search search) {
        List<Word> words = this.read();
        List<Word> result = new ArrayList<>();
        for (Word word : words) {
            if (word.getSearch() != null && word.getSearch().equals(search)) {
                result.add(word);
            }
        }
        return result;
    }

    public Word getWordById(Long id) {
        return wordRepository.getById(id);
    }

    public List<Word> existingBySearchId(Long id) {
        List<Word> words = this.read();
        List<Word> result = new ArrayList<>();
        for (Word word : words) {
            if (word.getSearch().getId() == id) {
                result.add(word);
            }
        }
        return result;
    }

    @Override
    public void create(Word entity) {
        wordRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        wordRepository.deleteById(id);
    }

    @Override
    public void update(Word entity) {
        wordRepository.save(entity);
    }

    @Override
    public List<Word> read() {
        return wordRepository.findAll();
    }
}

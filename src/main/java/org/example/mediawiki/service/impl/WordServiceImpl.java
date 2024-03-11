package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.WordRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class WordServiceImpl implements Service<Word> {

    @Autowired
    private WordRepository wordRepository;

    @Transactional
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

    @Transactional
    public Word getWordById(Long id) {
        return wordRepository.getById(id);
    }


    @Override
    public void create(Word entity) {
        wordRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        wordRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Word entity) {
        wordRepository.save(entity);
    }

    @Override
    @Transactional
    public List<Word> read() {
        return wordRepository.findAll();
    }
}

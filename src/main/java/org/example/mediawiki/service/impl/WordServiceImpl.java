package org.example.mediawiki.service.impl;

import org.example.mediawiki.cache.Cache;
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

    private Cache cache = new Cache();

    @Transactional
    public boolean existingById(Long id) {
        for (String key : cache.getCache().keySet()) {
            for (Word element : (List<Word>) cache.getCache().get(key)) {
                if (element.getId() == id) {
                    return true;
                }
            }
        }
        Word word = wordRepository.existingById(id);
        return word != null;

    }

    public List<Word> existingBySearch(Search search) {
        String cacheKey = Long.toString(search.getId());
        Object cachedData = cache.get(cacheKey);
        if (cachedData != null) {
            return (List<Word>) cachedData;
        } else {
            List<Word> result = wordRepository.existingBySearch(search);
            if (result != null) {
                cache.put(cacheKey, result);
            }
            return result;
        }
    }

    @Transactional
    public Word getWordById(Long id) {
        for (String key : cache.getCache().keySet()) {
            for (Word element : (List<Word>) cache.getCache().get(key)) {
                if (element.getId() == id) {
                    return element;
                }
            }
        }
        Word word = wordRepository.existingById(id);
        List<Word> words = new ArrayList<>();
        Object cachedData = cache.get(Long.toString((word.getSearch().getId())));
        if (cachedData != null) {
            cache.remove(Long.toString((word.getSearch().getId())));
            words = (List<Word>) cachedData;
        }
        words.add(word);
        cache.put((Long.toString((word.getSearch().getId()))), words);

        return word;
    }

    @Override
    public void create(Word entity) {
        wordRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        for (String key : cache.getCache().keySet()) {
            List<Word> words = (List<Word>) cache.getCache().get(key);
            for (Word element : words) {
                if (element.getId() == id) {
                    words.remove(element);
                    cache.remove(key);
                    cache.put(key, words);
                    break;
                }
            }
        }
        wordRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Word entity) {
        for (String key : cache.getCache().keySet()) {
            List<Word> words = (List<Word>) cache.getCache().get(key);
            for (Word element : words) {
                if (element.getId() == entity.getId()) {
                    words.remove(element);
                    cache.remove(key);
                    cache.put(key, words);
                    break;
                }
            }
        }
        wordRepository.save(entity);
    }


    @Override
    @Transactional
    public List<Word> read() {
        cache.clear();
        List<Word> words = wordRepository.findAll();
        for (Word word : words) {
            List<Word> wordsList = (List<Word>) cache.get((Long.toString(word.getSearch().getId())));
            if (wordsList != null) {
                cache.remove((Long.toString(word.getId())));
                wordsList.add(word);
            }
            cache.put((Long.toString(word.getId())), words);
        }
        return words;

    }

    @Transactional
    public List<Word> findWordByTitle(String title) {
        List<Word> words = new ArrayList<>();
        for (String key : cache.getCache().keySet()) {
            for (Word element : (List<Word>) cache.getCache().get(key)) {
                if (element.getTitle() == title) {
                    words.add(element);
                }
            }
        }
        if (!words.isEmpty()) {
            return words;
        } else {
            List<Word> wordsByTitle = wordRepository.findWordByTitle(title);
            for (Word word : wordsByTitle) {
                words = (List<Word>) cache.get(Long.toString(word.getSearch().getId()));
                if (words == null) {
                    words = new ArrayList<>();
                } else {
                    cache.remove((Long.toString(word.getSearch().getId())));
                }
                words.add(word);
                cache.put((Long.toString(word.getSearch().getId())), words);
            }
            return wordsByTitle;
        }
    }


}

package org.example.mediawiki.service.impl;


import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.WordRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class WordServiceImpl implements Service<Word> {

    private final WordRepository wordRepository;
    private final PagesServiceImpl pagesService;
    private final SearchServiceImpl searchService;

    @Autowired
    public WordServiceImpl(final PagesServiceImpl pages,
                           final WordRepository word,
                           final SearchServiceImpl search) {
        this.pagesService = pages;
        this.wordRepository = word;
        this.searchService = search;
    }

    @Transactional
    public List<Word> createWords(final List<Word> words, final List<Long> params) {
        for (Word word : words) {
            Long id = params.get(words.indexOf(word));
            Search search = searchService.getSearchById(id);
            if (search != null) {
                word.setSearch(search);
            }
        }
        return words.stream()
                .filter(word -> word.getTitle() != null && word.getDescription() != null)
                .map(wordRepository::save)
                .toList();
    }

    @Transactional
    @Cacheable(value = "words", key = "#id")
    public boolean getExistingById(final Long id) {
        return wordRepository.existingById(id) != null;
    }

    @CachePut(value = "words", key = "#search.id")
    public List<Word> getWordBySearch(final Search search) {
        return wordRepository.existingBySearch(search);
    }

    @Cacheable(value = "words", key = "#id")
    public Word getWordById(final Long id) {
        return wordRepository.existingById(id);
    }

    @Override
    public void create(final Word entity) {
        wordRepository.save(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "words", allEntries = true)
    public void delete(final Long id) {
        wordRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(final Word entity) {
        wordRepository.save(entity);
    }

    @Override
    @Transactional
    public List<Word> read() {
        return wordRepository.findAll();
    }

    @Transactional
    public List<Word> getWordByTitle(final String title) {
        return wordRepository.findWordByTitle(title);
    }

    public List<Word> getWordsFromPages(final Search search) {
        List<Word> words = new ArrayList<>();
        List<Pages> pages = pagesService.getPagesBySearch(search);
        for (Pages page : pages) {
            Word word = new Word();
            word.setId(page.getPageId());
            word.setTitle(page.getTitle());
            word.setSearch(search);
            words.add(word);
        }
        return words;
    }
}

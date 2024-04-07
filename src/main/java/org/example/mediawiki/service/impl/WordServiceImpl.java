package org.example.mediawiki.service.impl;

import org.example.mediawiki.cache.Cache;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.WordRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    private Cache cache = new Cache();


    @Transactional
    public List<Word> createWords(final List<Word> words, final List<Long> params) {
        Iterator<Word> listIterator = words.iterator();
        Iterator<Long> idIterator = params.iterator();

        while (listIterator.hasNext() && idIterator.hasNext()) {
            Word word = listIterator.next();
            Long id = idIterator.next();

            Search search = searchService.getSearchById(id);
            if (search != null) {
                word.setSearch(search);
            }
        }
        return words.stream()
                .filter(word -> word.getTitle() != null && word.getDescription() != null)
                .map(wordRepository::save)
                .toList(); // использование метода toList() вместо collect(Collectors.toList())

    }

    @Transactional
    public boolean getExistingById(final Long id) {
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

    public List<Word> getWordBySearch(final Search search) {
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
    public Word getWordById(final Long id) {
        for (String key : cache.getCache().keySet()) {
            for (Word element : (List<Word>) cache.getCache().get(key)) {
                if (element.getId() == id) {
                    return element;
                }
            }
        }
        Word word = wordRepository.existingById(id);
        List<Word> words = new ArrayList<>();
        Object cachedData = cache.
                get(Long.toString((word.getSearch().getId())));
        if (cachedData != null) {
            cache.remove(Long.toString((word.getSearch().getId())));
            words = (List<Word>) cachedData;
        }
        words.add(word);
        cache.put((Long.toString((word.getSearch().getId()))), words);

        return word;
    }

    @Override
    public void create(final Word entity) {
        wordRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
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
    public void update(final Word entity) {
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
            Long searchId;
            if (word.getSearch() == null) {
                searchId = -1L;
            } else {
                searchId = word.getSearch().getId();
            }

            List<Word> wordsList = (List<Word>) cache.
                    get(Long.toString(searchId));
            if (wordsList != null) {
                cache.remove(Long.toString(word.getId()));
                wordsList.add(word);
            } else {
                wordsList = new ArrayList<>();
                wordsList.add(word);
                cache.put(Long.toString(searchId), wordsList);
            }
        }
        return words;
    }

    @Transactional
    public List<Word> getWordByTitle(final String title) {
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
            List<Word> wordsByTitle = wordRepository.
                    findWordByTitle(title);
            for (Word word : wordsByTitle) {
                words = (List<Word>) cache.
                        get(Long.toString(word.getSearch().getId()));
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

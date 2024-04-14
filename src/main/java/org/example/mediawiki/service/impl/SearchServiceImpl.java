
package org.example.mediawiki.service.impl;

import org.example.mediawiki.controller.WikiApiRequest;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.repository.SearchRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
public class SearchServiceImpl implements Service<Search> {

    private final SearchRepository searchRepository;
    private final PagesServiceImpl pagesService;

    @Autowired
    public SearchServiceImpl(final PagesServiceImpl pages, final SearchRepository search) {
        this.pagesService = pages;
        this.searchRepository = search;
    }

    @Transactional
    @CachePut(value = "searches", key = "#id")
    public boolean getSearchExistingById(final Long id) {
        return searchRepository.existingById(id) != null;
    }

    @Transactional
    @CachePut(value = "searches", key = "#title")
    public Search getSearchByTitle(final String title) {
        return searchRepository.existingByTitle(title);
    }

    @Transactional
    @CachePut(value = "searches", key = "#id")
    public Search getSearchById(final Long id) {
        return searchRepository.existingById(id);
    }

    @Override
    @CachePut(value = "searches", key = "#entity.id")
    public void create(final Search entity) {
        searchRepository.save(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "searches", key = "#id")
    public void delete(final Long id) {
        searchRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "searches", key = "#entity.id")
    public void update(final Search entity) {
        searchRepository.save(entity);
    }

    @Override
    public List<Search> read() {
        return searchRepository.findAll();
    }

    @Transactional
    public List<Word> createSearchAndPages(final String name) throws InterruptedException {
        List<Word> words = WikiApiRequest.getDescriptionByTitle(name);
        Search search = new Search();
        search.setTitle(name);
        create(search);
        for (Word word : words) {
            Pages page = new Pages();
            word.setSearch(search);
            word.setDescription(word.getDescription().
                    replaceAll("\\<[^\\\\>]*+\\>", ""));
            page.setPageId(word.getId());
            page.setTitle(word.getTitle());
            Pages existingPage = pagesService.
                    getPageByPageId(page.getPageId());
            if (existingPage != null) {
                existingPage.getSearches().add(search);
                pagesService.update(existingPage);
            } else {
                page.getSearches().add(search);
                pagesService.create(page);
            }
        }
        return words;
    }
}

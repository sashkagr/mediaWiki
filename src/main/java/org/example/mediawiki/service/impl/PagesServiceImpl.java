package org.example.mediawiki.service.impl;

import org.example.mediawiki.cache.Cache;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class PagesServiceImpl implements Service<Pages> {

    private final PagesRepository pagesRepository;

    @Autowired
    public PagesServiceImpl(final PagesRepository repository) {
        this.pagesRepository = repository;
    }

    private Cache cache = new Cache();

    public Pages getPageByPageId(final Long pageId) {
        for (String key : cache.getCache().keySet()) {
            for (Pages element : (List<Pages>) cache.getCache().get(key)) {
                if (element.getPageId() == pageId) {
                    return element;
                }
            }
        }
        return pagesRepository.existingByPageId(pageId);
    }

    @Override
    @Transactional
    public void create(final Pages entity) {
        pagesRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        for (String key : cache.getCache().keySet()) {
            List<Pages> pages = (List<Pages>) cache.getCache().get(key);
            for (Pages element : pages) {
                if (element.getId() == id) {
                    pages.remove(element);
                    cache.remove(key);
                    cache.put(key, pages);
                    break;
                }
            }
        }
        pagesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(final Pages entity) {
        for (String key : cache.getCache().keySet()) {
            List<Pages> pages = (List<Pages>) cache.getCache().get(key);
            for (Pages element : pages) {
                if (element.getId() == entity.getId()) {
                    pages.remove(element);
                    cache.remove(key);
                    cache.put(key, pages);
                    break;
                }
            }
        }
        pagesRepository.save(entity);

    }

    @Override
    @Transactional
    public List<Pages> read() {
        cache.clear();
        List<Pages> pages = pagesRepository.findAll();
        for (Pages page : pages) {
            List<Pages> pagesList = (List<Pages>) cache.get(Long.toString(page.getId()));
            if (pagesList != null) {
                cache.remove(Long.toString(page.getId()));
                pagesList.add(page);
            } else {
                pagesList = new ArrayList<>();
                pagesList.add(page);
                cache.put(Long.toString(page.getId()), pagesList);
            }
        }
        return pages;
    }

    public List<Pages> getPagesBySearch(final Search search) {
        List<Pages> result = new ArrayList<>();
        List<Pages> pages;
        for (String key : cache.getCache().keySet()) {
            pages = (List<Pages>) cache.getCache().get(key);
            for (Pages page : pages) {
                if (page.getSearches().contains(search)) {
                    result.add(page);
                }
            }
        }
        if (result.isEmpty()) {

            pages = pagesRepository.existingBySearch(search);
            for (Pages page : pages) {
                result.add(page);
                List<Pages> pagesList = (List<Pages>) cache.
                        get(Long.toString(page.getId()));
                if (pagesList == null) {
                    pagesList = new ArrayList<>();
                }
                cache.remove(Long.toString(page.getId()));
                pagesList.add(page);
                cache.put(Long.toString(page.getId()), pagesList);
            }


        }
        return result;
    }
}



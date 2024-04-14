package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;


@org.springframework.stereotype.Service
public class PagesServiceImpl implements Service<Pages> {

    private final PagesRepository pagesRepository;

    @Autowired
    public PagesServiceImpl(final PagesRepository repository, final CacheManager cacheManager) {
        this.pagesRepository = repository;
    }

    @Override
    @Transactional
    public void create(final Pages entity) {
        pagesRepository.save(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "pages", allEntries = true)
    public void delete(final Long id) {
        pagesRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "pages", key = "#entity.id")
    public void update(final Pages entity) {
        pagesRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pages> read() {
        return pagesRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pages", key = "#pageId")
    public Pages getPageByPageId(final Long pageId) {
        return pagesRepository.existingByPageId(pageId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "pages", key = "#search.hashCode()")
    public List<Pages> getPagesBySearch(final Search search) {
        return pagesRepository.existingBySearch(search);
    }
}


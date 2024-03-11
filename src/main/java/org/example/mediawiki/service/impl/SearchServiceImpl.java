package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.SearchRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class SearchServiceImpl implements Service<Search> {

    @Autowired
    private SearchRepository searchRepository;

    @Transactional
    public boolean existingById(Long id) {
        return searchRepository.existsById(id);
    }

    public Search existingByTitle(String title) {
        List<Search> searches = this.read();
        for (Search search : searches) {
            if (search.getTitle().toLowerCase().equals(title.toLowerCase())) {
                return search;
            }
        }
        return null;
    }

    @Transactional
    public Search getSearchById(Long id) {
        return searchRepository.getById(id);
    }

    @Override
    @Transactional
    public void create(Search entity) {
        searchRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        searchRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Search entity) {
        searchRepository.save(entity);
    }

    @Override
    @Transactional
    public List<Search> read() {
        return searchRepository.findAll();
    }
}

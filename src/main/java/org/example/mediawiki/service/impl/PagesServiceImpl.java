package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

@org.springframework.stereotype.Service
public class PagesServiceImpl implements Service<Pages> {

    @Autowired
    private PagesRepository pagesRepository;

    public Pages existingByPageId(Long pageId) {
        List<Pages> pages = this.read();
        for (Pages page : pages) {
            if (page.getPageId()==pageId) {
                return page;
            }
        }
        return null;
    }

public void deleteBySearchId(Long id) {
    List<Pages> pages = this.read();
    for (Pages page : pages) {
        Iterator<Search> iterator = page.getSearchSet().iterator();
        while (iterator.hasNext()) {
            Search search = iterator.next();
            if (search.getId() == id) {
                iterator.remove();
                if (page.getSearchSet().isEmpty()) {
                    this.delete(page.getId());
                } else {
                    this.update(page);
                }
            }
        }
    }
}
    @Override
    public void create(Pages entity) {
        pagesRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        pagesRepository.deleteById(id);
    }

    @Override
    public void update(Pages entity) {
        pagesRepository.save(entity);

    }

    @Override
    public List<Pages> read() {
        return pagesRepository.findAll();
    }
}



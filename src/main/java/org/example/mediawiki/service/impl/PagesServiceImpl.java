package org.example.mediawiki.service.impl;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.repository.PagesRepository;
import org.example.mediawiki.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class PagesServiceImpl implements Service<Pages> {

    @Autowired
    private PagesRepository pagesRepository;

    public Pages existingByPageId(Long pageId) {
        List<Pages> pages = this.read();
        for (Pages page : pages) {
            if (page.getPageId() == pageId) {
                return page;
            }
        }
        return null;
    }


    @Override
    @Transactional
    public void create(Pages entity) {
        pagesRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        pagesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Pages entity) {
        pagesRepository.save(entity);

    }

    @Override
    @Transactional
    public List<Pages> read() {
        return pagesRepository.findAll();
    }
}



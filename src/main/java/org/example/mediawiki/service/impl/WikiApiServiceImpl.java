package org.example.mediawiki.service.impl;

import org.example.mediawiki.controller.WikiApiRequest;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.service.WikiApiService;

import java.util.List;

@org.springframework.stereotype.Service
public class WikiApiServiceImpl implements WikiApiService {
    @Override
    public List<Word> getDescriptionByTitle(String title) throws InterruptedException {
        return WikiApiRequest.getDescriptionByTitle(title);
    }
}

package org.example.mediawiki.service;

import org.example.mediawiki.modal.Word;

import java.util.List;

public interface WikiApiService {
    List<Word> getDescriptionByTitle(String title) throws InterruptedException;
}

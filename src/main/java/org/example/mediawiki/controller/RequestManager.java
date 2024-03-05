package org.example.mediawiki.controller;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.example.mediawiki.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/search")
public class RequestManager {

    private static final Logger logger = Logger.getLogger(RequestManager.class.getName());
    private List<Word> words = new ArrayList<>();
    private Search search = new Search();

    @Autowired
    private WordServiceImpl wordService;

    @Autowired
    private PagesServiceImpl pagesService;

    @Autowired
    private SearchServiceImpl searchService;

    @GetMapping
    public List<Word> getDefinition(@RequestParam String name) {
        search = searchService.existingByTitle(name);
        if (search != null) {
            words = wordService.existingBySearch(search);
            if (!words.isEmpty()) {
                return words;
            } else {
                return getWordsFromPages(search);
            }
        } else {
            return createSearchAndWords(name);
        }
    }

    private List<Word> getWordsFromPages(Search search) {
        List<Pages> pages = pagesService.read();
        for (Pages page : pages) {
            if (page.getSearchSet().contains(search)) {
                Word word = new Word();
                Long pageId = page.getPageId();
                word.setId(pageId);
                word.setTitle(page.getTitle());
                word.setSearch(search);
                words.add(word);
            }
        }
        return words;
    }

    private List<Word> createSearchAndWords(String name) {
        words = WikiApiRequest.getDescriptionByTitle(name);
        search = new Search();
        search.setTitle(name);
        searchService.create(search);
        for (Word word : words) {
            Pages page = new Pages();
            word.setSearch(search);
            word.setDescription(word.getDescription().replaceAll("\\<[^\\\\>]*+\\>", ""));
            page.setPageId(word.getId());
            page.setTitle(word.getTitle());
            Pages page1 = pagesService.existingByPageId(page.getPageId());
            if (page1 != null) {
                page1.getSearchSet().add(search);
                pagesService.update(page1);
            } else {
                page.getSearchSet().add(search);
                pagesService.create(page);
            }
        }
        return words;
    }

    @GetMapping("/{id}")
    public Word definitionController(@PathVariable Long id) {
        try {
            for (Word word : words) {
                if (word.getId() == id) {
                    Word retrievedWord = WikiApiRequest.getDescriptionByPageId(id);
                    retrievedWord.setSearch(search);
                    wordService.create(retrievedWord);
                    pagesService.existingByPageId(id).getSearchSet().clear();
                    pagesService.delete(pagesService.existingByPageId(id).getId());
                    return retrievedWord;
                }
            }
        } catch (NumberFormatException | IOException e) {
            logger.info("Invalid input!");
        }
        return null;
    }
    @PatchMapping("/{id}")
    public String updateSearch(@PathVariable Long id, @RequestBody Search newSearch) {
        Search existingSearch = searchService.getSearchById(id);
        if (existingSearch != null && newSearch.getTitle() != null) {
            existingSearch.setTitle(newSearch.getTitle());
            searchService.update(existingSearch);
            return "Search was updated";
        }

        return "Invalid input!";
    }


    @PostMapping("/add")
    public Word create(@RequestBody Word word) {
        if (word.getTitle() != null && word.getDescription() != null) {
            wordService.create(word);
        } else {
            logger.info("Error! You entered incorrect data!");
        }
        return word;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        String message = "";
        if (wordService.existingById(id)) {
            wordService.delete(id);
            message = "Word was deleted";
        } else {
            message = "Error! Word does not exist!";
        }
        return message;
    }

    @DeleteMapping("/delete-search/{id}")
    public String deleteSearch(@PathVariable Long id) {
        String message = "";
        if (searchService.existingById(id)) {
            List<Word> wordsBySearch = wordService.existingBySearchId(id);
            if(!wordsBySearch.isEmpty()) {
                for (Word word : wordsBySearch) {
                    wordService.delete(word.getId());
                }
            }
            pagesService.deleteBySearchId(id);
            searchService.delete(id);
            message = "Search was deleted";
        } else {
            message = "Error! Word does not exist!";
        }
        return message;
    }


    @PutMapping("/update/{id}")
    public String update(@RequestBody Word word, @PathVariable Long id) {
        String message = "Error! You entered incorrect data!";
        Word word1 = wordService.getWordById(id);
        if (word.getTitle() != null && word.getDescription() != null) {
            word1.setTitle(word.getTitle());
            word1.setDescription(word.getDescription());
            word1.setSearch(word.getSearch());
            wordService.update(word1);
            message = "Word was update";
        }
        return message;
    }


}



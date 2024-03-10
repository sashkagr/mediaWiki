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

    @Autowired
    private WordServiceImpl wordService;

    @Autowired
    private PagesServiceImpl pagesService;

    @Autowired
    private SearchServiceImpl searchService;

    Search currentSearch = new Search();

    @GetMapping
    public List<Word> getDefinition(@RequestParam String name) {
        Search search = searchService.existingByTitle(name);
        List<Word> words = new ArrayList<>();
        if (search != null) {
            words = wordService.existingBySearch(search);
            if (words.isEmpty()) {
                words = getWordsFromPages(search);
            }
            currentSearch = search;
        } else {
            words = createSearchAndWords(name);
        }

        return words;
    }

    private List<Word> getWordsFromPages(Search search) {
        List<Word> words = new ArrayList<>();
        List<Pages> pages = pagesService.read();
        for (Pages page : pages) {
            if (page.getSearches().contains(search)) {
                Word word = new Word();
                word.setId(page.getPageId());
                word.setTitle(page.getTitle());
                word.setSearch(search);
                words.add(word);
            }
        }
        return words;
    }

    private List<Word> createSearchAndWords(String name) {
        List<Word> words = WikiApiRequest.getDescriptionByTitle(name);
        Search search = new Search();
        search.setTitle(name);
        searchService.create(search);
        for (Word word : words) {
            Pages page = new Pages();
            word.setSearch(search);
            word.setDescription(word.getDescription().replaceAll("\\<[^\\\\>]*+\\>", ""));
            page.setPageId(word.getId());
            page.setTitle(word.getTitle());
            Pages existingPage = pagesService.existingByPageId(page.getPageId());
            if (existingPage != null) {
                existingPage.getSearches().add(search);
                pagesService.update(existingPage);
            } else {
                page.getSearches().add(search);
                pagesService.create(page);
            }
        }
        currentSearch = search;
        return words;
    }

    @GetMapping("/{id}")
    public Word definitionController(@PathVariable Long id) {
        try {
            Word retrievedWord = WikiApiRequest.getDescriptionByPageId(id);
            retrievedWord.setSearch(currentSearch);
            wordService.create(retrievedWord);
            Pages page = pagesService.existingByPageId(id);
            if (page != null) {
                for (Search search : page.getSearches()) {
                    search.getPages().remove(page);
                }
                page.getSearches().clear();
                pagesService.update(page);
                pagesService.delete(page.getId());
            }
            return retrievedWord;
        } catch (NumberFormatException | IOException e) {
            logger.info("Invalid input!");
            return null;
        }
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


    @GetMapping("/showSearches")
    public List<Search> showAllSearches() {
        return searchService.read();
    }

    @GetMapping("/showPages")
    public List<Pages> showAllPages() {
        return pagesService.read();
    }

    @PostMapping("/add/{id}")
    public Word create(@RequestBody Word word, @PathVariable Long id) {
        if (word.getTitle() != null && word.getDescription() != null) {
            if (id != null) {
                Search search = searchService.getSearchById(id);
                word.setSearch(search);
            }
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
            if (word.getSearch() != null) {
                Search searchCurrent = searchService.existingByTitle(word.getSearch().getTitle());
                if (searchCurrent != null) {
                    word1.setSearch(word.getSearch());
                }
            }
            wordService.update(word1);
            message = "Word was update";
        }
        return message;
    }


}



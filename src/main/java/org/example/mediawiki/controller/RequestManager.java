package org.example.mediawiki.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.CounterServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.example.mediawiki.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/definition")
public class RequestManager {
    private final WordServiceImpl wordService;

    private final PagesServiceImpl pagesService;

    private final SearchServiceImpl searchService;

    @Autowired
    public RequestManager(final WordServiceImpl word,
                          final PagesServiceImpl pages,
                          final SearchServiceImpl search) {
        this.wordService = word;
        this.pagesService = pages;
        this.searchService = search;
    }

    private Search currentSearch = new Search();

    private static final String REDIRECT = "redirect:/definition/showWords";
    private static final String ERROR = "error";

    @PostMapping
    public ResponseEntity<List<Word>> addWords(@RequestParam final List<Long> params, @RequestBody final List<Word> words) {
        List<Word> createdWords = wordService.createWords(words, params);
        if (createdWords == null || createdWords.isEmpty()) {
            log.error("Bad request");
            return new ResponseEntity<>(createdWords, HttpStatus.BAD_REQUEST);
        }
        if (createdWords.size() < words.size()) {
            log.warn("Some requests were bad");
        }

        return new ResponseEntity<>(createdWords, HttpStatus.CREATED);
    }

    @GetMapping("/findByTitle")
    public List<Word>
    getWordByTitle(@RequestParam("title") final String title) {
        CounterServiceImpl.incrementCount();
        int requestCount = CounterServiceImpl.getCount();
        log.info("Current count of requests findByTitle: {}", requestCount);
        return wordService.getWordByTitle(title);
    }

    @GetMapping("/findById/{id}")
    public String
    getWordById(@PathVariable final Long id, Model model) {
        CounterServiceImpl.incrementCount();
        int requestCount = CounterServiceImpl.getCount();
        log.info("Current count of requests findById: {}", requestCount);
        Word word = wordService.getWordById(id);
        model.addAttribute("word", word);
        return "update";
    }

    @GetMapping
    public String getDefinition(@RequestParam final String name, Model model) {
        try {
            if (name == null || name.equals("")) {
                log.error("An error occurred while processing the bad request");
                return ERROR;
            }
            log.info("Received request to get definition for: {}", name);

            Search search = searchService.getSearchByTitle(name);
            List<Word> words;

            if (search != null) {
                log.info("Search found for '{}'. Retrieving words", name);
                words = wordService.getWordBySearch(search);

                if (words.isEmpty()) {
                    log.info("No words found for '{}'. Attempting to retrieve from pages", name);
                    words = wordService.getWordsFromPages(search);
                    model.addAttribute("pages", words);
                    return "pages";
                }
                model.addAttribute("words", words);
                currentSearch = search;

                return "words";
            } else {
                log.info("No search found for '{}'. Creating search and pages", name);
                words = searchService.createSearchAndPages(name);
                currentSearch = searchService.getSearchByTitle(name);
            }

            log.info("Returning {} words for '{}'", words.size(), name);
            model.addAttribute("pages", words);
            return "pages";
        } catch (Exception e) {
            log.error("An error occurred while processing the request for '{}'", name, e);
            return ERROR;
        }
    }

    @GetMapping("/{id}")
    public String
    createWordBySearch(@PathVariable final Long id) {
        try {
            if (id == null) {
                return ERROR;
            }
            Word retrievedWord = WikiApiRequest.getDescriptionByPageId(id);
            retrievedWord.setSearch(currentSearch);
            wordService.create(retrievedWord);
            Pages page = pagesService.getPageByPageId(id);
            if (page != null) {
                for (Search search : page.getSearches()) {
                    search.getPages().remove(page);
                }
                page.getSearches().clear();
                pagesService.update(page);
                pagesService.delete(page.getId());
            }
            return REDIRECT;
        } catch (NumberFormatException | IOException e) {
            log.error("Invalid input!", e);
            return ERROR;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String>
    updateSearch(@PathVariable final Long id,
                 @RequestBody final Search newSearch) {
        try {
            log.info("Received request to update search with id: {}", id);
            Search existingSearch = searchService.getSearchById(id);
            if (existingSearch != null && newSearch.getTitle() != null) {
                existingSearch.setTitle(newSearch.getTitle());
                searchService.update(existingSearch);
                log.info("Search with id {} was successfully updated", id);
                return ResponseEntity.ok().body("Search was updated");
            } else {
                log.info("Invalid input or search not found for id: {}", id);
                return ResponseEntity.badRequest().body("Invalid input!");
            }
        } catch (Exception e) {
            log.error("An error occurred while updating search "
                    + "with id: {}", id, e);
            return ResponseEntity.badRequest().
                    body("An error occurred while updating search.");
        }
    }

    @GetMapping("/showSearches")
    public String showAllSearches(Model model) {
        CounterServiceImpl.incrementCount();
        int requestCount = CounterServiceImpl.getCount();
        log.info("Current count of requests showSearches: {}", requestCount);
        List<Search> searches = searchService.read();
        model.addAttribute("searches", searches);
        return "searches";
    }

    @GetMapping("/showPages")
    public List<Pages> showAllPages() {
        CounterServiceImpl.incrementCount();
        int requestCount = CounterServiceImpl.getCount();
        log.info("Current count of requests showPages: {}", requestCount);
        return pagesService.read();
    }

    @GetMapping("/showWords")
    public String showAllWords(Model model) {
        CounterServiceImpl.incrementCount();
        int requestCount = CounterServiceImpl.getCount();
        log.info("Current count of requests showWords: {}", requestCount);
        List<Word> words = wordService.read();
        model.addAttribute("words", words);
        return "definition";
    }

    @PostMapping("/add")
    public String createWord(@RequestParam(required = false) Long id,
                             @RequestParam String title,
                             @RequestParam String description) {
        try {
            log.info("Received request to create word with id: {}", id);
            Word word = new Word();
            if (title != null && description != null) {
                word.setTitle(title);
                word.setDescription(description);
                if (id != null) {
                    Search search = searchService.getSearchById(id);
                    word.setSearch(search);
                }
                wordService.create(word);
                log.info("Word successfully created");
                return REDIRECT;
            } else {
                log.info("Error! You entered incorrect data!");
                return ERROR;
            }
        } catch (Exception e) {
            log.error("An error occurred while creating word", e);
            return ERROR;

        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteWord(@PathVariable final Long id) {
        try {
            log.info("Received request to delete word with id: {}", id);

            if (wordService.getExistingById(id)) {
                wordService.delete(id);
                log.info("Word with id {} was successfully deleted", id);
                return REDIRECT;
            } else {
                log.info("Error! Word with id {} does not exist!", id);
                return ERROR;

            }
        } catch (Exception e) {
            log.error("An error occurred while deleting word with id: {}",
                    id, e);
            return ERROR;

        }
    }

    @DeleteMapping("/deleteSearch/{id}")
    public String
    deleteSearch(@PathVariable final Long id) {
        try {
            log.info("Received request to delete search with id: {}", id);
            if (searchService.getSearchExistingById(id)) {
                searchService.delete(id);
                log.info("Search with id {} was successfully deleted", id);
                return REDIRECT;
            } else {
                log.info("Error! Search with id {} does not exist!", id);
                return ERROR;

            }
        } catch (Exception e) {
            log.error("An error occurred while deleting search with id: {}",
                    id, e);
            return ERROR;

        }
    }

    @PutMapping("/update/{id}")
    public String updateWord(@RequestParam final String title,
                             @RequestParam final String description,
                             @PathVariable final Long id) {
        try {
            log.info("Received request to update word with id: {}", id);
            Word word1 = wordService.getWordById(id);
            if (word1 != null) {
                if (!title.equals("")) {
                    word1.setTitle(title);
                }
                if (!description.equals("")) {
                    word1.setDescription(description);
                }
                if(title.equals("")&&description.equals("")){
                    return ERROR;
                }
                wordService.update(word1);
                log.info("Word with id {} was successfully updated", id);
                return REDIRECT;
            } else {
                log.info("Error! You entered incorrect data or word "
                        + "with id {} does not exist!", id);
                return ERROR;

            }
        } catch (Exception e) {
            log.error("An error occurred while updating"
                    + " word with id: {}", id, e);
            return ERROR;

        }
    }

}



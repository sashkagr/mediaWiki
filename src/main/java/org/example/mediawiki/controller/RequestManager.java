package org.example.mediawiki.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.example.mediawiki.service.impl.PagesServiceImpl;
import org.example.mediawiki.service.impl.SearchServiceImpl;
import org.example.mediawiki.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
@RestController
@RequestMapping("/search")
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

    @GetMapping("/findByTitle")
    public List<Word>
    getWordByTitle(@RequestParam("title")final String title) {
        return wordService.getWordByTitle(title);
    }


    @GetMapping
    public ResponseEntity<List<Word>>
    getDefinition(@RequestParam final String name) {
        try {
            if (name == null || name.equals("")) {
                log.error("An error occurred while processing the bad request");
                return ResponseEntity.badRequest().build();
            }
            log.info("Received request to get definition for: {}", name);

            Search search = searchService.getSearchByTitle(name);
            List<Word> words = new ArrayList<>();

            if (search != null) {
                log.info("Search found for '{}'. Retrieving words", name);
                words = wordService.getWordBySearch(search);

                if (words.isEmpty()) {
                    log.info("No words found for '{}'."
                            + " Attempting to retrieve from pages", name);
                    words = wordService.getWordsFromPages(search);
                }

                currentSearch = search;
            } else {
                log.info("No search found for '{}'."
                        + " Creating search and pages", name);
                words = searchService.createSearchAndPages(name);
                currentSearch = searchService.getSearchByTitle(name);
            }

            log.info("Returning {} words for '{}'", words.size(), name);
            return ResponseEntity.ok(words);
        } catch (Exception e) {
            log.error("An error occurred while processing the request "
                    + "for '{}'", name, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Word>
    createWordBySearch(@PathVariable final Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().build();
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

            return ResponseEntity.ok().body(retrievedWord);
        } catch (NumberFormatException | IOException e) {
            log.error("Invalid input!", e);
            return ResponseEntity.badRequest().build();
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
    public List<Search> showAllSearches() {
        return searchService.read();
    }

    @GetMapping("/showPages")
    public List<Pages> showAllPages() {
        return pagesService.read();
    }

    @GetMapping("/showWords")
    public List<Word> showAllWords() {
        return wordService.read();
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Word> createWord(@RequestBody final Word word,
                                           @PathVariable final Long id) {
        try {
            log.info("Received request to create word with id: {}", id);

            if (word.getTitle() != null && word.getDescription() != null) {
                if (id != null) {
                    Search search = searchService.getSearchById(id);
                    word.setSearch(search);
                }
                wordService.create(word);
                log.info("Word successfully created");
                return ResponseEntity.ok().body(word);
            } else {
                log.info("Error! You entered incorrect data!");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error("An error occurred while creating word", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWord(@PathVariable final Long id) {
        try {
            log.info("Received request to delete word with id: {}", id);

            if (wordService.getExistingById(id)) {
                wordService.delete(id);
                log.info("Word with id {} was successfully deleted", id);
                return ResponseEntity.ok().
                        body("Word was deleted");
            } else {
                log.info("Error! Word with id {} does not exist!", id);
                return ResponseEntity.badRequest().
                        body("Error! Word does not exist!");
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting word with id: {}",
                    id, e);
            return ResponseEntity.badRequest().
                    body("An error occurred while deleting word.");
        }
    }

    @DeleteMapping("/delete-search/{id}")
    public ResponseEntity<String>
    deleteSearch(@PathVariable final Long id) {
        try {
            log.info("Received request to delete search with id: {}", id);

            if (searchService.getSearchExistingById(id)) {
                searchService.delete(id);
                log.info("Search with id {} was successfully deleted", id);
                return ResponseEntity.ok().
                        body("Search was deleted");
            } else {
                log.info("Error! Search with id {} does not exist!", id);
                return ResponseEntity.badRequest().
                        body("Error! Search does not exist!");
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting search with id: {}",
                    id, e);
            return ResponseEntity.badRequest().
                    body("An error occurred while deleting search.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateWord(@RequestBody final Word word,
                                             @PathVariable final Long id) {
        try {
            log.info("Received request to update word with id: {}", id);

            Word word1 = wordService.getWordById(id);
            if (word1 != null && word.getTitle() != null
                    && word.getDescription() != null) {
                word1.setTitle(word.getTitle());
                word1.setDescription(word.getDescription());
                if (word.getSearch() != null) {
                    Search searchCurrent = searchService.
                            getSearchByTitle(word.getSearch().getTitle());
                    if (searchCurrent != null) {
                        word1.setSearch(word.getSearch());
                    }
                }
                wordService.update(word1);
                log.info("Word with id {} was successfully updated", id);
                return ResponseEntity.ok().body("Word was updated");
            } else {
                log.info("Error! You entered incorrect data or word "
                        + "with id {} does not exist!", id);
                return ResponseEntity.badRequest().
                        body("Error! You entered incorrect data"
                                + " or word does not exist!");
            }
        } catch (Exception e) {
            log.error("An error occurred while updating"
                    + " word with id: {}", id, e);
            return ResponseEntity.badRequest().
                    body("An error occurred while updating word.");
        }
    }

}



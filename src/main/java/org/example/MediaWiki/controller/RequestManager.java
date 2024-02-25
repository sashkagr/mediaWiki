package org.example.MediaWiki.controller;

import org.example.MediaWiki.modal.Word;
import org.example.MediaWiki.service.impl.WordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    List<Word> words = new ArrayList<>();
    String titleForApi="";

    @Autowired
    WordServiceImpl wordService = new WordServiceImpl();

    @GetMapping
    public String getDefinition(@RequestParam String name) {

        words = wordService.getAllUserWords();
        String message ="";
        String description=wordService.existingByTitle(name);
            if (description!=null) {
                message="Correct! Definition:"+description;
            }
        else{
            message="Error! If you would to search in Wiki, enter parameter Yes in path /search/answer";
            titleForApi=name;
            }
      return message;
    }
    @GetMapping("/answer")
    public String definitionController(@RequestParam String answer) throws IOException {
        answer=answer.toLowerCase();
        List<Word> descriptions;
        descriptions=WikiApiRequest.getDescriptionByTitle(titleForApi);
        String listing = "";
        if(answer.equals("yes")) {
            int cnt = 1;
            StringBuilder builder = new StringBuilder();

            for (Word word : descriptions) {
                word.setDescription(word.getDescription().replaceAll("\\<[^\\\\>]*+\\>", ""));
                String count = Integer.toString(cnt);
                builder.append(count)
                        .append("|")
                        .append(word.getTitle())
                        .append("|")
                        .append(word.getDescription())
                        .append("\n");

                cnt++;
            }
            listing = builder.toString();
            listing += "Select number of title to insert it into SQL (to answer enter number in path /search/answer)";
        }
        else {
            try {
                int input = Integer.parseInt(answer);
                if (input > 0 && input <= descriptions.size()) {
                    int pageId = descriptions.get(input - 1).getId();
                    Word word = WikiApiRequest.getDescriptionByPageId(pageId);
                    wordService.addWord(word);
                    listing = word.getTitle() + "|" + word.getDescription()+"(Added to database)";
                }
            }
            catch (Exception e)
            {
                logger.info(e.getMessage());
                listing = "Error! Incorrect data";
            }

        }
        return listing;
    }
    @PostMapping("/add")
    public String create(@RequestBody Word word)
    {
        String message ="";
            if (wordService.existingByTitle(word.getTitle())!=null) {
                message=  "Error! Word exists" ;
            }
            else{
            if(word.getTitle()!=null&&word.getDescription()!=null)
            {
                wordService.addWord(word);
                message="Word was added to database";
            }
            else{
                message="Error! You entered incorrect data!";
            }
        }
        return message;
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id)
    {
        String message="";
        if(wordService.existingById(id)){
            wordService.removeWord(id);
            message="Word was deleted";
        }
        else{
            message="Error! Word does not exist!";
        }
       return message;
    }

    @PutMapping("/update/{id}")
    public String update(@RequestBody Word word, @PathVariable int id)
    {
        String message="";
        Word word1 = wordService.getWordById(id);
        if (word.getTitle()!= null&&word.getDescription()!=null)
        {
            word1.setTitle(word.getTitle());
            word1.setDescription(word.getDescription());
            wordService.editWord(word1);
            message="Word was update";
        }
       return message;
    }









    }



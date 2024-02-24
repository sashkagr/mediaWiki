package org.example.controller;

import org.example.modal.Word;
import org.example.service.WordService;
import org.example.service.impl.WordServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@RestController
@RequestMapping("/search")
public class RequestManager {
    private static final Logger logger = Logger.getLogger(RequestManager.class.getName());
    Scanner scanner = new Scanner(System.in);
    List<Word> words = new ArrayList<>();
    WordService wordService = WordServiceImpl.INSTANCE;
    @GetMapping
    public void getDefinition(@RequestParam String name) {

        words = wordService.getAllUserWords();
        boolean existance = false;
        for (Word word : words) {
            if (word.getTitle().equals(name)) {
                logger.info("Correct! Definition:" + word.getDescription());
                existance = true;
                break;
            }
        }
        if (!existance) {
            logger.info("Error! If you would to search in Wiki, enter Yes");
            String input = scanner.nextLine();
            if (input.equals("Yes"))
                 definitionController(name);
        }
    }

    public void definitionController(String name) {
        List<Word> descriptions;
        descriptions=WikiApiRequest.getDescriptionByTitle(name);
        int cnt=1;
        for (Word word : descriptions) {
            word.setDescription(word.getDescription().replaceAll("\\<[^\\\\>]*+\\>", ""));
            if(cnt>0)
            logger.info(String.format("| %d | %s | %s", cnt, word.getTitle(), word.getDescription()));
            cnt++;
        }
        logger.info("Select number of title to insert it into SQL");
        int input = scanner.nextInt();
        if (input>0&&input<= descriptions.size())
        {
            Word word= descriptions.get(input-1);
            wordService.addWord(word);

    }






    }


    }



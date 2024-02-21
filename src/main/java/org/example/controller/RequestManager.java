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
import java.util.Map;
import java.util.Scanner;

@RestController
@RequestMapping("/search")
public class RequestManager {

    Scanner scanner = new Scanner(System.in);
    List<Word> words = new ArrayList<>();
    WordService wordService = WordServiceImpl.INSTANCE;
    @GetMapping
    public void getDefinition(@RequestParam String name) {

        words = wordService.getAllUserWords();
        boolean existance = false;
        for (Word word : words) {
            if (word.getTitle().equals(name)) {
                System.out.println("Correct! Definition:" + word.getDescription());
                existance = true;
                break;
            }
        }
        if (!existance) {
            System.out.println("Error! If you would to search in Wiki, enter Yes");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("Yes"))
                 definitionController(name);
        }
    }

    public void definitionController(String name) {
        List<Word> words;
        words=WikiApiRequest.getDescriptionByTitle(name);
        int cnt=1;
        System.out.println("+---------------------------------------------------------------------------------------------------------------------+");
        System.out.println("|id  |    title                     | description  ");
        System.out.println("+---------------------------------------------------------------------------------------------------------------------+");
        for (Word word : words) {
            word.setDescription(word.getDescription().replaceAll("\\<.*?\\>", ""));
            System.out.printf("|%d| %30s | %s \n",cnt,word.getTitle(), word.getDescription());
            cnt++;
        }
        System.out.println("+---------------------------------------------------------------------------------------------------------------------+");

        System.out.println("Select number of title to insert it into SQL");
        int input = scanner.nextInt();
        if (input>0&&input<= words.size())
        {
            Word word= words.get(input-1);
            wordService.addWord(word);

    }






    }


    }



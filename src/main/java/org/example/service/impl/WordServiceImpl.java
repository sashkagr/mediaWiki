package org.example.service.impl;
import org.example.modal.Word;
import org.example.repository.WordRepository;
import org.example.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private  WordRepository wordRepository;


    @Override
    public void addWord(Word word) {
        wordRepository.save(word);
    }

    @Override
    public void removeWord(int id) {
       wordRepository.deleteById(id);
    }

    @Override
    public void editWord(Word word) {
        wordRepository.save(word);
    }

    @Override
    public List<Word> getAllUserWords() {
        return wordRepository.findAll();
    }
    @Override
    public boolean existingById(int id)
    {
        return wordRepository.existsById(id);
    }
    @Override
    public String existingByTitle(String title)
    {
        List<Word> words = this.getAllUserWords();
        for (Word word : words) {
            if (word.getTitle().equals(title)){
                return word.getDescription();
            }
        }
        return null;
    }

    @Override
    public Word getWordById(int id) {
        return wordRepository.getById(id);
    }


}

package org.example.mediaWiki.repository;

import org.example.mediaWiki.modal.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
}

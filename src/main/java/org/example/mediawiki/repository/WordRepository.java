package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("SELECT d FROM Word d WHERE d.title = :title")
    List<Word> findWordByTitle(@Param("title") String title);

    @Query("SELECT d FROM Word d WHERE d.id = :id")
    Word existingById(@Param("id") Long id);

    @Query("SELECT d FROM Word d WHERE d.search = :search")
    List<Word> existingBySearch(@Param("search") Search search);

}

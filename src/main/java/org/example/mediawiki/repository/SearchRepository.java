package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Search, Long> {
    @Query("SELECT d FROM Search d WHERE d.title = :title")
    Search existingByTitle(@Param("title") String title);

    @Query("SELECT d FROM Search d WHERE d.id = :id")
    Search existingById(@Param("id") Long id);
}

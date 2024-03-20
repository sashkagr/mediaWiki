package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagesRepository extends JpaRepository<Pages, Long> {
    @Query("SELECT p FROM Pages p WHERE p.pageId = :pageId")
    Pages existingByPageId(@Param("pageId") Long pageId);

    @Query("SELECT p FROM Pages p JOIN p.searches s WHERE s = :search")
    List<Pages> existingBySearch(@Param("search") Search search);

}

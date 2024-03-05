package org.example.mediawiki.repository;

import org.example.mediawiki.modal.Pages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagesRepository extends JpaRepository<Pages, Long> {
}

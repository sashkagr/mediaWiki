package org.example.mediawiki.service;

import java.util.List;

public interface Service<T> {
    void create(T entity);

    void delete(Long id);

    void update(T entity);

    List<T> read();
}


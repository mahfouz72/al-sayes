package org.example.backend.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> listAll();

    void insert(T t);

    Optional<T> getById(Long id);

    void update(Long id, T t);

    void delete(Long id);
}

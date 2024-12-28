package org.example.backend.dao;

import java.util.List;
import java.util.Optional;


public interface DAO<T, K> {
    List<T> listAll();

    void insert(T t);

    Optional<T> getByPK(K pKey);
    void update(K pKey, T t);

    void delete(K pKey);
}

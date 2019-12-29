package ru.javalab.socketsapp.repository.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    void save(T t);
    void delete(int id);

    Optional<T> findOne(ID id);
    List<T> findAll();
}

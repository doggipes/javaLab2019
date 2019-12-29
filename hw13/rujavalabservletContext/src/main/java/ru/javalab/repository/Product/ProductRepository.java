package ru.javalab.repository.Product;


import ru.javalab.models.Product;
import ru.javalab.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    void save(Product product);

    void delete(int id);

    List<Product> findAll();
}

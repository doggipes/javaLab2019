package ru.javalab.socketsapp.repository.repository.Product;

import ru.javalab.socketsapp.Dto.ProductDto;
import ru.javalab.socketsapp.models.Product;
import ru.javalab.socketsapp.repository.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    void save(Product product);

    void delete(int id);

    List<Product> findAll();
}

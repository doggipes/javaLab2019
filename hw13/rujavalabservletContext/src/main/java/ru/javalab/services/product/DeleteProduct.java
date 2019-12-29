package ru.javalab.services.product;


import ru.javalab.dto.Dto;
import ru.javalab.models.Product;

public interface DeleteProduct {
    Dto delete(Product product);
}

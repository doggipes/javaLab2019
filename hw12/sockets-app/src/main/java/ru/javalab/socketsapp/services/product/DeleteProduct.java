package ru.javalab.socketsapp.services.product;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.models.Product;

public interface DeleteProduct {
    Dto delete(Product product);
}
package ru.javalab.services.product;


import ru.javalab.dto.Dto;
import ru.javalab.dto.ProductDto;
import ru.javalab.models.Product;

public interface AddProduct {
    Dto save(ProductDto product);
}

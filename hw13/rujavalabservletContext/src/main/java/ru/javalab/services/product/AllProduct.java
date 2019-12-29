package ru.javalab.services.product;

import ru.javalab.dto.ProductDto;
import ru.javalab.models.Product;

import java.util.List;

public interface AllProduct {
    List<ProductDto> allProducts();
}

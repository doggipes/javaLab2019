package ru.javalab.services.product;

import ru.javalab.context.Component;
import ru.javalab.dto.ProductDto;
import ru.javalab.models.Product;
import ru.javalab.repository.Product.ProductRepository;
import ru.javalab.repository.Product.ProductRepositoryImpl;

import java.util.List;

public class AllProductImpl implements AllProduct, Component {
    private ProductRepositoryImpl productRepository;

    public AllProductImpl(){}

    @Override
    public String getName() {
        return "AllProduct";
    }

    @Override
    public List<ProductDto> allProducts() {
        return productRepository.find();
    }
}

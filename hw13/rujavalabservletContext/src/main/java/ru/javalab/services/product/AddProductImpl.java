package ru.javalab.services.product;


import ru.javalab.context.Component;
import ru.javalab.dto.Dto;
import ru.javalab.dto.ProductDto;
import ru.javalab.models.Product;
import ru.javalab.repository.Product.ProductRepositoryImpl;

public class AddProductImpl implements AddProduct, Component {
    ProductRepositoryImpl productRepositoryImpl;

    public AddProductImpl(){}

    public AddProductImpl(ProductRepositoryImpl productRepositoryImpl){
        this.productRepositoryImpl = productRepositoryImpl;
    }

    @Override
    public String getName() {
        return "AddProduct";
    }

    @Override
    public Dto save(ProductDto product) {
        productRepositoryImpl.save(product);
        return null;
    }
}

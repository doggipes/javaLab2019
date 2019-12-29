package ru.javalab.socketsapp.services.product;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.Product;
import ru.javalab.socketsapp.repository.repository.Product.ProductRepositoryImpl;

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
    public Dto save(Product product) {
        productRepositoryImpl.save(product);
        return null;
    }
}

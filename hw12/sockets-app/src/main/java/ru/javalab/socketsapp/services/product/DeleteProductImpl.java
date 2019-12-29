package ru.javalab.socketsapp.services.product;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.Product;
import ru.javalab.socketsapp.repository.repository.Product.ProductRepositoryImpl;

public class DeleteProductImpl implements DeleteProduct, Component {
    ProductRepositoryImpl productRepositoryImpl;

    public DeleteProductImpl(){}

    public DeleteProductImpl(ProductRepositoryImpl productRepositoryImpl){
        this.productRepositoryImpl = productRepositoryImpl;
    }

    @Override
    public String getName() {
        return "DeleteProduct";
    }

    @Override
    public Dto delete(Product product) {
        productRepositoryImpl.delete(product.getId());
        return null;
    }
}

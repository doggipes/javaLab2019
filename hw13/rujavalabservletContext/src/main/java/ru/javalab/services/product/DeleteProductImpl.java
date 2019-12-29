package ru.javalab.services.product;


import ru.javalab.context.Component;
import ru.javalab.dto.Dto;
import ru.javalab.models.Product;
import ru.javalab.repository.Product.ProductRepositoryImpl;

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

package ru.javalab.repository.Product;


import ru.javalab.context.Component;
import ru.javalab.dto.ProductDto;
import ru.javalab.models.Product;
import ru.javalab.repository.RowMapper;
import ru.javalab.util.connectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository, Component {
    private Connection connection;

    private RowMapper<ProductDto> productRowmapper = row -> new ProductDto(
            row.getInt("id"),
            row.getString("name"),
            row.getInt("price")
    );

    public ProductRepositoryImpl() throws SQLException {
        connection = connectionToDB.getInstance();
    }

    public ProductRepositoryImpl(Connection connection){
        this.connection = connection;
    }

    public void save(ProductDto product){
        String query = "INSERT INTO product(name, price) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, String.valueOf(product.getPrice()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Product product) {

    }

    public void delete(int id){
        String query = "DELETE FROM product WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Product> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    public List<ProductDto> find(){
        String query = "SELECT * FROM product";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ProductDto> list = new ArrayList<>();
            while(resultSet.next()){
                list.add(productRowmapper.mapRow(resultSet));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return "ProductRepository";
    }
}

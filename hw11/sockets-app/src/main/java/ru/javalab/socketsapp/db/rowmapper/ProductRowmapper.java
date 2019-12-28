package ru.javalab.socketsapp.db.rowmapper;

import ru.javalab.socketsapp.db.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowmapper {

    public static Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getString("price"));
        return product;
    }
}

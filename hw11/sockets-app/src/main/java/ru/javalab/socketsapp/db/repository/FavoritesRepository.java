package ru.javalab.socketsapp.db.repository;

import ru.javalab.socketsapp.db.models.Favorites;
import ru.javalab.socketsapp.db.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FavoritesRepository {
    private Connection connection;

    public FavoritesRepository(Connection connection){this.connection = connection;}

    public void save(Favorites favorites){
        String query = "INSERT INTO favorites (product_id, user_id) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, favorites.getProduct_id());
            preparedStatement.setInt(2, favorites.getUser_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}

package ru.javalab.socketsapp.repository.repository.Favorites;

import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.Favorites;
import ru.javalab.socketsapp.models.Product;
import ru.javalab.socketsapp.util.connectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FavoritesRepositoryImpl implements FavoritesRepository, Component {
    private Connection connection;

    public FavoritesRepositoryImpl() throws SQLException {connection = connectionToDB.getInstance();;}

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

    @Override
    public void delete(int id) {

    }

    @Override
    public Optional<Favorites> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Favorites> findAll() {
        return null;
    }

    @Override
    public String getName() {
        return "FavoritesRepository";
    }
}

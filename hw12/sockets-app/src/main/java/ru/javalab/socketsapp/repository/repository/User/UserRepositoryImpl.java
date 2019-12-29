package ru.javalab.socketsapp.repository.repository.User;

import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.User;
import ru.javalab.socketsapp.repository.repository.RowMapper;
import ru.javalab.socketsapp.util.connectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository, Component {
    private Connection connection;

    private RowMapper<User> userRowmapper = row -> new User (
            row.getInt("id"),
            row.getString("username"),
            row.getString("password"),
            row.getString("role")
    );

    public UserRepositoryImpl() throws SQLException {
        connection = connectionToDB.getInstance();
    }

    public void save(User user){
        String query = "INSERT INTO kontrolnaya.user (username, password) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Optional<User> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    public Optional<User> find(String name) throws SQLException {
        String query = "SELECT * FROM kontrolnaya.user WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        User user = new User();
        if(rs.next()){
            user = userRowmapper.mapRow(rs);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public String getName() {
        return "UserRepository";
    }
}

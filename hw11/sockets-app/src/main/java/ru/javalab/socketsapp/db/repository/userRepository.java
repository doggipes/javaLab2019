package ru.javalab.socketsapp.db.repository;

import ru.javalab.socketsapp.db.models.User;
import ru.javalab.socketsapp.db.rowmapper.Rowmapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userRepository {
    private Connection connection;
    private Rowmapper rowmapper;

    public userRepository(Connection connection){
        this.connection = connection;
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

    public User find(String name) throws SQLException {
        String query = "SELECT * FROM kontrolnaya.user WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        User user = new User();
        while(rs.next()) {
            rowmapper = new Rowmapper();
            user = rowmapper.mapRow(rs);
        }
        return user;
    }
}

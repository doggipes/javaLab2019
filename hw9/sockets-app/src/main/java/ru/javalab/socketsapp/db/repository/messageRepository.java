package ru.javalab.socketsapp.db.repository;

import ru.javalab.socketsapp.db.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class messageRepository {
    private Connection connection;

    public messageRepository(Connection connection){
        this.connection = connection;
    }

    public void save(Message message){
        String sqlQuery = "INSERT INTO kontrolnaya.message(from_id, text_message) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, message.getUserID());
            stmt.setString(2, message.getMessage());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}

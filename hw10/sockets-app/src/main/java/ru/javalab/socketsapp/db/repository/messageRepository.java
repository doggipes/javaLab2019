package ru.javalab.socketsapp.db.repository;

import ru.javalab.socketsapp.db.models.Message;
import ru.javalab.socketsapp.db.rowmapper.MessageRowmapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Message> findByPage(int page){
        String sqlQuery = "SELECT * FROM kontrolnaya.message ORDER BY `date` DESC LIMIT  ?, 5";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, (page - 1) * 5);
            ResultSet rs = stmt.executeQuery();
            List<Message> list = new ArrayList<>();
            while (rs.next()) {
                list.add(MessageRowmapper.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}

package ru.javalab.socketsapp.repository.repository.Message;

import ru.javalab.socketsapp.Dto.MessageDto;
import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.Message;
import ru.javalab.socketsapp.repository.repository.RowMapper;
import ru.javalab.socketsapp.util.connectionToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryImpl implements MessageRepository, Component {
    private Connection connection;

    private RowMapper<MessageDto> rowMapper = row -> new MessageDto(
                row.getString("text_message"),
                row.getInt("from_id")
            );

    public MessageRepositoryImpl() throws SQLException {
        connection = connection = connectionToDB.getInstance();;
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

    @Override
    public void delete(int id) {

    }

    @Override
    public Optional<Message> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        return null;
    }

    public List<MessageDto> findByPage(int page){
        String sqlQuery = "SELECT * FROM kontrolnaya.message ORDER BY `date` DESC LIMIT  ?, 5";
        try (PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, (page - 1) * 5);
            ResultSet rs = stmt.executeQuery();
            List<MessageDto> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getName() {
        return "MessageRepository";
    }
}

package ru.javalab.socketsapp.db.rowmapper;

import ru.javalab.socketsapp.db.models.Message;
import ru.javalab.socketsapp.db.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowmapper {

    public static Message mapRow(ResultSet row) throws SQLException {
        Message message = new Message();
        message.setID(row.getInt("id"));
        message.setMessage(row.getString("text_message"));
        message.setUserID(row.getInt("from_id"));
        return message;
    }
}

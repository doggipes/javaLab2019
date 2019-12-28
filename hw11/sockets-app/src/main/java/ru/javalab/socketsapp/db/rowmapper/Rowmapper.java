package ru.javalab.socketsapp.db.rowmapper;

import ru.javalab.socketsapp.db.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Rowmapper {

    public User mapRow(ResultSet row) throws SQLException {
        User user = new User();
        user.setID(row.getInt("id"));
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        user.setRole(row.getString("role"));
        return user;
    }
}

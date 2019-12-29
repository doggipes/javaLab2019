package ru.javalab.socketsapp.services.signIn;

import ru.javalab.socketsapp.Dto.UserDto;

import java.sql.SQLException;

public interface SignInService {
    UserDto signIn(String login, String password) throws SQLException;
}

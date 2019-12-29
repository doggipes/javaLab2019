package ru.javalab.services.signIn;


import ru.javalab.dto.UserDto;

import java.sql.SQLException;

public interface SignInService {
    UserDto signIn(String login, String password) throws SQLException;
}

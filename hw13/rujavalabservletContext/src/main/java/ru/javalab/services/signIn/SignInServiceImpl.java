package ru.javalab.services.signIn;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.context.Component;
import ru.javalab.dto.UserDto;
import ru.javalab.models.User;
import ru.javalab.repository.User.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.Optional;

public class SignInServiceImpl implements SignInService, Component {
    private UserRepositoryImpl userRepositoryImpl;

    public SignInServiceImpl(){
        try {
            userRepositoryImpl = new UserRepositoryImpl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SignInServiceImpl(UserRepositoryImpl usersRepository) {
        this.userRepositoryImpl = usersRepository;
    }


    @Override
    public UserDto signIn(String login, String password) throws SQLException {
        Optional<User> newUser = userRepositoryImpl.find(login);
        if(newUser.isPresent()){
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            if (encoder.matches(password, newUser.get().getPassword())) {
                UserDto userDto = new UserDto(newUser.get().getUsername(), newUser.get().getPassword());
                return userDto;
            }
        }else{
            userRepositoryImpl.save(new User(login, password));
            newUser = userRepositoryImpl.find(login);
            UserDto userDto = new UserDto(newUser.get().getUsername(), newUser.get().getPassword());
            return userDto;
        }
        return null;
    }

    @Override
    public String getName() {
        return "SignInService";
    }
}

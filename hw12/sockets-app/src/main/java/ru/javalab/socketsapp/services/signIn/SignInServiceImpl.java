package ru.javalab.socketsapp.services.signIn;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.socketsapp.Dto.UserDto;
import ru.javalab.socketsapp.context.Component;
import ru.javalab.socketsapp.models.User;
import ru.javalab.socketsapp.repository.repository.User.UserRepositoryImpl;
import ru.javalab.socketsapp.util.connectionToDB;
import ru.javalab.socketsapp.util.jwtToken;

import java.sql.SQLException;
import java.util.Optional;

public class SignInServiceImpl implements SignInService, Component {
    private UserRepositoryImpl userRepositoryImpl;

    public SignInServiceImpl(){
        try {
            userRepositoryImpl = new UserRepositoryImpl();
        } catch (SQLException e) {
            System.out.println(e);
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
                UserDto userDto = new UserDto(newUser.get().getId(), newUser.get().getUsername(), jwtToken.createToken(String.valueOf(newUser.get().getId()), newUser.get().getRole()));
                return userDto;
            }
        }else{
            userRepositoryImpl.save(new User(login, password));
            newUser = userRepositoryImpl.find(login);
            UserDto userDto = new UserDto(newUser.get().getId(), newUser.get().getUsername(), jwtToken.createToken(String.valueOf(newUser.get().getId()), newUser.get().getRole()));
            return userDto;
        }
        return null;
    }

    @Override
    public String getName() {
        return "SignInService";
    }
}

package ru.javalab.socketsapp.repository.repository.User;

import ru.javalab.socketsapp.models.User;
import ru.javalab.socketsapp.repository.repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    void save(User user);

    Optional<User> find(String name) throws SQLException;
}

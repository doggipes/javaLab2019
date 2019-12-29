package ru.javalab.repository.User;


import ru.javalab.models.User;
import ru.javalab.repository.CrudRepository;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    void save(User user);

    Optional<User> find(String name) throws SQLException;
}

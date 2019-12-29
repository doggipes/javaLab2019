package ru.javalab.socketsapp.repository.repository.Message;

import ru.javalab.socketsapp.models.Message;
import ru.javalab.socketsapp.repository.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    void save(Message message);
}

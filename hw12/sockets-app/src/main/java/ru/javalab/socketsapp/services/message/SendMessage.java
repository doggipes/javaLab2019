package ru.javalab.socketsapp.services.message;

import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.models.Message;

public interface SendMessage {
    Dto save(Message message);
}

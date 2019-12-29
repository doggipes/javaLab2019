package ru.javalab.socketsapp.dispatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.socketsapp.Dto.Dto;
import ru.javalab.socketsapp.protocol.LoginRequest;
import ru.javalab.socketsapp.protocol.MessageRequest;
import ru.javalab.socketsapp.protocol.Request;
import ru.javalab.socketsapp.services.signIn.SignInService;
import ru.javalab.socketsapp.services.signIn.SignInServiceImpl;

import java.sql.SQLException;

public class RequestDispatcher {
    private SignInService signInService;

    public RequestDispatcher() {
        signInService = new SignInServiceImpl();
    }

    public Dto doDispatch(Request request) {
        if (request.getHeader().equals("Login")) {


            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest requestLogin = ((Request<LoginRequest>) request).getPayload();

            try {
                return signInService.signIn(requestLogin.getLogin(), requestLogin.getPassword());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

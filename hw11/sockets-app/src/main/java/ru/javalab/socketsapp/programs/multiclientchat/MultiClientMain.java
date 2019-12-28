package ru.javalab.socketsapp.programs.multiclientchat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.socketsapp.clients.ChatClient;
import ru.javalab.socketsapp.models.*;

public class MultiClientMain {
    public static void main(String[] args) {
        int port = 7000;
        String ip = "127.0.0.1";

        for (String param : args) {
            String setting = param.split("=")[0];
            String settingValue = param.split("=")[1];
            if(setting.equals("--server-ip"))
                ip = settingValue;
            if(setting.equals("--server-port"))
                port = Integer.parseInt(settingValue);
        }

        //login admin password admin have role admin
        //other users have default user role
        ChatClient chatClient = new ChatClient();
        chatClient.startConnection(ip, port);
    }
}

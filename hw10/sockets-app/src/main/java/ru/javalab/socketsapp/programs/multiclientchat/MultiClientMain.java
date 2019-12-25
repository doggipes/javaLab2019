package ru.javalab.socketsapp.programs.multiclientchat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.socketsapp.clients.ChatClient;
import ru.javalab.socketsapp.models.*;

import java.util.Scanner;

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

        ChatClient chatClient = new ChatClient();
        chatClient.startConnection(ip, port);

        Controller(chatClient);
    }

    public static void Controller(ChatClient chatClient){
        Scanner in = new Scanner(System.in);

        System.out.println("Login: ");
        String login = in.nextLine();

        System.out.println("Password: ");
        String password = in.nextLine();

        Request<LoginRequest> auth = new Request<>("Login", new LoginRequest(login, password));


        chatClient.sendMessage(toJson(auth));

        while (true) {
            String message = in.nextLine();

            if(message.contains("/command")){
                if(message.contains("get-messages")){
                    int page;
                    page = Integer.parseInt(message.substring(message.length() - 1));
                    Pagination pagination = new Pagination("get-messages", page);
                    Request<Pagination> request = new Request<>("Command", pagination);

                    chatClient.sendMessage(toJson(request));
                }
            } else if(message.equals("/logout")){
                Request<LogoutRequest> request = new Request<>("Logout", null);
                chatClient.sendMessage(toJson(request));
                return;
            } else {
                MessageRequest messageRequest = new MessageRequest(message);
                Request<MessageRequest> request = new Request<>("Message",messageRequest);

                chatClient.sendMessage(toJson(request));
            }
        }
    }

    private static String toJson(Object object){
        String json = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}

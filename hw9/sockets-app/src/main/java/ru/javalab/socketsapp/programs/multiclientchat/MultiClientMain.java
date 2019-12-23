package ru.javalab.socketsapp.programs.multiclientchat;

import ru.javalab.socketsapp.clients.ChatClient;

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
        Scanner in = new Scanner(System.in);

        System.out.println("Login: ");
        String login = in.nextLine();

        System.out.println("Password: ");
        String password = in.nextLine();

        chatClient.loginUser(login, password);

        while (true) {
            String message = in.nextLine();
            chatClient.sendMessage(message);
        }
    }
}

package ru.javalab.socketsapp.servers;

import ru.javalab.socketsapp.db.models.Message;
import ru.javalab.socketsapp.db.models.User;
import ru.javalab.socketsapp.db.repository.messageRepository;
import ru.javalab.socketsapp.db.repository.userRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MultiClientServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public MultiClientServer(){
        clients = new ArrayList<ClientHandler>();
    }

    private boolean checkLogin(ClientHandler user){
        return clients.contains(user);
    }

    public void start(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        for(;;){
            try {
                ClientHandler handler = new ClientHandler(serverSocket.accept());
                handler.start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private class ClientHandler extends Thread{
        private Socket clientSocket;
        private BufferedReader reader;
        private int userId;
        private String username;

        public ClientHandler(Socket clientSocket){
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            System.out.println("in run");
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;

                while((line = reader.readLine()) != null){
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                    if(!checkLogin(this)){
                        String name = line.split(" ")[0];
                        String password = line.split(" ")[1];
                        userRepository userRepository = new userRepository(connectionToDB.getInstance());
                        User newUser = userRepository.find(name);
                        if(newUser.getUsername() != null){
                            if(password.equals(newUser.getPassword())){
                                userId = newUser.getID();
                                username = newUser.getUsername();

                                clients.add(this);
                                System.out.println(username + " connected to chat");
                            }
                            else
                                System.out.println("Password is invalid");
                        }
                        else{
                            User user = new User();
                            user.setUsername(name);
                            user.setPassword(password);
                            userRepository.save(user);

                            userId = userRepository.find(name).getID();
                            username = user.getUsername();

                            clients.add(this);
                            System.out.println(username + " connected to chat");
                        }
                    }
                    else{
                        for(ClientHandler client : clients){
                            writer = new PrintWriter(client.clientSocket.getOutputStream(), true);
                            writer.println(username + ": " + line);
                        }
                    }

                    messageRepository messageRepository = new messageRepository(connectionToDB.getInstance());
                    Message message = new Message();
                    message.setUserID(userId);
                    message.setMessage(line);
                    messageRepository.save(message);
                }
                reader.close();
                clientSocket.close();
            } catch (IOException | SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}

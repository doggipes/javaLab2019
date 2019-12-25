package ru.javalab.socketsapp.servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.socketsapp.db.models.Message;
import ru.javalab.socketsapp.db.models.User;
import ru.javalab.socketsapp.db.repository.messageRepository;
import ru.javalab.socketsapp.db.repository.userRepository;
import ru.javalab.socketsapp.models.*;

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

                    ObjectMapper mapper = new ObjectMapper();
                    Request request = mapper.readValue(line, Request.class);
                    if(request.getHeader().equals("Login")){
                        Request<LoginRequest> loginRequest = mapper.readValue(line, new TypeReference<Request<LoginRequest>>() {
                        });
                        String name = loginRequest.getPayload().getLogin();
                        String password = loginRequest.getPayload().getPassword();
                        userRepository userRepository = new userRepository(connectionToDB.getInstance());
                        User newUser = userRepository.find(name);
                        if(!checkLogin(this)) {
                            if (newUser.getUsername() != null) {
                                PasswordEncoder encoder = new BCryptPasswordEncoder();

                                if (encoder.matches(password, newUser.getPassword())) {
                                    userId = newUser.getID();
                                    username = newUser.getUsername();

                                    clients.add(this);
                                    System.out.println(username + " connected to chat");
                                } else
                                    System.out.println("Password is invalid");
                            } else {
                                PasswordEncoder encoder = new BCryptPasswordEncoder();
                                User user = new User();
                                user.setUsername(name);
                                user.setPassword(encoder.encode(password));
                                userRepository.save(user);

                                userId = userRepository.find(name).getID();
                                username = user.getUsername();

                                clients.add(this);
                                System.out.println(username + " connected to chat");
                            }
                        }
                    } else
                        if(request.getHeader().equals("Message")){
                            Request<MessageRequest> messageRequest = mapper.readValue(line, new TypeReference<Request<MessageRequest>>() {
                            });
                            for(ClientHandler client : clients){
                                writer = new PrintWriter(client.clientSocket.getOutputStream(), true);
                                writer.println(username + ": " + messageRequest.getPayload().getMessage());
                            }
                            messageRepository messageRepository = new messageRepository(connectionToDB.getInstance());
                            Message message = new Message();
                            message.setUserID(userId);
                            message.setMessage(messageRequest.getPayload().getMessage());
                            messageRepository.save(message);
                        }
                        else
                            if(request.getHeader().equals("Command")){
                                Request<Pagination> paginationRequest = mapper.readValue(line, new TypeReference<Request<Pagination>>() {
                                });
                                if(paginationRequest.getPayload().getCommand().equals("get-messages")) {
                                    messageRepository messageRepository = new messageRepository(connectionToDB.getInstance());
                                    List<Message> list = messageRepository.findByPage(paginationRequest.getPayload().getPage());
                                    if (!list.isEmpty()) {
                                        writer.println(toJson(new PaginationClient(list)));
                                    } else {
                                        writer.println("Messages list is empty");
                                    }
                                }
                            }
                }
                reader.close();
                clientSocket.close();
            } catch (IOException | SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        private String toJson(Object object){
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
}

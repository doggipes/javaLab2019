package ru.javalab.socketsapp.servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.javalab.socketsapp.db.models.Favorites;
import ru.javalab.socketsapp.db.models.Message;
import ru.javalab.socketsapp.db.models.Product;
import ru.javalab.socketsapp.db.models.User;
import ru.javalab.socketsapp.db.repository.FavoritesRepository;
import ru.javalab.socketsapp.db.repository.ProductRepository;
import ru.javalab.socketsapp.db.repository.messageRepository;
import ru.javalab.socketsapp.db.repository.userRepository;
import ru.javalab.socketsapp.jwt.jwtToken;
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

                while((line = reader.readLine()) != null) {
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                    ObjectMapper mapper = new ObjectMapper();
                    Request request = mapper.readValue(line, Request.class);
                    switch (request.getHeader()) {
                        case "Login":
                            Request<LoginRequest> loginRequest = mapper.readValue(line, new TypeReference<Request<LoginRequest>>() {
                            });
                            String name = loginRequest.getPayload().getLogin();
                            String password = loginRequest.getPayload().getPassword();
                            userRepository userRepository = new userRepository(connectionToDB.getInstance());
                            User newUser = userRepository.find(name);
                            JwtResponse jwtResponse = new JwtResponse();
                            if (!checkLogin(this)) {
                                if (newUser.getUsername() != null) {
                                    PasswordEncoder encoder = new BCryptPasswordEncoder();

                                    if (encoder.matches(password, newUser.getPassword())) {
                                        userId = newUser.getID();
                                        username = newUser.getUsername();

                                        clients.add(this);
                                        System.out.println(username + " connected to chat");
                                        jwtResponse = new JwtResponse(jwtToken.createToken(String.valueOf(userId), newUser.getRole()));
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
                                    jwtResponse = new JwtResponse(jwtToken.createToken(String.valueOf(userId), newUser.getRole()));
                                }
                            }
                            Response<JwtResponse> response = new Response<>("Token", jwtResponse);
                            writer.println(toJson(response));
                            break;
                        case "Message": {
                            Request<MessageRequest> messageRequest = mapper.readValue(line, new TypeReference<Request<MessageRequest>>() {
                            });
                            messageRepository messageRepository = new messageRepository(connectionToDB.getInstance());
                            Message message = new Message();
                            message.setUserID(userId);
                            message.setMessage(messageRequest.getPayload().getMessage());
                            messageRepository.save(message);
                            Response<MessageRequest> messageRequestResponse = new Response<>("Message", messageRequest.getPayload());
                            for (ClientHandler client : clients) {
                                writer = new PrintWriter(client.clientSocket.getOutputStream(), true);
                                messageRequestResponse.getPayload().setMessage(username + ": " + messageRequestResponse.getPayload().getMessage());
                                writer.println(toJson(messageRequestResponse));
                            }
                            break;
                        }
                        case "Command":
                            Request<Pagination> paginationRequest = mapper.readValue(line, new TypeReference<Request<Pagination>>() {
                            });
                            if (paginationRequest.getPayload().getCommand().equals("get-messages")) {
                                messageRepository messageRepository = new messageRepository(connectionToDB.getInstance());
                                List<Message> list = messageRepository.findByPage(paginationRequest.getPayload().getPage());
                                if (!list.isEmpty()) {
                                    writer.println(toJson(new PaginationClient(list)));
                                } else {
                                    writer.println("Messages list is empty");
                                }
                            }
                            break;
                        case "Product Add": {
                            MessageRequest messageRequest = new MessageRequest();
                            Request<AddProductRequest> addProductRequestRequest = mapper.readValue(line, new TypeReference<Request<AddProductRequest>>() {
                            });
                            if (jwtToken.checkRole(addProductRequestRequest.getPayload().getToken())) {
                                ProductRepository productRepository = new ProductRepository(connectionToDB.getInstance());
                                productRepository.save(new Product(addProductRequestRequest.getPayload().getName(), addProductRequestRequest.getPayload().getPrice()));
                                messageRequest.setMessage("Successfully added");
                            } else {
                                messageRequest.setMessage("You don't have permission to access");
                            }
                            Response<MessageRequest> messageRequestResponse = new Response<>("Message", messageRequest);
                            writer.println(toJson(messageRequestResponse));
                            break;
                        }
                        case "Product All":
                            ProductRepository productRepository = new ProductRepository(connectionToDB.getInstance());
                            List<Product> list = productRepository.find();
                            writer.println(toJson(new Response<>("Products", list)));
                            break;
                        case "Buy product":
                            Request<AddFavoritesRequest> favoritesRequestRequest = mapper.readValue(line, new TypeReference<Request<AddFavoritesRequest>>() {
                            });
                            FavoritesRepository favoritesRepository = new FavoritesRepository(connectionToDB.getInstance());
                            favoritesRepository.save(new Favorites(jwtToken.getIdFromJwt(favoritesRequestRequest.getPayload().getToken()), favoritesRequestRequest.getPayload().getId()));
                            writer.println(toJson(new Response<>("Message", new MessageRequest("Successfully added"))));
                            break;
                        case "Delete Product":
                            Request<DeleteProductRequest> deleteProductRequest = mapper.readValue(line, new TypeReference<Request<DeleteProductRequest>>(){});
                            MessageRequest messageRequest = new MessageRequest();
                            if(jwtToken.checkRole(deleteProductRequest.getPayload().getToken())) {
                                ProductRepository productRepository1 = new ProductRepository(connectionToDB.getInstance());
                                productRepository1.delete(deleteProductRequest.getPayload().getId());
                                messageRequest.setMessage("Successfully deleted");
                            }
                            else
                                messageRequest.setMessage("You don't have permission to access");
                            Response<MessageRequest> messageRequestResponse = new Response<>("Message", messageRequest);
                            writer.println(toJson(messageRequestResponse));
                            break;
                        case "Logout":
                            clients.remove(this);
                            break;
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

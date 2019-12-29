package ru.javalab.socketsapp.servers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.socketsapp.Dto.MessageDto;
import ru.javalab.socketsapp.Dto.ProductDto;
import ru.javalab.socketsapp.Dto.UserDto;
import ru.javalab.socketsapp.context.ApplicationContext;
import ru.javalab.socketsapp.context.ApplicationContextReflectionBased;
import ru.javalab.socketsapp.dispatcher.RequestDispatcher;
import ru.javalab.socketsapp.models.Favorites;
import ru.javalab.socketsapp.models.Message;
import ru.javalab.socketsapp.models.Product;
import ru.javalab.socketsapp.repository.repository.Product.ProductRepositoryImpl;
import ru.javalab.socketsapp.repository.repository.Message.MessageRepositoryImpl;
import ru.javalab.socketsapp.services.favorites.AddToFavorites;
import ru.javalab.socketsapp.services.favorites.AddToFavoritesImpl;
import ru.javalab.socketsapp.services.message.SendMessage;
import ru.javalab.socketsapp.services.message.SendMessageImpl;
import ru.javalab.socketsapp.services.product.AddProduct;
import ru.javalab.socketsapp.services.product.AddProductImpl;
import ru.javalab.socketsapp.services.product.DeleteProduct;
import ru.javalab.socketsapp.services.product.DeleteProductImpl;
import ru.javalab.socketsapp.util.jwtToken;
import ru.javalab.socketsapp.protocol.*;
import ru.javalab.socketsapp.util.connectionToDB;

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
        private ApplicationContext applicationContext;
        private Socket clientSocket;
        private BufferedReader reader;
        private int userId;
        private String username;
        private RequestDispatcher requestDispatcher;

        public ClientHandler(Socket clientSocket){
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            System.out.println("in run");

            applicationContext = new ApplicationContextReflectionBased();
            requestDispatcher = new RequestDispatcher();
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
                            UserDto currUser = (UserDto) requestDispatcher.doDispatch(loginRequest);
                            JwtResponse jwtResponse = new JwtResponse(currUser.getToken());
                            Response<JwtResponse> response = new Response<>("Token", jwtResponse);
                            username = currUser.getLogin();
                            userId = currUser.getId();
                            clients.add(this);
                            writer.println(toJson(response));
                            break;
                        case "Message": {
                            Request<MessageRequest> messageRequest = mapper.readValue(line, new TypeReference<Request<MessageRequest>>() {
                            });

                            SendMessage sendMessage = applicationContext.getComponent(SendMessageImpl.class, "SendMessage");
                            sendMessage.save(new Message(messageRequest.getPayload().getMessage(), userId));

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
                                MessageRepositoryImpl messageRepositoryImpl = new MessageRepositoryImpl();
                                List<MessageDto> list = messageRepositoryImpl.findByPage(paginationRequest.getPayload().getPage());
                                if (!list.isEmpty()) {
                                    writer.println(toJson(new PaginationClient(list)));
                                } else {
                                    writer.println("Messages list is empty");
                                }
                            }
                            break;
                        case "product Add": {
                            MessageRequest messageRequest = new MessageRequest();
                            Request<AddProductRequest> addProductRequestRequest = mapper.readValue(line, new TypeReference<Request<AddProductRequest>>() {
                            });
                            if (jwtToken.checkRole(addProductRequestRequest.getPayload().getToken())) {

                                AddProduct addProduct = applicationContext.getComponent(AddProductImpl.class, "AddProduct");
                                addProduct.save(new Product(addProductRequestRequest.getPayload().getName(), addProductRequestRequest.getPayload().getPrice()));

                                messageRequest.setMessage("Successfully added");
                            } else {
                                messageRequest.setMessage("You don't have permission to access");
                            }
                            Response<MessageRequest> messageRequestResponse = new Response<>("Message", messageRequest);
                            writer.println(toJson(messageRequestResponse));
                            break;
                        }
                        case "product All":
                            ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl(connectionToDB.getInstance());
                            List<ProductDto> list = productRepositoryImpl.find();
                            writer.println(toJson(new Response<>("Products", list)));
                            break;
                        case "Buy product":
                            Request<AddFavoritesRequest> favoritesRequestRequest = mapper.readValue(line, new TypeReference<Request<AddFavoritesRequest>>() {
                            });

                            AddToFavorites addToFavorites = applicationContext.getComponent(AddToFavoritesImpl.class, "AddToFavorites");
                            addToFavorites.addToFavorites(new Favorites(userId, favoritesRequestRequest.getPayload().getId()));

                            writer.println(toJson(new Response<>("Message", new MessageRequest("Successfully added"))));
                            break;
                        case "Delete product":
                            Request<DeleteProductRequest> deleteProductRequest = mapper.readValue(line, new TypeReference<Request<DeleteProductRequest>>(){});
                            MessageRequest messageRequest = new MessageRequest();
                            if(jwtToken.checkRole(deleteProductRequest.getPayload().getToken())) {

                                DeleteProduct deleteProduct = applicationContext.getComponent(DeleteProductImpl.class, "DeleteProduct");
                                deleteProduct.delete(new Product(deleteProductRequest.getPayload().getId()));

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

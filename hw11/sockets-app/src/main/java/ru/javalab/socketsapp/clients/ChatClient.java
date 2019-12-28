package ru.javalab.socketsapp.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.socketsapp.db.models.Product;
import ru.javalab.socketsapp.jwt.jwtToken;
import ru.javalab.socketsapp.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class ChatClient {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String token;


    public void startConnection(String ip, int port){
        try {
            clientSocket = new Socket(ip,port);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        doLogin();
        new Thread(() ->{
            while(true){
                try {
                    String line = reader.readLine();
                    ObjectMapper mapper = new ObjectMapper();
                    Response response = mapper.readValue(line, Response.class);
                    if(response.getHeader().equals("Token")){
                        Response<JwtResponse> jwtResponse = mapper.readValue(line, new TypeReference<Response<JwtResponse>>(){});
                        if(jwtResponse.getPayload().getToken() != null){
                            token = jwtResponse.getPayload().getToken();
                            System.out.println("Welcome to dopka");
                            Controller(this);
                        }else {
                            System.out.println("Welcome to comissiya");
                            doLogin();
                        }
                    }else
                    if(response.getHeader().equals("Message")){
                        Response<MessageRequest> messageRequestResponse = mapper.readValue(line, new TypeReference<Response<MessageRequest>>(){});
                        System.out.println(messageRequestResponse.getPayload().getMessage());
                    } else
                    if(response.getHeader().equals("Products")){
                        Response<List<Product>> listResponse = mapper.readValue(line, new TypeReference<Response<List<Product>>>(){});
                        listResponse.getPayload().stream().forEach(product ->
                        System.out.println(product.getId() + " " + product.getName() + " " + product.getPrice()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}
        ).start();
    }

    public void Controller(ChatClient chatClient){
        Scanner in = new Scanner(System.in);
        new Thread(() -> {while (true) {
            String message = in.nextLine();

            if(message.contains("/command")){
                if(message.contains("get-messages")){
                    int page;
                    page = Integer.parseInt(message.substring(message.length() - 1));
                    Pagination pagination = new Pagination("get-messages", page);
                    Request<Pagination> request = new Request<>("Command", pagination);

                    sendMessage(toJson(request));
                } else
                if(message.contains("add-to-favorites")){
                     System.out.println("ID: ");
                     int id = in.nextInt();
                     Request<AddFavoritesRequest> favoritesRequestRequest = new Request<>("Buy product", new AddFavoritesRequest(id, token));
                     sendMessage(toJson(favoritesRequestRequest));
                } else
                if(message.contains("add-product")){
                     System.out.println("Name: ");
                     String name = in.nextLine();
                     System.out.println("Price: ");
                     String price = in.nextLine();
                     Request<AddProductRequest> addProductRequestRequest = new Request<>("Product Add", new AddProductRequest(name, price, token));
                     sendMessage(toJson(addProductRequestRequest));
                } else
                if(message.contains("all-products")){
                     Request<MessageRequest> messageRequestRequest = new Request<>("Product All", null);
                     sendMessage(toJson(messageRequestRequest));
                } else
                if(message.contains("delete-product")){
                    System.out.println("ID: ");
                    int id = in.nextInt();
                    Request<DeleteProductRequest> deleteProductRequest = new Request<>("Delete Product", new DeleteProductRequest(id, token));
                    sendMessage(toJson(deleteProductRequest));
                }
            } else
            if(message.equals("/logout")){
                Request<LogoutRequest> request = new Request<>("Logout", null);
                sendMessage(toJson(request));
                token = "";
                doLogin();
                return;
            } else {
                MessageRequest messageRequest = new MessageRequest(message);
                Request<MessageRequest> request = new Request<>("Message", messageRequest);

                sendMessage(toJson(request));
            }
        }}).start();
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

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void doLogin(){
        Scanner in = new Scanner(System.in);

        System.out.println("Login: ");
        String login = in.nextLine();

        System.out.println("Password: ");
        String password = in.nextLine();

        Request<LoginRequest> auth = new Request<>("Login", new LoginRequest(login, password));


        sendMessage(toJson(auth));
    }

    public void loginUser(String name, String password){
        writer.println(name + " " + password);
    }

//    private Runnable receiveMessagesTask = new Runnable() {
//        public void run() {
//            while (true) {
//                try {
//                    String message = reader.readLine();
//                    System.out.println(message);
//                } catch (IOException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        }
//    };


}

package ru.javalab.socketsapp.servers;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class connectionToDB {
    private static connectionToDB instance;
    private Connection connection;
    private static String url;
    private static String username;
    private static String password;

    private connectionToDB() throws SQLException{
        Properties properties = new Properties();
        properties.setProperty("user",username);
        properties.setProperty("password",password);

        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        try {
            this.connection = DriverManager.getConnection(url, properties);
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    public static Connection createConnection(String path){
        try {
            propertyParser propertyParser = new propertyParser(path);

            Map<String,String> map = propertyParser.propertyParse();
            username = map.get("user");
            password = map.get("password");
            url = map.get("url");

            return getInstance();
        }  catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getInstance() throws SQLException {
        if(instance == null){
            try {
                instance = new connectionToDB();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        try {
            return DriverManager.getConnection(instance.url, instance.username, instance.password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
}

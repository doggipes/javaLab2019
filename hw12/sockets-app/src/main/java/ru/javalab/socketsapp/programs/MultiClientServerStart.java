package ru.javalab.socketsapp.programs;

import ru.javalab.socketsapp.servers.MultiClientServer;
import ru.javalab.socketsapp.util.connectionToDB;

public class MultiClientServerStart {
    public static void main(String[] args) {
        int port = 7000;
        String path = "db.properties";

        for (String param : args) {
            String setting = param.split("=")[0];
            String settingValue = param.split("=")[1];
            if(setting.equals("--db-properties-path"))
                path = settingValue;
            if(setting.equals("--port"))
                port = Integer.parseInt(settingValue);
        }

        connectionToDB.createConnection(path);

        MultiClientServer multiClientServer = new MultiClientServer();
        multiClientServer.start(port);
    }
}

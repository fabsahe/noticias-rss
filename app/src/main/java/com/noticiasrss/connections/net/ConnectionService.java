package com.noticiasrss.connections.net;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ConnectionService {

    public static HttpsURLConnection getConnection(String path) throws IOException {
        URL url = new URL(path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(10000);
        connection.connect();

        return connection;
    }
}

package web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ConnectionToAPI {

    String userAgent;
    String method;
    String MIME;
    String cookie;
    String contentType;

    public ConnectionToAPI(String method, HashMap<String,String> property) {
        this.method = method;
        this.userAgent = property.get("userAgent");
        this.MIME = property.get("MIME");
        this.cookie = property.get("cookie");
        this.contentType = property.get("contentType");
    }

    public HttpURLConnection getConnection(URL url){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", MIME);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Cookie", cookie);
        } catch (IOException exception ) {
            exception.printStackTrace();
        }
        return  connection;
    }
//    public static String
}

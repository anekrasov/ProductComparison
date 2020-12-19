package ru.auchan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Auchan {


    public String getHttpRequest(String payload){
        StringBuilder jsonString = new StringBuilder();
        String userAgent = "YandexMobileBot";
        try {
            URL apiurl = new URL("https://www.auchan.ru/v1/");
            HttpURLConnection connection = (HttpURLConnection) apiurl.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Cookie", "_GASHOP=069_Barnaul_Volna; merchantID_=69; region_id=25");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(jsonString);
    }

}

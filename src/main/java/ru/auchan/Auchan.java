package ru.auchan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.Database;
import org.sqlite.SQLiteException;
import web.UserAgent;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;

public class Auchan {

    static String url = "https://www.auchan.ru/v1/categories/?merchant_id=65";


    public static String getHttpResponse(String url){
        StringBuilder jsonString = new StringBuilder();
        String userAgent = UserAgent.getRandomUserAgent();
        try {
            URL apiurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiurl.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Cookie", "_GASHOP=069_Barnaul_Volna; merchantID_=69; region_id=25");
            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(connection.getInputStream());
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Auchan Thread sleep 60 sec");
                Thread.sleep(60000);
                System.out.println("resend");
                inputStreamReader = new InputStreamReader(connection.getInputStream());
            }
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(jsonString);
    }

    public static HashMap<String,String> generateUrl(){
        HashMap<String,String> category;
        JsonArray jsonArray = new Gson().fromJson(getHttpResponse(url),JsonArray.class);
        category = getItems(jsonArray);
        return category;
    }
    public static HashMap<String,String> getItems(JsonArray jsonArray){
        HashMap <String,String> result = new HashMap<>();
        String nameCategory = null;
        String codeCatecory;
        String productsCountcodeCategory;
        String url;
        for (JsonElement items: jsonArray) {
            JsonArray o2 = items.getAsJsonObject().getAsJsonArray("items");
            for (JsonElement items2: o2) {
                try {
                    JsonArray o3 = items2.getAsJsonObject().getAsJsonArray("items");
                    for (JsonElement items3: o3) {
                        nameCategory = items3.getAsJsonObject().get("name").toString().replace("\"","");
                        codeCatecory = items3.getAsJsonObject().get("code").toString().replace("\"","");
                        productsCountcodeCategory = items3.getAsJsonObject().get("productsCount").toString().replace("\"","");
                        url ="https://www.auchan.ru/v1/catalog/products?merchantId=65&filter[category]="
                                +codeCatecory+"&page=1&perPage="+productsCountcodeCategory+"&orderField=rank&orderDirection=asc";
                        result.put(nameCategory,url);
                    }
                }catch (Exception ex){
                    System.out.println("нет категории (item3)" + nameCategory);
                }
                nameCategory = items2.getAsJsonObject().get("name").toString().replace("\"","");
                codeCatecory = items2.getAsJsonObject().get("code").toString().replace("\"","");
                productsCountcodeCategory = items2.getAsJsonObject().get("productsCount").toString().replace("\"","");
                url ="https://www.auchan.ru/v1/catalog/products?merchantId=65&filter[category]="
                        +codeCatecory+"&page=1&perPage="+productsCountcodeCategory+"&orderField=rank&orderDirection=asc";
                result.put(nameCategory,url);
            }
            nameCategory = items.getAsJsonObject().get("name").toString().replace("\"","");
            codeCatecory = items.getAsJsonObject().get("code").toString().replace("\"","");
            productsCountcodeCategory = items.getAsJsonObject().get("productsCount").toString().replace("\"","");
            url ="https://www.auchan.ru/v1/catalog/products?merchantId=65&filter[category]="
                    +codeCatecory+"&page=1&perPage="+productsCountcodeCategory+"&orderField=rank&orderDirection=asc";
            result.put(nameCategory,url);
        }
        return result;
    }

    public HashMap<String,String> getProduct(String url){
        HashMap<String, String> product = new HashMap<>();
        String response = getHttpResponse(url);
        JsonElement items = new Gson().fromJson(response, JsonObject.class).get("items");
        for (JsonElement i: items.getAsJsonArray()) {
            String title = i.getAsJsonObject().get("title").toString();
            String priceValue = i.getAsJsonObject().get("price").getAsJsonObject().get("value").toString();
            String priceCurrency = i.getAsJsonObject().get("price").getAsJsonObject().get("currency").toString();
            product.put(title,priceValue+priceCurrency);
            }
        return product;
    }

    public void toDatabase() throws SQLException {
        String cat;
        HashMap<String,String> mapAuchan = generateUrl();
        Database database = new Database();
        Connection connection = database.getConn();
        Statement statement = connection.createStatement();
        PreparedStatement psCategory = connection.prepareStatement("INSERT INTO 'auchan_category' ('name') VALUES (?);");
        PreparedStatement psProduct = connection.prepareStatement("INSERT INTO auchan_product (name,price,category) VALUES (?, ?, ?);");
        statement.execute("DELETE FROM auchan_category;");
        statement.execute("DELETE FROM auchan_product;");
        Database.commit(connection);
        for (String category: mapAuchan.keySet()) {
            cat = category;
            try {
                psCategory.setString(1,category);
                psCategory.addBatch();
                HashMap<String,String> product = getProduct(mapAuchan.get(cat));
                    for (String p: product.keySet()) {
                        String name = p.replace("'","").toLowerCase();
                        String price = product.get(p).replace("'","").replace("\"RUB\"","");
                        psProduct.setString(1,name);
                        psProduct.setString(2,price);
                        psProduct.setString(3,cat);
                        psProduct.addBatch();
                    }
            } catch (SQLException throwables) {
                connection.close();
                throwables.printStackTrace();
            }
        }
        psCategory.executeBatch();
        psProduct.executeBatch();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        statement.execute("UPDATE auchan_status SET lastDateUpdate="+"\""+timestamp+"\";");
        Database.commit(connection);
        System.out.println("auchan filling complete");
    }
}

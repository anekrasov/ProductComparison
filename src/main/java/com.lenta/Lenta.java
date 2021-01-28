package com.lenta;

import com.github.hemantsonu20.json.JsonMerge;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.Database;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import web.UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Lenta {
    Document doc = null;
    String masthead = null;
    JsonObject jsonObject = null;


    public JsonObject getJsonDataMenu() {
        try {
            doc = Jsoup.connect("https://lenta.com/catalog").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            masthead = doc.select("div.header__catalog-menu-container").attr("data-menu");
            jsonObject = new Gson().fromJson(masthead, JsonObject.class);
        }
        return jsonObject;
    }

    public JsonArray getCategoryProduct(JsonObject jsonObject){
        return jsonObject.getAsJsonArray("groups");
    }

    public JsonObject getProduct(String code){
        Gson gson = new Gson();
        JsonObject jsonObject;
        JsonObject temp = null;
        String totalCount;
        String offset = "0";
        String payload = "{\"nodeCode\":"+code+",\"filters\":[],\"tag\":\"\",\"typeSearch\":1,\"sortingType\":\"ByPriority\",\"offset\":"+offset+",\"limit\":24,\"updateFilters\":true}";
        jsonObject = new Gson().fromJson(getHttpRequest(payload),JsonObject.class);
        totalCount = jsonObject.get("totalCount").toString();
        if(totalCount!=null){
            int total = Integer.parseInt(totalCount);
            if(total>=1000){
                int count = 0;
                while (total>=1000){
                    totalCount = "1000";
                    String payload2 = "{\"nodeCode\":"+code+",\"filters\":[],\"tag\":\"\",\"typeSearch\":1,\"sortingType\":\"ByPriority\",\"offset\":"+offset+",\"limit\":"+totalCount+",\"updateFilters\":true}";
                    if(temp==null){
                        temp = gson.fromJson(getHttpRequest(payload2),JsonObject.class);
                        }
                    else {
                        JsonObject temp2 = gson.fromJson(getHttpRequest(payload2),JsonObject.class);
                        String outputMerge = JsonMerge.merge(temp.toString(), temp2.toString());
                        temp = gson.fromJson(outputMerge,JsonObject.class);
                        }
                    count++;
                    total = total-1000;
                    offset = String.valueOf(Integer.parseInt(totalCount)*count);
                    }
                String payload2 = "{\"nodeCode\":"+code+",\"filters\":[],\"tag\":\"\",\"typeSearch\":1,\"sortingType\":\"ByPriority\",\"offset\":"+offset+",\"limit\":"+totalCount+",\"updateFilters\":true}";
                JsonObject temp3 = gson.fromJson(getHttpRequest(payload2),JsonObject.class);
                String outputMerge = JsonMerge.merge(temp.toString(), temp3.toString());
                jsonObject = gson.fromJson(outputMerge,JsonObject.class);
                }
            else {
                String payload2 = "{\"nodeCode\":"+code+",\"filters\":[],\"tag\":\"\",\"typeSearch\":1,\"sortingType\":\"ByPriority\",\"offset\":0,\"limit\":"+totalCount+",\"updateFilters\":true}";
                jsonObject = gson.fromJson(getHttpRequest(payload2),JsonObject.class);
                }
            }
        else {
            System.out.println("Нет значения TotalCount");
                }
        return jsonObject;
        }

    public String getHttpRequest(String payload){
        StringBuilder jsonString = new StringBuilder();
        try {
            URL apiurl = new URL("https://lenta.com/api/v1/skus/list");
            HttpURLConnection connection = (HttpURLConnection) apiurl.openConnection();
            connection.setRequestProperty("User-Agent", UserAgent.getRandomUserAgent());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Cookie", "lentaT2=brn; CityCookie=brn; ReviewedSkus=275922,291465,112117,275920;Stora=0083");
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

    public void toDatabase() throws SQLException {
        Database database = new Database();
        Connection connection = database.getConn();
        Statement statement = connection.createStatement();
        String name;
        String code;
        JsonObject dataMenu = getJsonDataMenu();
        JsonArray allCategoryProduct = getCategoryProduct(dataMenu);
        PreparedStatement psCategory = connection.prepareStatement("INSERT INTO 'lenta_category' ('name', 'code') VALUES (?,?);");
        PreparedStatement psProduct = connection.prepareStatement("INSERT INTO 'lenta_product' ('name', 'price','price_card','category','sub_name') VALUES (?,?,?,?,?);");
        statement.execute("DELETE FROM lenta_category;");
        statement.execute("DELETE FROM lenta_product;");
        Database.commit(connection);

        for (JsonElement o: allCategoryProduct) {
            name = o.getAsJsonObject().get("name").toString();
            code = o.getAsJsonObject().get("code").toString();
            psCategory.setString(1,name);
            psCategory.setString(2,code);
            psCategory.addBatch();
            JsonObject product = getProduct(code);
            for (JsonElement p: product.getAsJsonArray("skus") ) {
                String title = p.getAsJsonObject().get("title").toString().replace("'","").toLowerCase();
                String regularPrice = p.getAsJsonObject().get("regularPrice").getAsJsonObject().get("value").toString();
                String cardPrice = p.getAsJsonObject().get("cardPrice").getAsJsonObject().get("value").toString();
                String subTitle = p.getAsJsonObject().get("subTitle").toString().toLowerCase();
                psProduct.setString(1,title);
                psProduct.setString(2,regularPrice);
                psProduct.setString(3,cardPrice);
                psProduct.setString(4,name);
                psProduct.setString(5,subTitle);
                psProduct.addBatch();
            }
        }
        psCategory.executeBatch();
        psProduct.executeBatch();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        statement.execute("UPDATE lenta_status SET lastDateUpdate="+"\""+timestamp+"\";");
        Database.commit(connection);
        System.out.println("lenta filling complate");
    }

}

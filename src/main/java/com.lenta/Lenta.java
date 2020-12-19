package com.lenta;

import com.github.hemantsonu20.json.JsonMerge;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

//    public String getCode(String name,JsonObject jsonObject){
//        JsonArray jsonArray = getCategoryProduct(jsonObject);
//        for (JsonElement o: jsonArray) {
//            String nameProduct = o.getAsJsonObject().get("name").toString().replace("\"","");
//            String code  = o.getAsJsonObject().get("code").toString();
//            if(nameProduct.equals(name)) return code ;
//        }
//        return "нет товарной группы";
//    }

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
            System.out.println("всего: "+total);
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
        String userAgent = "YandexMobileBot";
        try {
            URL apiurl = new URL("https://lenta.com/api/v1/skus/list");
            HttpURLConnection connection = (HttpURLConnection) apiurl.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
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

    public void toDatabase(){

    }

}

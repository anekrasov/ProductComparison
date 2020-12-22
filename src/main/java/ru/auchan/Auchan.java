package ru.auchan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.Database;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Auchan {

    String auchansiteCategoryes = "https://www.auchan.ru/v1/categories/?merchant_id=65";


    public String getHttpResponse(String url){
        StringBuilder jsonString = new StringBuilder();
        String userAgent = "YandexMobileBot";
        try {
            URL apiurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiurl.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Cookie", "_GASHOP=069_Barnaul_Volna; merchantID_=69; region_id=25");
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

    public HashMap<String,String> generateUrl(){
        HashMap<String,String> category = new HashMap<>();
        JsonArray jsonArray = new Gson().fromJson(getHttpResponse(auchansiteCategoryes),JsonArray.class);
        for (JsonElement o: jsonArray) {
            String nameCategory = o.getAsJsonObject().get("name").toString();
            String codeCatecory = o.getAsJsonObject().get("code").toString().replace("\"","");
            String productsCountcodeCategory = o.getAsJsonObject().get("productsCount").toString().replace("\"","");
            JsonArray items = o.getAsJsonObject().getAsJsonArray("items");
            String urlsCategory = "https://www.auchan.ru/v1/catalog/products?merchantId=65&filter[category]="
                    +codeCatecory+"&page=1&perPage="
                    +productsCountcodeCategory+"&orderField=rank&orderDirection=asc";

            category.put(nameCategory,urlsCategory);
            for (JsonElement o2: items ) {
                String nameSubcategory = o2.getAsJsonObject().get("name").toString().replace("\"","");
                String codeSubcategory = o2.getAsJsonObject().get("code").toString().replace("\"","");
                String productsCountcodeSubcategory = o2.getAsJsonObject().get("productsCount").toString().replace("\"","");
                String urlsSubcategory = "https://www.auchan.ru/v1/catalog/products?merchantId=65&filter[category]="
                        +codeSubcategory+"&page=1&perPage="
                        +productsCountcodeSubcategory+"&orderField=rank&orderDirection=asc";
                category.put(nameSubcategory,urlsSubcategory);
            }
        }
        return category;
    }

    public ArrayList<String> getCategory(){
        ArrayList<String> arrayList = new ArrayList<>();
        JsonArray jsonArray = new Gson().fromJson(getHttpResponse(auchansiteCategoryes),JsonArray.class);
        for (JsonElement o: jsonArray) {
            arrayList.add(o.getAsJsonObject().get("name").toString());
        }
        return arrayList;
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
        Database database = new Database();
        Connection connection = database.getConn();
        Statement statement = connection.createStatement();
        String cat;
        HashMap<String,String> mapAuchan = generateUrl();
        for (String category: mapAuchan.keySet()) {
            cat = category;
            if (statement!=null){
                try {
                    statement.execute("INSERT INTO 'auchan_category' ('name') VALUES ('"+category+"');");
                    HashMap<String,String> product = getProduct(mapAuchan.get(cat));
                        for (String p: product.keySet()) {
                            String name = p.replace("'","");
                            String price = product.get(p).replace("'","");
                            statement.execute("INSERT INTO 'auchan_product' ('name','price','category') VALUES ('"+name+"','"+price+"','"+cat+"');");
                        }

                } catch (SQLException throwables) {
                    connection.close();
                    throwables.printStackTrace();
                }
            }
        }
//        if(statement!=null)statement.close();
//        connection.close();
    }
}

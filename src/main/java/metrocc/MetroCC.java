package metrocc;

import com.github.hemantsonu20.json.JsonMerge;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.Database;
import web.ConnectionToAPI;
import web.UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;

public class MetroCC {

    final String getMetroccCategoryTree = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/categories/tree";
    static final Gson gson = new Gson();

    public static String getHttpResponse(String url) {
        StringBuilder jsonString = new StringBuilder();
        InputStreamReader inputStreamReader;
        HashMap<String,String> property = new HashMap<>();
        property.put("userAgent",UserAgent.getRandomUserAgent());
        property.put("MIME","application/json");
        property.put("contentType","application/json; charset=UTF-8");
        property.put("cookie","metrostore=57; UserSettings=SelectedStore={6cfe3e81-a91d-44c0-a8b3-b41b4a2ff9a8};");
        HttpURLConnection connection = null;
        URL apiurl = null;
        try {
            apiurl = new URL(url);
            connection = new ConnectionToAPI("GET",property).getConnection(apiurl);
            inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MetroApi return error");
            if(connection!=null) connection.disconnect();
            System.out.println("MetroCC Thread sleep 2 min");
            try {
                Thread.sleep(120000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.out.println("resend");
            if (apiurl != null) {
                connection = new ConnectionToAPI("GET",property).getConnection(apiurl);
            }
            try {
                if (connection != null) {
                    inputStreamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader br = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = br.readLine()) != null) {
                        jsonString.append(line);
                    }
                    br.close();
                    connection.disconnect();
                    return jsonString.toString();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return String.valueOf(jsonString);
    }

    public JsonObject getProduct(String id) {
        JsonObject jsonObject = null;
        String paginate;
        String urltotal = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/products?category_id[]D=" + id + "";
        paginate = gson.fromJson(getHttpResponse(urltotal), JsonObject.class).get("data").getAsJsonObject().get("total").toString();
        int paginateInt = Integer.parseInt(paginate);
        String url;
        if (paginateInt >= 100) {
            while (paginateInt >= 100) {
                paginateInt = paginateInt - 100;
                url = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/products?category_id[]D=" + id + "&paginate=100";
                JsonObject tmpJsonObject = gson.fromJson(getHttpResponse(url), JsonObject.class);
                if (jsonObject == null) {
                    jsonObject = tmpJsonObject;
                } else {
                    String outMerge = JsonMerge.merge(jsonObject.toString(), tmpJsonObject.toString());
                    jsonObject = gson.fromJson(outMerge, JsonObject.class);
                }
            }
            if (paginateInt > 0) {
                url = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/products?category_id[]D=" + id + "&paginate=" + paginateInt + "";
                JsonObject tmp = gson.fromJson(getHttpResponse(url), JsonObject.class);
                String outMerge = JsonMerge.merge(jsonObject.toString(), tmp.toString());
                jsonObject = gson.fromJson(outMerge, JsonObject.class);
                return jsonObject;
            }
        }
        if (paginateInt > 0) {
            url = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/products?category_id[]D=" + id + "&paginate=" + paginateInt + "";
            jsonObject = gson.fromJson(getHttpResponse(url), JsonObject.class);
            return jsonObject;
        }
        return jsonObject;
    }


    public void toDatabase() throws SQLException {
        Database database = new Database();
        Connection connection = database.getConn();
        PreparedStatement psCategory = connection.prepareStatement("INSERT INTO metrocc_category (name,site_id) VALUES (?,?);");
        PreparedStatement psProduct = connection.prepareStatement("INSERT INTO 'metrocc_product' ('name','id_category','price','price_opt', 'opt_count','packing') " +
                "VALUES (?,?,?,?,?,?);");
        Statement statement;
        statement = connection.createStatement();
        statement.execute("DELETE FROM metrocc_category;");
        statement.execute("DELETE FROM metrocc_product;");
        Database.commit(connection);
        JsonObject jsonObjectCategory = gson.fromJson(getHttpResponse(getMetroccCategoryTree), JsonObject.class);
        for (JsonElement o : jsonObjectCategory.get("data").getAsJsonArray()) {
            for (JsonElement subCategory : o.getAsJsonObject().get("childs").getAsJsonArray()) {
                String name = subCategory.getAsJsonObject().get("name").toString().toLowerCase();
                String id = subCategory.getAsJsonObject().get("id").toString();
                psCategory.setString(1, name);
                psCategory.setString(2, id);
                psCategory.addBatch();
                JsonObject jsonObjectProduct = null;
                try {
                    jsonObjectProduct = getProduct(id);
                } catch (NullPointerException e) {
                    System.out.println("Касяк!!! в получении продуктов по id");
                }
                if (jsonObjectProduct != null) {
                    JsonArray jsonArray = jsonObjectProduct.get("data").getAsJsonObject().get("data").getAsJsonArray();
                    jsonArrayToDatabase(jsonArray, psProduct);
                } else {
                    try {
                        JsonArray subLevel2 = subCategory.getAsJsonObject().get("childs").getAsJsonArray();
                        for (JsonElement sl2 : subLevel2) {
                            String subLevel2name = sl2.getAsJsonObject().get("name").toString().toLowerCase();
                            String subLevel2id = sl2.getAsJsonObject().get("id").toString();
                            psCategory.setString(1, subLevel2name);
                            psCategory.setString(2, subLevel2id);
                            psCategory.addBatch();
                            jsonObjectProduct = getProduct(subLevel2id);
                            if (jsonObjectProduct != null) {
                                JsonArray jsonArray = jsonObjectProduct.get("data").getAsJsonObject().get("data").getAsJsonArray();
                                jsonArrayToDatabase(jsonArray, psProduct);
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("КАСЯК!!! в категории" + id + " (" + name + ")");
                    }
                }
            }
        }
        psCategory.executeBatch();
        psProduct.executeBatch();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        statement.execute("UPDATE metrocc_status SET lastDateUpdate="+"\""+timestamp+"\";");
        Database.commit(connection);
        System.out.println("metrocc filling complate");
    }

    public void jsonArrayToDatabase(JsonArray jsonArray, PreparedStatement ps) throws SQLException {
        for (JsonElement jp : jsonArray) {
            String outOfStock = jp.getAsJsonObject().get("stock").getAsJsonObject().get("text").getAsString();
            if (!outOfStock.equals("Отсутствует")) {
                String nameProduct = jp.getAsJsonObject().get("name").getAsString().replace("'", "").toLowerCase();
                String categoryId = jp.getAsJsonObject().get("category_id").getAsString();
                String price = jp.getAsJsonObject().get("prices").getAsJsonObject().get("price").getAsString();
                String packing = jp.getAsJsonObject().get("packing").getAsJsonObject().get("type").getAsString();
                try {
                    JsonArray priceLevels = jp.getAsJsonObject().get("prices").getAsJsonObject().get("levels").getAsJsonArray();
                    for (JsonElement priceopt : priceLevels) {
                        String count = priceopt.getAsJsonObject().get("count").toString();
                        String priceOpt = priceopt.getAsJsonObject().get("price").toString();
                        ps.setString(1, nameProduct);
                        ps.setString(2, categoryId);
                        ps.setString(3, price);
                        ps.setString(4, priceOpt);
                        ps.setString(5, count);
                        ps.setString(6, packing);
                        ps.addBatch();
                    }
                } catch (NullPointerException e) {
                    ps.setString(1, nameProduct);
                    ps.setString(2, categoryId);
                    ps.setString(3, price);
                    ps.setString(6, packing);
                    ps.addBatch();
//                    System.out.println("Товар "+ nameProduct+" не отптовый");
                }
            }
        }
    }
}

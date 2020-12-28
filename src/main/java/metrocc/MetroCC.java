package metrocc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.Database;
import web.UserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MetroCC {

    String metroccCategoryurl = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/categories";
    static Gson gson = new Gson();

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
            connection.setRequestProperty("Cookie", "metrostore=57; UserSettings=SelectedStore={6cfe3e81-a91d-44c0-a8b3-b41b4a2ff9a8};");
            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(connection.getInputStream());
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("MetroCC Thread sleep 60 sec");
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

    public void getProduct(String id){
        String paginate;
        String urltoral = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/products?category_id[]D="+id+"";
        paginate = gson.fromJson(getHttpResponse(urltoral),JsonObject.class).get("data").getAsJsonObject().get("total").toString();
        int paginateInt = Integer.parseInt(paginate);
//        int count= 0;
//        if(paginateInt>=100){
//            while ()
            String url = "https://api.metro-cc.ru/api/v1/C98BB1B547ECCC17D8AEBEC7116D6/57/products?category_id[]D="+id+"&paginate="+paginate+"";
//        }

        System.out.println(url);
    }

    public void toDatabase() throws SQLException {
        Database database = new Database();
        Connection connection = database.getConn();
        Statement statement;
        statement = connection.createStatement();
        statement.execute("DELETE FROM metrocc_category;");
//        statement.execute("DELETE FROM metrocc_product;");
//        HashMap<String, String> category = new HashMap<>();
        String url = metroccCategoryurl;
        JsonObject jsonObject = gson.fromJson(getHttpResponse(url), JsonObject.class);
        for (JsonElement o : jsonObject.get("data").getAsJsonArray()) {
            String name = o.getAsJsonObject().get("name").toString().toLowerCase();
            String id = o.getAsJsonObject().get("id").toString();
            statement.execute("INSERT INTO 'metrocc_category' ('name','site_id') VALUES ('" + name + "','" + id + "');");
            getProduct(id);
            System.out.println(name + " " + id);
        }
    }
}

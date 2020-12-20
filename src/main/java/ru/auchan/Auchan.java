package ru.auchan;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

    public ArrayList<String> generateUrl(){
        ArrayList <String> urls = new ArrayList<>();
        JsonArray jsonArray = new Gson().fromJson(getHttpResponse(auchansiteCategoryes),JsonArray.class);
        for (JsonElement o: jsonArray) {
            JsonArray items = o.getAsJsonObject().getAsJsonArray("items");
            for (JsonElement o2: items ) {
                String code = o2.getAsJsonObject().get("code").toString().replace("\"","");
                String productsCount = o2.getAsJsonObject().get("productsCount").toString().replace("\"","");
                urls.add("https://www.auchan.ru/v1/catalog/products?merchantId=65&filter[category]="+code+"&page=1&perPage="+productsCount+"&orderField=rank&orderDirection=asc");
            }
        }
        return urls;
    }

    public ArrayList<String> getCategory(){
        ArrayList<String> arrayList = new ArrayList<>();
        JsonArray jsonArray = new Gson().fromJson(getHttpResponse(auchansiteCategoryes),JsonArray.class);
        for (JsonElement o: jsonArray) {
            arrayList.add(o.getAsJsonObject().get("name").toString());
        }
        return arrayList;
    }

    public void getProduct(){
        ArrayList<String> urls = generateUrl();
        for (String u : urls) {
            String response = getHttpResponse(u);
            JsonElement items = new Gson().fromJson(response, JsonObject.class).get("items");
            for (JsonElement i: items.getAsJsonArray()) {
                String title = i.getAsJsonObject().get("title").toString();
                String priceValue = i.getAsJsonObject().get("price").getAsJsonObject().get("value").toString();
                String priceCurrency = i.getAsJsonObject().get("price").getAsJsonObject().get("currency").toString();

                System.out.println(title+" "+priceValue+priceCurrency);
            }
        }
//        return "";
    }
}

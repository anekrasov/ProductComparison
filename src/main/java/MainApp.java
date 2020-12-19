import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenta.Lenta;
import database.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("load.....");
        Connection conn = Database.conn();
        Statement statement = conn.createStatement();
        String name;
        String code;
        Lenta lenta = new Lenta();
        JsonObject dataMenu = lenta.getJsonDataMenu();
        JsonArray allCategoryProduct = lenta.getCategoryProduct(dataMenu);
        for (JsonElement o: allCategoryProduct) {
            name = o.getAsJsonObject().get("name").toString();
            code = o.getAsJsonObject().get("code").toString();
            String sql = "INSERT INTO 'lenta_category' ('productCategory', 'code') VALUES ('"+name+"', "+code+");";
            JsonObject product = lenta.getProduct(code);
            System.out.println(name);
            statement.execute(sql);
            for (JsonElement p: product.getAsJsonArray("skus") ) {
                String title = p.getAsJsonObject().get("title").toString().replace("'","");
                String regularPrice = p.getAsJsonObject().get("regularPrice").getAsJsonObject().get("value").toString();
                String cardPrice = p.getAsJsonObject().get("cardPrice").getAsJsonObject().get("value").toString();
                String sqlproduct = "INSERT INTO 'lenta_product' ('name', 'price','price_card','category') " +
                        "VALUES ('"+title+"', "+regularPrice+","+cardPrice+","+name+");";
//                System.out.println(sqlproduct);
                statement.execute(sqlproduct);
//                System.out.println(title+" цена: " + regularPrice + " цена по карте: "+cardPrice);
            }
//            System.out.println(code);
//            System.out.println(product);
        }
        statement.close();
        conn.close();

//        System.out.println(lenta.getCategoryProduct(dataMenu));
    }
}

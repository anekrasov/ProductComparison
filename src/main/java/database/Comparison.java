package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Comparison {

    public static ResultSet getProduct(String store, String name) throws SQLException {
        String storeName = switch (store) {
            case "lenta" -> "lenta_product";
            case "auchan" -> "auchan_product";
            case "metrocc" -> "metrocc_product";
            default -> null;
        };
        Database database = new Database();
        Connection connection = database.getConn();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM  "+storeName+" WHERE name LIKE \"%"+name+"%\";";
        return statement.executeQuery(sql);
    }
}

package database;

import database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Status {

    public static ResultSet getLastDataUpdate (String shop) throws SQLException {
        ResultSet result = null;
        final String auchan = "auchan";
        final String lenta = "lenta";
        final String metrocc = "metrocc";
        Database database = new Database();
        Connection connection = database.getConn();
        Statement statement = connection.createStatement();
        switch (shop){
            case auchan:
                result = statement.executeQuery("SELECT lastDateUpdate FROM auchan_status");
                break;
            case metrocc:
                result = statement.executeQuery("SELECT lastDateUpdate FROM lenta_status");
                break;
            case lenta:
                result = statement.executeQuery("SELECT lastDateUpdate FROM metrocc_status");
                break;
            default:
                break;
        }
        return result;
    }
}

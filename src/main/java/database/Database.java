package database;

import java.sql.*;

public class Database {
    public static Connection conn = null;
    public static Statement statmt;

    private static void conn() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:product");
//            System.out.println("База Подключена!");
            createTables();
        }catch (Exception e){
//            System.out.println("База не подключена");
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        conn();
        return conn;
    }

    public static void createTables() throws SQLException
    {
        String lenta_category = "CREATE TABLE if not exists \"lenta_category\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\t\"code\"\tTEXT UNIQUE,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        String lenta_product = "CREATE TABLE if not exists \"lenta_product\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"sub_name\"\tTEXT,\n" +
                "\t\"price\"\tTEXT,\n" +
                "\t\"price_card\"\tTEXT,\n" +
                "\t\"category\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"category\") REFERENCES \"lenta_category\"(\"name\"),\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        String auchan_category = "CREATE TABLE if not exists \"auchan_category\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT UNIQUE,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        String auchan_product ="CREATE TABLE if not exists \"auchan_product\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"price\"\tTEXT,\n" +
                "\t\"category\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"id\"),\n" +
                "\tFOREIGN KEY(\"category\") REFERENCES \"auchan_category\"(\"name\")\n" +
                ");";

        String metrocc_category ="CREATE TABLE if not exists \"metrocc_category\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"site_id\"\tTEXT UNIQUE,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";

        String metrocc_product = "CREATE TABLE if not exists \"metrocc_product\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\t\"id_category\"\tTEXT,\n" +
                "\t\"price\"\tTEXT,\n" +
                "\t\"price_opt\"\tTEXT,\n" +
                "\t\"opt_count\"\tTEXT,\n" +
                "\t\"packing\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";

        statmt = conn.createStatement();
        statmt.execute(lenta_category);
        statmt.execute(lenta_product);
        statmt.execute(auchan_category);
        statmt.execute(auchan_product);
        statmt.execute(metrocc_category);
        statmt.execute(metrocc_product);
//        System.out.println("Таблицы созданы или уже существует.");
    }
}


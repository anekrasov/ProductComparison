package database;

import java.sql.*;

public class Database {
    public static Connection conn = null;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    private static void conn() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:product");
            System.out.println("База Подключена!");
            createTables();
        }catch (Exception e){
            System.out.println("База не подключена");
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        conn();
        return conn;
    }

    // --------Создание таблицы--------
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
                "\t\"price\"\tTEXT,\n" +
                "\t\"price_card\"\tTEXT,\n" +
                "\t\"category\"\tTEXT,\n" +
                "\t\"sub_name\"\tTEXT,\n" +
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

        statmt = conn.createStatement();
        statmt.execute(lenta_category);
        statmt.execute(lenta_product);
        statmt.execute(auchan_category);
        statmt.execute(auchan_product);
        System.out.println("Таблицы созданы или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void writeDB(String sql ) throws SQLException
    {
        statmt.execute(sql);
        System.out.println("Таблица заполнена");
    }

    // -------- Вывод таблицы--------
    public static void readDB() throws SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM users");

        while(resSet.next())
        {
            int id = resSet.getInt("id");
            String  name = resSet.getString("name");
            String  phone = resSet.getString("phone");
            System.out.println( "ID = " + id );
            System.out.println( "name = " + name );
            System.out.println( "phone = " + phone );
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    // --------Закрытие--------
    public static void closeDB() throws SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();
        System.out.println("Соединения закрыты");
    }


}


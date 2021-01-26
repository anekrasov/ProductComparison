package database;

import web.Web;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.util.Objects;

public class Database {

    public static Connection conn = null;
    public static Statement statmt;

    private static void conn() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:product.db3");
            conn.setAutoCommit(false);
//            System.out.println("База Подключена!");
//            createTables();
        }catch (Exception e){
//            System.out.println("База не подключена");
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        conn();
        return conn;
    }

    public static void createTables()
    {
        conn();
        ClassLoader classLoader = Web.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("product.sql")).getFile());
        String contents;
        try {
            statmt = conn.createStatement();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(file.toPath())));
            while ((contents = bufferedReader.readLine())!=null){
                statmt.execute(contents);
            }
            conn.commit();
        } catch (IOException | SQLException ignored) {
        }
        try {
            statmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Таблицы созданы или уже существует.");
    }
}


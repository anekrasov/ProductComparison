package database;

import web.Web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Database {

    public static Connection conn = null;
    public static Statement statmt;

    private static void conn() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:product.db3");
            conn.setAutoCommit(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        conn();
        return conn;
    }

    public static void createTables(){
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
            System.out.println("ошибка создания базы");
        }
        try {
            statmt.close();
            conn.close();
            System.out.println("Таблицы созданы или уже существует.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    synchronized public static void commit(Connection connection) throws SQLException {
        connection.commit();
    }
}


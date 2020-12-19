package database;

import java.sql.*;

public class Database {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public Database() {
        try {
            conn();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static Connection conn() throws ClassNotFoundException, SQLException
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:product");
        System.out.println("База Подключена!");
        return conn;
    }

    // --------Создание таблицы--------
    public static void createDB() throws SQLException
    {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");
        System.out.println("Таблица создана или уже существует.");
    }

    // --------Заполнение таблицы--------
    public static void writeDB( ) throws SQLException
    {
        statmt.execute("INSERT INTO 'lenta' ('productCategory', 'code') VALUES ('Petya', 125453); ");
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Vasya', 321789); ");
        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");

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

    public static Connection getConn() {
        return conn;
    }
}


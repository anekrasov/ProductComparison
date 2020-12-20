import database.Database;
import ru.auchan.Auchan;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("load.....");
        Auchan auchan = new Auchan();
//        auchan.getProduct();
        ArrayList<String> arrayList = auchan.getCategory();
        Connection conn = Database.conn();
        Statement statement = conn.createStatement();
        String name;
        for (String s: arrayList) {
            name = s;
            System.out.println(name);
            statement.execute("INSERT INTO 'auchan_category' ('name') VALUES ('"+name+"');");
        }

        statement.close();
        conn.close();
    }
}

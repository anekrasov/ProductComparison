import com.lenta.Lenta;
import database.Database;
import ru.auchan.Auchan;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("load.....");

        Auchan auchan = new Auchan();
        Lenta lenta = new Lenta();

        Thread thread1 = new Thread(() -> {
            try {
                auchan.toDatabase();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                lenta.toDatabase();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
    }
}

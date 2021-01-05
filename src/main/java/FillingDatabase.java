import com.lenta.Lenta;
import metrocc.MetroCC;
import ru.auchan.Auchan;

import java.lang.management.MemoryType;
import java.sql.SQLException;

public class FillingDatabase {

    public static void filling(){
        Auchan auchan = new Auchan();
        Lenta lenta = new Lenta();
        MetroCC metroCC = new MetroCC();

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

        Thread thread3 = new Thread(() -> {
            try {
                metroCC.toDatabase();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

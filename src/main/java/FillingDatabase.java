import com.lenta.Lenta;
import ru.auchan.Auchan;

import java.sql.SQLException;

public class FillingDatabase {

    public static void filling(){
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

import com.lenta.Lenta;
import metrocc.MetroCC;
import ru.auchan.Auchan;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public static void service(){
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
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
        service.scheduleWithFixedDelay(thread1,0,3, TimeUnit.HOURS);
        service.scheduleWithFixedDelay(thread2,0,3, TimeUnit.HOURS);
        service.scheduleWithFixedDelay(thread3,0,3, TimeUnit.HOURS);

    }
}

package database;

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
                System.out.println("auchan service Start");
                auchan.toDatabase();
                System.out.println("auchan service Stop");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("lenta service Start");
                lenta.toDatabase();
                System.out.println("lenta service Stop");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                System.out.println("metroCC service Start");
                metroCC.toDatabase();
                System.out.println("metroCC service Stop");
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
        Thread schedule_service_run = new Thread(() -> {
            System.out.println("Schedule service run");
            filling();
        });
        service.scheduleWithFixedDelay(schedule_service_run,0,40, TimeUnit.MINUTES);
    }
}

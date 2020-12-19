import ru.auchan.Auchan;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("load.....");
        Auchan auchan = new Auchan();
        auchan.getProduct();
    }
}

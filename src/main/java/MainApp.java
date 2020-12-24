import java.sql.ResultSet;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("load.....");
        FillingDatabase.filling();
//        try {
//            ResultSet resultSetLenta = Comparison.getProduct("lenta","Walker");
//            ResultSet resultSetAuchan = Comparison.getProduct("auchan","Walker");
//            while (resultSetLenta.next()){
//                String name = resultSetLenta.getString("name");
//                String nameSubname = resultSetLenta.getString("sub_name");
//                String price = resultSetLenta.getString("price");
//                String price_card = resultSetLenta.getString("price_card");
//                System.out.println("Lenta :"+name+"("+nameSubname+")"+"  "+price +" po karte :"+price_card);
//            }
//            while (resultSetAuchan.next()){
//                String name = resultSetAuchan.getString("name");
//                String price = resultSetAuchan.getString("price");
//                System.out.println("Auchan: "+ name+"  "+price);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }
}

import metrocc.MetroCC;
import web.Web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import static spark.Spark.get;
import static spark.Spark.post;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("load.....");
//        FillingDatabase.filling();
        get("/", (req, res) -> Web.getPage("base.html"));
        get("/comparison", (req, res) -> Web.getPage("comparison.html"));
        get("/search", (req, res) -> "search!!");
        post("/search", (req, res) -> {
            String text = req.queryParams("text").toLowerCase();
            ResultSet lentaResult = Comparison.getProduct("lenta",text);
            ResultSet auchanResult = Comparison.getProduct("auchan",text);
            ResultSet metroccResult = Comparison.getProduct("metrocc",text);
            String lenta = "";
            String auchan = "";
            String metrocc = "";
            while (lentaResult.next()){
                String name = lentaResult.getString("name");
                String nameSubname = lentaResult.getString("sub_name");
                String price = lentaResult.getString("price");
                String price_card = lentaResult.getString("price_card");
                String th = "<tr><td>"+name+" "+nameSubname+"</td><td>"+price +"</td><td>"+price_card+"</td></tr>";
                lenta = lenta + th;
//                System.out.println("Lenta :"+name+"("+nameSubname+")"+"  "+price +" po karte :"+price_card);
            }
            while (auchanResult.next()){
                String name = auchanResult.getString("name");
                String price = auchanResult.getString("price");
                String th = "<tr><td>"+name+"</td><td>"+price +"</td></tr>";
                auchan = auchan + th;
//                System.out.println("Auchan: "+ name+"  "+price);
            }
            while (metroccResult.next()){
                String name = metroccResult.getString("name");
                String price = metroccResult.getString("price");
                String price_opt = metroccResult.getString("price_opt");
                String opt_count = metroccResult.getString("opt_count");
                String th = "<tr><td>"+name+"</td><td>"+price +"</td><td>"+price_opt+"</td><td>"+opt_count+"</td></tr>";
                metrocc = metrocc + th;
//                System.out.println("Auchan: "+ name+"  "+price);
            }
            return Web.getPage("comparison.html", lenta, auchan, metrocc);
        });

        get("/filling", (req, res) -> {
            FillingDatabase.filling();
            return Web.getPage("fillingPage.html");
        });
    }
}

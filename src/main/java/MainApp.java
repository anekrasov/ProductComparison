import web.Web;

import java.sql.ResultSet;

import static spark.Spark.get;
import static spark.Spark.post;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("load.....");
        get("/", (req, res) -> Web.getPage("base.html"));
        get("/comparison", (req, res) -> Web.getPage("comparison.html"));
        get("/search", (req, res) -> "search!!");
        post("/search", (req, res) -> {
            String text = req.queryParams("text");
            ResultSet lentaResult = Comparison.getProduct("lenta",text);
            ResultSet auchanResult = Comparison.getProduct("auchan",text);
            String lenta = "";
            String auchan = "";
            while (lentaResult.next()){
                String name = lentaResult.getString("name");
                String nameSubname = lentaResult.getString("sub_name");
                String price = lentaResult.getString("price");
                String price_card = lentaResult.getString("price_card");
                String th = "<tr><td>"+name+" "+"("+nameSubname+")"+price +" po karte :"+price_card+"</td></tr>";
                lenta = lenta + th;
                System.out.println("Lenta :"+name+"("+nameSubname+")"+"  "+price +" po karte :"+price_card);
            }
            while (auchanResult.next()){
                String name = auchanResult.getString("name");
                String price = auchanResult.getString("price");
                String th = "<tr><td>"+name+"  "+price +"</td></td>";
                auchan = auchan + th;
                System.out.println("Auchan: "+ name+"  "+price);
            }
            return Web.getPage("comparison.html", lenta, auchan);
        });

        get("/filling", (req, res) -> {
            FillingDatabase.filling();
            return Web.getPage("fillingPage.html");
        });
    }
}

import database.Comparison;
import database.Database;
import database.FillingDatabase;
import database.Status;
import web.Web;

import java.sql.ResultSet;
import java.util.HashMap;

import static spark.Spark.*;

public class MainApp {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        System.out.println("load.....");
        Database.createTables();
//        database.FillingDatabase.filling();
        database.FillingDatabase.service();
        get("/", (req, res) -> Web.getPage("base.html"));
        get("/comparison", (req, res) -> Web.getPage("comparison.html"));
        get("/search", (req, res) -> "search!!");
        post("/search", (req, res) -> {
            String lenta = "";
            String auchan = "";
            String metrocc = "";
            String text = req.queryParams("text").toLowerCase();
            String lenta_checked = req.queryParams("lenta");
            String auchan_checked = req.queryParams("auchan");
            String metrocc_checked = req.queryParams("metro");
            HashMap<String,String> shopSelected = new HashMap<>();
            if (lenta_checked != null && lenta_checked.equals("on")){
                ResultSet lentaResult = Comparison.getProduct("lenta",text);
                while (lentaResult.next()){
                    String name = lentaResult.getString("name");
                    String nameSubname = lentaResult.getString("sub_name");
                    String price = lentaResult.getString("price");
                    String price_card = lentaResult.getString("price_card");
                    String th = "<tr><td>"+name+" "+nameSubname+"</td><td>"+price +"</td><td>"+price_card+"</td></tr>";
                    lenta = lenta + th;
                }
                shopSelected.put("lenta",lenta);
            }
            if(auchan_checked != null &&auchan_checked.equals("on")){
                ResultSet auchanResult = Comparison.getProduct("auchan",text);
                while (auchanResult.next()){
                    String name = auchanResult.getString("name");
                    String price = auchanResult.getString("price");
                    String th = "<tr><td>"+name+"</td><td>"+price +"</td></tr>";
                    auchan = auchan + th;
                }
                shopSelected.put("auchan",auchan);
            }
            if (metrocc_checked != null && metrocc_checked.equals("on")){
                ResultSet metroccResult = Comparison.getProduct("metrocc",text);
                while (metroccResult.next()){
                    String name = metroccResult.getString("name");
                    String price = metroccResult.getString("price");
                    String price_opt = metroccResult.getString("price_opt");
                    String opt_count = metroccResult.getString("opt_count");
                    String th = "<tr><td>"+name+"</td><td>"+price +"</td><td>"+price_opt+"</td><td>"+opt_count+"</td></tr>";
                    metrocc = metrocc + th;
                }
                shopSelected.put("metrocc",metrocc);
            }
            return Web.getPage("comparison.html", shopSelected);
        });
        get("/status",(req,res) -> {
            String auchanLastDateUpdate = Status.getLastDataUpdate("auchan").getString("lastDateUpdate");
            String lentaLastDateUpdate = Status.getLastDataUpdate("lenta").getString("lastDateUpdate");
            String metroccLastDateUpdate = Status.getLastDataUpdate("metrocc").getString("lastDateUpdate");
            return Web.getPage("status.html",lentaLastDateUpdate, auchanLastDateUpdate,metroccLastDateUpdate);
        });
        get("/filling", (req, res) -> {
            FillingDatabase.filling();
            return Web.getPage("fillingPage.html");
        });
        get("/initialDatabase", (req, res) -> {
            Database.createTables();
            return "Databse initial";
        });
        get("/testTable", (req,res)-> Web.getPage("testTable.html"));
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
        }
}

import com.lenta.SiteLenta;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import selenium.Selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainApp {
    public static void main(String[] args) {

//        try{
        SiteLenta siteLenta = new SiteLenta();
        Map<String,String> catalogGroutes = siteLenta.getCatalogGroups();
        for (String o: catalogGroutes.keySet()) {
            String link = catalogGroutes.get(o);
            int size = siteLenta.getProduct(link).size();
            System.out.println("Линк на " + o + " " + link + "наименований товаров в категории " + size);
        }
        System.out.println(catalogGroutes.keySet());
        siteLenta.closeSite();

//        System.out.println(siteLenta.getProduct(catalogGroutes.get(0)));
//        catch (Exception e){
//            System.out.println(e);

//        webDriver.close();
//        }

    }
}

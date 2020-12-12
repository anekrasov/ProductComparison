import com.lenta.SiteLenta;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import selenium.Selenium;

import java.util.ArrayList;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        WebDriver webDriver = new Selenium().getDriver();
//        try{
        SiteLenta siteLenta = new SiteLenta(webDriver);
        ArrayList<String> catalogGroutes = siteLenta.getCatalogGroups();
        System.out.println(siteLenta.getProduct(catalogGroutes.get(0)));
//        catch (Exception e){
//            System.out.println(e);
            webDriver.close();
//        }

    }
}

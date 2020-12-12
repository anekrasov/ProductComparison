package com.lenta;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SiteLenta {
    public static final String url = "https://lenta.com/catalog";
    private static final String button = "/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]";
    private static final String button2 = "/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]";
    private static final String loadMoreBotton = "/html/body/div[1]/div[1]/div/main/article/div/div/div/div[2]/div/div[4]/div[2]/div[1]/div/div[2]/button";
    private static final String catalogGroups = "group-card";
    private static final String productList = "sku-card-small-container";
    private static final String catalogGrid = "/html/body/div[1]/div[1]/div/main/article/div/div/div/div[3]";
    private static final String cooke = "/html/body/div[2]/div/button/div[1]";
    public WebDriver driver;

    public SiteLenta(WebDriver driver) {
        this.driver = driver;
        driver.get(url);
        selectSity();
    }

    public ArrayList<String> getCatalogGroups(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<String> href = new ArrayList<>();
        WebElement catalogGridPage = driver.findElement(By.xpath(catalogGrid));
        List<WebElement> catalog_groups = catalogGridPage.findElements(By.className(catalogGroups));
        for (WebElement o: catalog_groups) {
            String link = o.getAttribute("href");
            href.add(link);
//            System.out.println(o.getAttribute("href"));
        }
        return href;
    }

    public Map<String, String> getProduct(String url) {
        Map<String,String> productsMap = new HashMap<>();
        driver.get(url);
//        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        selectSity();
        cookeClick();
        loadMore();
        List<WebElement> products = driver.findElements(By.className(productList));
        for (WebElement o : products) {
            String product_name = o.findElement(By.className("sku-card-small__head")).getText();
            String product_price_integer = o.findElement(By.className("sku-price__integer")).getText();
            String product_price_fraction = o.findElement(By.className("sku-price__fraction")).getText();
            String product_price_icon = o.findElement(By.className("sku-price__icon")).getText();
            productsMap.put(product_name,product_price_integer+"."+product_price_fraction+product_price_icon);
        }
        return productsMap;
    }

    public void loadMore(){
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                driver.findElement(By.xpath(loadMoreBotton)).click();
            }
            catch (Exception e){
                System.out.println("Элемент не найден");
                break;
            }
//            buttonLoadMore.click();
        }
    }
    private void selectSity() {
        try {
            Thread.sleep(2000);
            driver.findElement(By.xpath(button)).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath(button2)).click();
        } catch (Exception e) {
            System.out.println("Кнопки нет");
        }
    }
    private void cookeClick(){
        driver.findElement(By.xpath(cooke)).click();
    }
}

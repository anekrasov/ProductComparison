package com.lenta;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import selenium.Selenium;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteLenta {
    public static final String url = "https://lenta.com/catalog";
    private static final String button = "/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]";
    private static final String button2 = "/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]";
    private static final String loadMoreBotton = "catalog-grid-container__pagination";
    private static final String catalogGroups = "group-card";
    private static final String productList = "sku-card-small-container";
    private static final String catalogGrid = "catalog-groups-page__grid";
    private static final String cooke = "/html/body/div[2]/div/button/div[1]";
    Selenium selenium = new Selenium();
    WebDriver driver = selenium.getDriver();

    public SiteLenta() {
        driver.get(url);
        selectSity();
        cookeClick();
    }

    public Map<String,String> getCatalogGroups(){
        Map<String,String> href = new HashMap<>();
        WebElement catalogGridPage = driver.findElement(By.className(catalogGrid));
        List<WebElement> catalog_groups = catalogGridPage.findElements(By.className(catalogGroups));
        for (WebElement o: catalog_groups) {
            String link = o.getAttribute("href");
            String name = o.findElement(By.className("group-card__title")).getText();
            href.put(name,link);
//            System.out.println(o.getAttribute("href"));
        }
        return href;
    }

    public Map<String, String> getProduct(String url) {
        driver.get(url);
        cookeClick();
        loadMore();
        List<WebElement> products = driver.findElements(By.className(productList));
        Map<String,String> productsMap = new HashMap<>();
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
                Thread.sleep(1000);
                driver.findElement(By.className(loadMoreBotton)).click();
            }
            catch (Exception e){
                System.out.println("Кнопка \"Загрузить еще\" не найдена");
//                e.printStackTrace();
                break;
            }
        }
    }
    private void selectSity() {
        try {
            Thread.sleep(2000);
            driver.findElement(By.xpath(button)).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath(button2)).click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Кнопки выбора города нет");
        }
    }
    private void cookeClick(){
        try {
            driver.findElement(By.xpath(cooke)).click();
        }catch (Exception e){
            System.out.println("Кнопки кук нет");
        }
    }
    public void closeSite(){
        driver.quit();
    }
}

package com.lenta;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class SiteLenta {

    private static final String url = "https://lenta.com/catalog/myaso-ptica-kolbasa/";
    private static final String button1 = "/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]";
    private static final String button2 = "/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]";
    private static final String loadMoreBotton = "/html/body/div[1]/div[1]/div/main/article/div/div/div/div[2]/div/div[4]/div[2]/div[1]/div/div[2]/button";
    private static final String catalogGroups = "group-card";
    private static final String productList = "sku-card-small-container";
    public WebDriver driver;

    public SiteLenta(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getCatalogGroups(){
        List<String> href = null;
        List<WebElement> catalog_groups = driver.findElements(By.className(catalogGroups));
        for (WebElement o: catalog_groups) {
            href.add(o.getAttribute("href"));
        }
        return href;
    }

    public Map<String, String> getProduct() {
        Map<String,String> productsMap = null;
        List<WebElement> products = driver.findElements(By.className(productList));
        for (WebElement o : products) {
            String product_name = o.findElement(By.className("sku-card-small__head")).getText();
            String product_price_integer = o.findElement(By.className("sku-price__integer")).getText();
            String product_price_fraction = o.findElement(By.className("sku-price__fraction")).getText();
            String product_price_icon = o.findElement(By.className("sku-price__icon")).getText();
            productsMap.put(product_name,product_price_integer+product_price_fraction+product_price_icon);
        }
        return productsMap;
    }

    public void loadMore(){
        WebElement buttonLoadMore;
        while (true){
            try { buttonLoadMore = driver.findElement(By.xpath(loadMoreBotton)); }
            catch (Exception e){
//                System.out.println("Элемент не найден");
                break;
            }
            buttonLoadMore.click();
        }
    }

}

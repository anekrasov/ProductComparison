import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver87");
        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("--headless");
        chrome_options.addArguments("user-agent=\"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)\"");;
        WebDriver driver = new ChromeDriver(chrome_options);
        driver.get("https://lenta.com/catalog/myaso-ptica-kolbasa/");
        Thread.sleep(5000);
        WebElement yes = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]"));
        yes.click();
        Thread.sleep(2000);
        WebElement yes2 = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[2]"));
        yes2.click();
//        driver.get("https://lenta.com/catalog/");
//        WebElement otherSity = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div/div[2]/div/div[3]/button[1]"));
//        otherSity.click();

//        List<WebElement> elements = driver.findElements(By.className("group-card"));
//        for (WebElement o: elements) {
//            WebElement name = o.findElement(By.className("group-card__title"));
//            System.out.println(name.getText());
//        }
//        Thread.sleep(5000);
        WebElement load;
        int i = 0;
        while (true){
            try {
                load = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/main/article/div/div/div/div[2]/div/div[4]/div[2]/div[1]/div/div[2]/button"));
            }catch (Exception e){
                System.out.println("Элемент не найден");
                break;
            }
            Thread.sleep(2000);
            load.click();
            i++;
            System.out.println(i);
        }
        List<WebElement> products = driver.findElements(By.className("sku-card-small-container"));
        System.out.println("Всего " + products.size());
        for (WebElement o: products) {
            String product_name = o.findElement(By.className("sku-card-small__head")).getText();
            String product_price_integer = o.findElement(By.className("sku-price__integer")).getText();
            String product_price_fraction = o.findElement(By.className("sku-price__fraction")).getText();
            String product_price_icon = o.findElement(By.className("sku-price__icon")).getText();
//            String product_price_weight = o.findElement(By.className("sku-price__weight")).getText();

            System.out.println(product_name + "  "
                    + product_price_integer
                    + "."
                    +  product_price_fraction
                    + product_price_icon
                    /*+ product_price_weight*/);
        }
        driver.quit();
    }
}

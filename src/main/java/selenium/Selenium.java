package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Selenium {
    WebDriver driver;

    public Selenium() {
        System.setProperty("webdriver.chrome.driver", "chromedriver87");
        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("--headless");
        chrome_options.addArguments("user-agent=\"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)\"");
        driver = new ChromeDriver(chrome_options);
    }

    public WebDriver getDriver() {
        return driver;
    }
}

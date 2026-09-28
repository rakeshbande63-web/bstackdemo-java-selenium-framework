package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = ConfigReader.get("browser");
        if (!"chrome".equalsIgnoreCase(browser)) {
            throw new IllegalArgumentException("Only Chrome is configured, but browser=" + browser);
        }

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-popup-blocking");
        if (ConfigReader.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080",
                    "--no-sandbox", "--disable-dev-shm-usage");
        } else {
            options.addArguments("--start-maximized");
        }
        return new ChromeDriver(options);
    }
}

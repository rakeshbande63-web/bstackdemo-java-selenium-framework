package utils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
public final class WebDriverFactory {
 private WebDriverFactory(){}
 public static WebDriver createDriver(){
  if(!"chrome".equalsIgnoreCase(ConfigReader.get("browser")))throw new IllegalArgumentException("Only Chrome is configured");
  WebDriverManager.chromedriver().setup(); 
  ChromeOptions o=new ChromeOptions();
  if(ConfigReader.getBoolean("headless"))o.addArguments("--headless=new");
  o.addArguments("--start-maximized","--disable-notifications","--disable-popup-blocking");
  return new ChromeDriver(o);
 }
}

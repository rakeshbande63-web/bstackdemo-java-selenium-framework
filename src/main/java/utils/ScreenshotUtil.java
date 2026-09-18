package utils;
import org.openqa.selenium.*;
import java.io.*; import java.nio.file.*; import java.util.Base64;
public final class ScreenshotUtil 
{
 private ScreenshotUtil()
 {}
 public static String capture(WebDriver d,String name)
 {try{
  Path dir=Path.of("test-output","screenshots");Files.createDirectories(dir);
  Path target=dir.resolve(name.replaceAll("[^a-zA-Z0-9._-]","_")+".png");
  Files.copy(((TakesScreenshot)d).getScreenshotAs(OutputType.FILE).toPath(),target,StandardCopyOption.REPLACE_EXISTING);return target.toString();
 }catch(Exception e){return null;}}
 public static String toBase64(String p)
 {try{return Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of(p)));}
 catch(Exception e){return "";}}
}

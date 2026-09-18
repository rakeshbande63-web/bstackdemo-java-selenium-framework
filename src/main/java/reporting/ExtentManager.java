package reporting;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.nio.file.*;
public final class ExtentManager {
 private static ExtentReports extent;
 private ExtentManager(){}
 public static synchronized ExtentReports getExtent()
 {if(extent==null){try{Files.createDirectories(Path.of("test-output"));
 }
 catch(Exception ignored){}
  ExtentSparkReporter r=new ExtentSparkReporter("test-output/ExtentReport.html");
  r.config().setReportName("bstackdemo.com Automation Report");r.config().setDocumentTitle("Java Selenium Test Execution Report");
  extent=new ExtentReports();extent.attachReporter(r);extent.setSystemInfo("Application","bstackdemo.com");extent.setSystemInfo("Framework","Java + Selenium + TestNG + POM");}
  return extent;}
 public static void flush(){if(extent!=null)extent.flush();}
}

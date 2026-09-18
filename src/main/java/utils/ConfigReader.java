package utils;
import java.io.InputStream;
import java.util.Properties;
public final class ConfigReader {
 private static final Properties P=new Properties();
 static { try(InputStream in=ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")){if(in==null)throw new RuntimeException("config.properties not found");P.load(in);}catch(Exception e){throw new RuntimeException(e);} }
 private ConfigReader(){}
 public static String get(String k){String v=System.getProperty(k);return v!=null?v:P.getProperty(k);}
 public static int getInt(String k){return Integer.parseInt(get(k));}
 public static boolean getBoolean(String k){return Boolean.parseBoolean(get(k));}
}

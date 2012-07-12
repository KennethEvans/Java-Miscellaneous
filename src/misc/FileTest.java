package misc;

import java.io.File;

public class FileTest
{
//  public static final String LS = System.getProperty("line.separator");
  public static final String LS = "\n";
  private static final String DEFAULT_FILENAME = "LaB6_80kev_40cm_0001.tif";
  
  public static String fileCheck(String fileName) {
    String info = "";
    File file = new File(fileName);
    try {
      info += "Checking: " + fileName + LS;
      info += "Exists: " + file.exists() + LS;
      info += "CWD: " + getCwd() + LS;
      info += "AbsolutePath: " + file.getAbsolutePath() + LS;
      info += "Parent: " + file.getParent() + LS;
      info += "ParentFile: " + file.getParentFile() + LS;
      info += "LastModified: " + file.lastModified() + LS;
      info += "Length: " + file.length() + LS;
      info += "System Information: " + LS;
      info += sysInfo();
      
      // Check if we can make a file with this full name
      info += LS;
      info += "Checking existence of a file with this absolute path" + LS;
      info += "File2 AbsolutePath: " + file.getAbsolutePath() + LS;
      File file2 = new File(file.getAbsolutePath());
      info += "File2 Exists: " + file2.exists();
    } catch(Exception ex) {
      info += LS + "Error: Cannot determine properties" + LS + ex;
    }
    return info;
  }
    
    public static String getCwd() {
    try {
      return new File(".").getCanonicalPath();
    } catch(Exception ex) {
      return ("<unknown>");
    }
  }
    
    public static String sysInfo() {
    String info = "";
    String[] properties = {"user.dir", "java.version", "java.home",
      "java.vm.version", "java.vm.vendor", "java.ext.dirs"};
    String property;
    for(int i = 0; i < properties.length; i++) {
      property = properties[i];
      info += property + ": " + System.getProperty(property, "<not found>")
        + LS;
    }
    info += getClassPath("  ");
    return info;
  }
    
    public static String getClassPath(String tabs) {
    String info = "";
    String classPath = System.getProperty("java.class.path", "<not found>");
    String[] paths = classPath.split(File.pathSeparator);
    for(int i = 0; i < paths.length; i++) {
      info += tabs + i + " " + paths[i] + LS;
    }
    return info;
  }


  /**
   * @param args
   */
  public static void main(String[] args) {
    fileCheck(DEFAULT_FILENAME);
  }

}

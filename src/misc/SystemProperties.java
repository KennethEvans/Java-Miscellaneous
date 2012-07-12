package misc;

import java.io.PrintStream;
import java.util.Properties;

/**
 * SystemProperties
 * @author Kenneth Evans, Jr.
 */
public class SystemProperties
{
  /**
   * Prints all system properties to System.out.
   */
  public static void getSystemProperties() {
    getSystemProperties(System.out);
  }

  /**
   * Prints all system properties to a PrintStream.
   * @param os is the name of the PrintStream
   */
  public static void getSystemProperties(PrintStream os) {
    try {
      Properties props = System.getProperties();
      props.store(os, "System Properties");
    } catch(Throwable t) {
      t.printStackTrace(os);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    getSystemProperties();
    System.out.println("All Done");
  }

}

package misc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest
{
 private static final String formatString = "MMM dd, yyyy HH:mm:ss.SSS zzzz (z)";
  public static final SimpleDateFormat defaultFormatter =
    new SimpleDateFormat(formatString);
  
  public static void sysInfo() {
    String version = System.getProperty("java.version","Not found");
    String osName = System.getProperty("os.name", "Not found");
    String osVersion = System.getProperty("os.version", "Not found");
    String osArch = System.getProperty("os.arch", "Not found");
    String userTimeZone = System.getProperty("user.timezone", "Not found");
    
    System.out.println("java.version=" + version);
    System.out.println("os.name=" + osName);
    System.out.println("os.version=" + osVersion);
    System.out.println("os.arch=" + osArch);
    System.out.println("user.timezone=" + userTimeZone);
  }

  public static void doTimeStamp(String[] args) {
    System.out.println("Current Time 1: " + timeStamp1());
    System.out.println("Current Time 2: " + timeStamp2());
  }

  public static void doExamples(String[] args) {
    // Make a new Date object. It will be initialized to the current time.
    Date now = new Date();

    // See what toString() returns
    System.out.println(" 1. " + now.toString());

    // Next, try the default DateFormat
    System.out.println(" 2. " + DateFormat.getInstance().format(now));

    // And the default time and date-time DateFormats
    System.out.println(" 3. " + DateFormat.getTimeInstance().format(now));
    System.out.println(" 4. " +
        DateFormat.getDateTimeInstance().format(now));

    // Next, try the short, medium and long variants of the
    // default time format
    System.out.println(" 5. " +
        DateFormat.getTimeInstance(DateFormat.SHORT).format(now));
    System.out.println(" 6. " +
        DateFormat.getTimeInstance(DateFormat.MEDIUM).format(now));
    System.out.println(" 7. " +
        DateFormat.getTimeInstance(DateFormat.LONG).format(now));

    // For the default date-time format, the length of both the
    // date and time elements can be specified. Here are some examples:
    System.out.println(" 8. " + DateFormat.getDateTimeInstance(
        DateFormat.SHORT, DateFormat.SHORT).format(now));
    System.out.println(" 9. " + DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM, DateFormat.SHORT).format(now));
    System.out.println("10. " + DateFormat.getDateTimeInstance(
        DateFormat.LONG, DateFormat.LONG).format(now));
}

  /**
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("System Info:");
    sysInfo();
    System.out.println();
    System.out.println("TimeStamp:");
    doTimeStamp(args);
    System.out.println();
    System.out.println("Examples:");
    doExamples(args);
  }

  /**
   * Generates a timestamp.
   * @return timestamp with the current time.
   */
  public static String timeStamp1() {
    Date now=new Date();
    return defaultFormatter.format(now);
  }

  /**
   * Generates a timestamp similar to xrays.core.utils.
   * 
   * @return String timestamp with the current time
   */
  public static String timeStamp2() {
    Date now = new Date();
    final SimpleDateFormat defaultFormatter = new SimpleDateFormat(formatString);
    return defaultFormatter.format(now);
  }

}

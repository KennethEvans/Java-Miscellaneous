package misc;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

/**
 * Gets the DailyDilbert from the Dilbert site.<br>
 * <br>
 * http://dilbert.com is comic page for today<br>
 * http://dilbert.com/strips/comic/2010-06-25/ is comic page for 2010-06-25<br>
 * 
 * @author Kenneth Evans, Jr.
 */
public class DailyDilbertComic
{
    private static final boolean PRINT_LINES = false;

    /**
     * Gets the URL for the Dilbert strip. This version looks at
     * "http://www.dilbert.com" for strings starting with /dyn/str_strip and
     * ending with .strip.gif, e.g.<br>
     * src="/dyn/str_strip/000000000/00000000/0000000/000000/90000/2000/700/92766/92766.strip.gif"
     * 
     * @return
     * @throws IOException
     */
    public static String todaysDilbert() throws IOException {
        // Read the Dilbert webpage
        URL url = new URL("http://www.dilbert.com");
        // Look for /dyn/str_strip/xxx.strip.gif
        String regex = "(/dyn/str_strip/.*\\.strip\\.gif)";
        Pattern pattern = Pattern.compile(regex);
        String retVal = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(url
            .openStream()));
        String line;
        while((line = br.readLine()) != null) {
            if(PRINT_LINES) {
                System.out.println(line);
            } else {
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()) {
                    retVal = "http://www.dilbert.com" + matcher.group();
                    // System.out.println(matcher.group());
                    // System.out.println(retVal);
                    br.close();
                    return retVal;
                }
            }
        }
        if(br!= null) {
            br.close();
        }
        // May 2011:  Seems to have the Sunday strip at strip.sunday.gif
        // Try this if the above fails
        regex = "(/dyn/str_strip/.*\\.strip\\.sunday\\.gif)";
        pattern = Pattern.compile(regex);
        br = new BufferedReader(new InputStreamReader(url
            .openStream()));
        while((line = br.readLine()) != null) {
            if(PRINT_LINES) {
                System.out.println(line);
            } else {
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()) {
                    retVal = "http://www.dilbert.com" + matcher.group();
                    // System.out.println(matcher.group());
                    // System.out.println(retVal);
                    br.close();
                    return retVal;
                }
            }
        }
        if(br!= null) {
            br.close();
        }

        return retVal;
    }

    /**
     * Gets the URL for the Dilbert strip. This version looks at
     * "http://www.dilbert.com" for strings with
     * "ALT=\"Today's Dilbert Comic\"". The current page apparently does not use
     * this pattern.
     * 
     * @return
     * @throws IOException
     */
    public static String todaysDilbert1() throws IOException {
        // open up the webpage to today's comic
        URL url = new URL("http://www.dilbert.com");
        BufferedReader br = new BufferedReader(new InputStreamReader(url
            .openStream()));
        String line;
        while((line = br.readLine()) != null) {
            if(PRINT_LINES) {
                System.out.println(line);
            } else {
                if(line.indexOf("ALT=\"Today's Dilbert Comic\"") != -1) {
                    int offset = line
                        .indexOf("<IMG SRC=\"/comics/dilbert/archive/images/dilbert");
                    line = line.substring(offset + 10);
                    return "http://www.dilbert.com"
                        + line.substring(0, line.indexOf('"'));
                }
            }
        }
        return null;
    }

    /**
     * Gets the URL for the Dilbert strip. This version looks at
     * "http://www.dilbert.com/comics/dilbert/archive/". It is not there now and
     * gives a 404.
     * 
     * @return
     * @throws IOException
     */
    public static String todaysDilbert2() throws IOException {
        // Open up the archive
        URL url = new URL("http://www.dilbert.com/comics/dilbert/archive/");
        String regex = "/comics/dilbert/archive/images/(\\w+).gif";
        Pattern pattern = Pattern.compile(regex);
        String retVal = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(url
            .openStream()));
        String line;
        while((line = br.readLine()) != null) {
            if(PRINT_LINES) {
                System.out.println(line);
            } else {
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()) {
                    retVal = "http://www.dilbert.com" + matcher.group();
                    System.out.println(retVal);
                    return retVal;
                }
            }
        }
        return retVal;
    }

    /**
     * Write the given URL into the given File.
     * 
     * @param url
     * @param file
     * @throws IOException
     */
    public static void download(URL url, File file) throws IOException {
        InputStream in = url.openStream();
        FileOutputStream out = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int len;
        while((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
    }

    /**
     * Main routine.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Looking for today's Dilbert comic...");
        String imageUrl = todaysDilbert();

        if(imageUrl == null) {
            System.out.println("Could not find today's Dilbert comic!");
        } else {
            System.out.println("Found the image at\n  " + imageUrl);
            URL url = new URL(imageUrl);

            // we could download the comic to a local file like this:
            // download(url, new File("todaydilbert.gif"));

            // Instead, we are simply going to download it as an ImageIcon
            // and show it in a JFrame.

            System.out.println("Downloading the Image...");
            ImageIcon im = new ImageIcon(url);

            JFrame frame = new JFrame("Today's Dilbert");
            frame.getContentPane().add(new JLabel(im));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Center it
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("\nAll done");
        }
    }
}

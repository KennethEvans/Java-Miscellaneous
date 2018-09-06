package misc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * ParseKindleNotes extracts text from Kindle notes.<br>
 * <br>
 * 
 * The Kindle file, My Clippingd.txt, is is in UTF-8 BOM.<br>
 * It can be read in Notepad, Notepad++, TextPad, and LibreOffice with special
 * characters ok.<br>
 * <br>
 * The output file is now in UTF-8 BOM.<br>
 * It can be read in Notepad, Notepad++, TextPad, and LibreOffice with special
 * characters ok.<br><br>
 * If the BOM byte were not written, then<br>
 * It can be read in Notepad and Notepad++ with special characters ok.<br>
 * It cannot be read in TextPad or Libreoffice with special characters ok.<br>
 * However, if converted in Notepad++ to UTF-8 BOM, then<br>
 * It can be read in Notepad, Notepad++, TextPad, and LibreOffice with special
 * 
 * @author Kenneth Evans, Jr.
 */
public class ParseKindleNotes
{
    private static final String dir = "C:/Scratch/AAA/Kindle/";
    private static final String title = "The Four Pillars of Investing";
    private static final String inName = dir + "My Clippings.txt";
    private static final String outName = dir + title + " Notes.txt";
    private static final String separator = "==========";

    /** Notes will be taken from this book. */

    /**
     * @param args
     */
    public static void main(String[] args) {
        fix();
    }

    public static void fix() {
        int lineNum = 0, itemsFound = 0;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                new FileInputStream(inName), StandardCharsets.UTF_8));
            out = new PrintWriter(outName, StandardCharsets.UTF_8);
            // This makes it BOM
            // (Must out.print, not out.write)
            out.print("\uFEFF");
            out.flush();
            // in = new BufferedReader(new FileReader(inName));
            // out = new PrintWriter(new FileWriter(outName));
            String line, line0 = "==========";
            while((line = in.readLine()) != null) {
                lineNum++;
                if(line.startsWith(title) && line0.equals(separator)) {
                    line = in.readLine();
                    line = in.readLine();
                    line = in.readLine();
                    out.println(line);
                    out.println();
                    itemsFound++;
                }
                line0 = line;
            }
            in.close();
            out.close();
            System.out.println("Input: " + inName);
            System.out.println("Output: " + outName);
            System.out.println("Lines processed: " + lineNum);
            System.out.println("Items found: " + itemsFound);
        } catch(Exception ex) {
            System.err.println("Error at line " + lineNum);
            ex.printStackTrace();
        }
    }

}

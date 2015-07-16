package misc;

import java.util.Scanner;

/*
 * Created on Jul 29, 2012
 * By Kenneth Evans, Jr.
 */

public class ByteUtils
{
    /**
     * Converts an InputStream to a single String. Works because Scanner
     * iterates over tokens in the stream, and in this case we separate tokens
     * using "beginning of the input boundary" (\A) thus giving us only one
     * token for the entire contents of the stream.
     * 
     * @param is
     * @return
     */
    public static String convertStreamToString(java.io.InputStream is) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(is);
            return scanner.useDelimiter("\\A").next();
        } catch(java.util.NoSuchElementException ex) {
            return "";
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }

}

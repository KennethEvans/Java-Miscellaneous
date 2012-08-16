package misc;

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
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch(java.util.NoSuchElementException ex) {
            return "";
        }
    }

}

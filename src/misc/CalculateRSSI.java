package misc;

/*
 * Created on Apr 14, 2016
 * By Kenneth Evans, Jr.
 */

public class CalculateRSSI
{
    private static final int MIN_RSSI = -100;
    // private static final int MAX_RSSI = -55; // Value used by Android
    private static final int MAX_RSSI = -63;
    private static final int MAXMAX_RSSI = -63;;
    public static final String LS = System.getProperty("line.separator");

    public static int calculateSignalLevel(int rssi, int numLevels) {
        if(rssi <= MIN_RSSI) {
            return 0;
        } else if(rssi >= MAXMAX_RSSI) {
            return numLevels - 1;
        } else {
            float inputRange = (MAX_RSSI - MIN_RSSI);
            float outputRange = (numLevels - 1);
            if(inputRange != 0) return (int)((float)(rssi - MIN_RSSI)
                * outputRange / inputRange);
        }
        return 0;
    }

    public static float calculateDecimalSignalLevel(int rssi, int numLevels) {
        if(rssi <= MIN_RSSI) {
            return 0;
        } else if(rssi >= MAXMAX_RSSI) {
            return numLevels - 1;
        } else {
            float inputRange = (MAX_RSSI - MIN_RSSI);
            float outputRange = (numLevels - 1);
            if(inputRange != 0)
                return ((float)(rssi - MIN_RSSI) * outputRange / inputRange);
        }
        return 0;
    }

    public static void main(String[] args) {
        for(int rssid = 0; rssid >= -100; rssid--) {
            System.out.printf("%4d, %10d, %10.3f" + LS, rssid,
                calculateSignalLevel(rssid, 5),
                calculateDecimalSignalLevel(rssid, 5));
        }
    }

}

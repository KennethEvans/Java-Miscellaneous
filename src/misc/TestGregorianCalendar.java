package misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/*
 * Created on May 16, 2011
 * By Kenneth Evans, Jr.
 */

public class TestGregorianCalendar
{
    private static final long defaultTimeSec = 981401195599l;
    private static final SimpleDateFormat formatter1 = new SimpleDateFormat(
        "MM/dd/yyyy hh:mm:ss a");
    private static final SimpleDateFormat formatter2 = new SimpleDateFormat(
        "MM/dd/yyyy HH:mm:ss");

    /**
     * @param args
     */
    public static void main(String[] args) {
        GregorianCalendar gcal = new GregorianCalendar(
            TimeZone.getTimeZone("GMT"));
        gcal.setTimeInMillis(defaultTimeSec);
        String time1, time2, time3;
        for(int offset = 0; offset < 25; offset++) {
            gcal.add(GregorianCalendar.SECOND, 3600);
            time1 = formatter1.format(gcal.getTime());
            time2 = formatter2.format(gcal.getTime());
            time3 = String.format("%02d/%02d/%04d %02d:%02d:%02d",
                gcal.get(GregorianCalendar.MONTH) + 1,
                gcal.get(GregorianCalendar.DAY_OF_MONTH),
                gcal.get(GregorianCalendar.YEAR),
                gcal.get(GregorianCalendar.HOUR_OF_DAY),
                gcal.get(GregorianCalendar.MINUTE),
                gcal.get(GregorianCalendar.SECOND));

            System.out.printf("%2d   %s   %s   %s\n", offset, time1, time2,
                time3);
        }

        // Test formatting
        System.out.println();
        for(int i = 0; i < 25; i++) {
            Date date = new Date(i * 3600 * 1000);
            final SimpleDateFormat formatter = new SimpleDateFormat(
                "HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            time1 = formatter.format(date);
            System.out.printf("%2d   %s\n", i, time1);
        }
    }

}

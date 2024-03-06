package hcmute.kltn.vtv.util;



import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}

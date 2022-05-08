package fit.wenchao.second_hand_trading_platform_front.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getCurrentDateString(String formatPattern) {

        Date currentTime = new Date();

        System.out.println(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getCurrentDateString() {
        return getCurrentDateString("yyyy-MM-dd HH:mm:ss");
    }

}
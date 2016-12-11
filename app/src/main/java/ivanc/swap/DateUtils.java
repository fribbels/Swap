package ivanc.swap;

/**
 * Created by ivanc on 12/10/2016.
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateUtils {

    public static String getTimestamp() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String convertISO8601ToTimeAgo(String date) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date source;
        try {
            source = sourceFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        destFormat.setTimeZone(TimeZone.getDefault());
        String timezoned_date = destFormat.format(source);

        SimpleDateFormat timezonedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        long minutesAgo;
        try {
            Date past = timezonedFormat.parse(timezoned_date);
            Date now = new Date();
            minutesAgo = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            minutesAgo = -1;
        }

        String res;
        long timeAgo;
        if (minutesAgo >= 1440) {
            timeAgo = minutesAgo / (60 * 24);
            res = "" + timeAgo + " day";
        } else if (minutesAgo >= 60) {
            timeAgo = minutesAgo / 60;
            res = "" + timeAgo + " hour";
        } else {
            timeAgo = minutesAgo;
            res = "" + timeAgo + " minute";
        }
        if (timeAgo == 1) {
            return res + " ago";
        } else {
            return res + "s ago";
        }
    }
}
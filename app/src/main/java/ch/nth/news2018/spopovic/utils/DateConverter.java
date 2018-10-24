package ch.nth.news2018.spopovic.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    private static final int SECOND = 1000;
    private static final int MINUTE = SECOND * 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;
    private static final int WEEK = DAY * 7;

    public static String convertDateStringToPastTimeString(String s) {
        Date date = null;
        try {
            date = (Date) formatter.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return s;
        }

        long currentTime = new Date().getTime();
        long articleTime = date.getTime();
        long timePassed = currentTime - articleTime;
        int days    = (int)((timePassed % WEEK) / DAY);
        int hours   = (int)((timePassed % DAY) / HOUR);
        int minutes = (int)((timePassed % HOUR) / MINUTE);

        return String.valueOf(days + " d " + hours +" h "+ minutes + " m");
    }
}

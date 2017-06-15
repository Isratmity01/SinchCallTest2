package com.example.isjahan.sinchcalltest.utility;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shadman.rahman on 15-Jun-17.
 */

public class DateTimeUtility {




    public static String getFormattedDateFromTimestamp(long millis)
    {
        String dateFormat = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return simpleDateFormat.format(calendar.getTime());

    }

    public static String getFormattedDateWithOutYearFromTimeStamp(long millis){
        String dateFormat = "dd MMM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return simpleDateFormat.format(calendar.getTime());
    }

    private static boolean isToday(long millis){
        if(getFormattedDateFromTimestamp(millis).equals(getFormattedDateFromTimestamp(System.currentTimeMillis())))
            return true;
        return false;
    }

    private static String returnPrettyTime(long millis){
        PrettyTime prettyTime = new PrettyTime();
        String time =  prettyTime.format(new Date(millis));

        if(time.contains("hours")){
            time = time.replace("hours", "hrs");
        }

        if(time.contains("hour")){
            time = time.replace("hour", "hrs");
        }

        if(time.contains("minute")){
            time = time.replace("minute", "min");
        }

        if(time.contains(" mins")){
            time = time.replace("mins", "min");
        }

        return time;
    }

    public static String returnRelativeTime(long millis){
        if(isToday(millis)){
            return returnPrettyTime(millis);
        }

        else
            return BanglaConvert.convertBangla(getFormattedTimeFromTimestamp(millis));

    }

    public static String returnOnlineStatus(long millis, long statusCode){
        if(statusCode == 1L){
            return "Online";
        }
        else{
            if(timeDiff(millis))
                return "Last seen "+returnRelativeDate(millis)+" "+returnRelativeTime(millis);
            else
                return "Offline";

        }
    }

    public static String returnRelativeDate(long millis){
        if(isToday(millis))
            return "আজ";
        else
            return BanglaConvert.convertBangla(getFormattedDateFromTimestamp(millis));
    }

    public static boolean timeDiff(long millis){
        if((System.currentTimeMillis() - millis) < 300000L){
            return true;
        }
        else return false;
    }

    public static boolean isSameDay(long millis_1, long millis_2){
        if (getFormattedDateFromTimestamp(millis_1).equals(getFormattedDateFromTimestamp(millis_2)))
            return true;
        else return false;
    }

    public static String getFormattedTimeFromTimestamp(long millis)
    {
        Date date = new Date();
        date.setTime(millis);
        String twentyFourHourTime = new SimpleDateFormat("HH").format(date);
        String banglaTime = BanglaConvert.getBanglaTime(twentyFourHourTime);
        String formattedTime = new SimpleDateFormat("hh:mm").format(date);
        String banglaTimeValues = BanglaConvert.convertBangla(formattedTime);
        return banglaTime+" "+banglaTimeValues;
    }

    public static String returnTime(long millis){
        if(isToday(millis)) {
            String eng = returnPrettyTime(millis);
            return BanglaConvert.convertBangla(eng);
        }
        else {
            String eng = getFormattedTimeFromTimestamp(millis);
            return eng;
        }
    }


}
package com.workfront.internship.event_management.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hermine Turshujyan 8/26/16.
 */
public class DateParser {

    public static Date parseStringToDate(String dateString, String timeString) {
        if (timeString.isEmpty()) {
            timeString = "00:00";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        String dateTimeString = dateString + " " + timeString;
        Date parsedDate;
        try {
            parsedDate = formatter.parse(dateTimeString);
        } catch (ParseException e) {
            throw new RuntimeException();
        }

        return parsedDate;
    }

    public static String parseDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    public static String getDateStringFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String getTimeStringFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }
}

package com.workfront.internship.event_management.controller.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hermine Turshujyan 8/26/16.
 */
public class DateParser {

    public static Date parseStringToDate(String dateString, String timeString) {
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


}

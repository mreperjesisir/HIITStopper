package com.example.hiitstopper;

/**
 * Utility class used for converting time units
 * for the UI
 */

public class TimerUtilities {

    /**
     * Converts time from milliseconds to a String for the UI to
     * display in the timer
     *
     * @param millisUntilFinished Time remaining in long format
     * @return String format of the remaining time
     */

    public static String getFormattedTime(long millisUntilFinished){

        int minutes = (int) millisUntilFinished / 1000 / 60;
        int seconds = (int) millisUntilFinished / 1000 % 60;

        String secondsWithZero = String.valueOf(seconds);
        if (seconds<10){
            secondsWithZero = "0" + seconds;
        }
        String formattedTime = minutes + ":" + secondsWithZero + "\u00A0";
        return formattedTime;
    }
}

package com.example.hiitstopper;

public class TimerUtilities {

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

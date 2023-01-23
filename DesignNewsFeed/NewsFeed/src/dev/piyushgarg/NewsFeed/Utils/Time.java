package dev.piyushgarg.NewsFeed.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDate;


public class Time {
    static PrettyTime prettyTime = new PrettyTime();

    public static String toHumanize(LocalDate date) {
        return prettyTime.format(date);
    }
}

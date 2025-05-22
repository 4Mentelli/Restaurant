package it.unibs.se.model;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

public class Date {

    private final int day;
    private final int month;
    private final int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String date) {
        String[] dateArray = date.split("/");
        this.day = Integer.parseInt(dateArray[0]);
        this.month = Integer.parseInt(dateArray[1]);
        this.year = Integer.parseInt(dateArray[2]);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getString() {
        if (month < 10) {
            if (day < 10) {
                return "0" + day + "/0" + month + "/" + year;
            } else return day + "/0" + month + "/" + year;
        }
        return day + "/" + month + "/" + year;
    }

    public boolean isAfter(Date date) {
        if (this.year != date.year) {
            return this.year > date.year;
        }
        if (this.month != date.month) {
            return this.month > date.month;
        }
        return this.day > date.day;
    }

    public boolean isBefore(Date date) {
        if (this.year != date.year) {
            return this.year < date.year;
        }
        if (this.month != date.month) {
            return this.month < date.month;
        }
        return this.day < date.day;
    }

    public boolean isBetween(Date start, Date end) {
        return !this.isBefore(start) && !this.isAfter(end);
    }

    public boolean isInCurrentWeek() {
        LocalDate date = LocalDate.of(this.year, this.month, this.day);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.now().get(weekFields.weekOfWeekBasedYear()) == date.get(weekFields.weekOfWeekBasedYear());
    }

    public boolean isInWeekend() {
        LocalDate date = LocalDate.of(this.year, this.month, this.day);
        return (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7);
    }

    public boolean isHoliday(List<Date> holidays) {
        for (Date holiday : holidays) {
            if (this.equals(holiday)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExpired(Date now) {
        return (now.isAfter(this) || now.equals(this));
    }

    public boolean isTooFar() {
        return this.getYear() > 2026;
    }

}

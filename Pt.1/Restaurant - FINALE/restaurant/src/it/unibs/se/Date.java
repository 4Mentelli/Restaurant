package it.unibs.se;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;

public class Date {

    private int day;
    private int month;
    private int year;
    private int day_of_the_week;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.day_of_the_week = dayOfTheWeek();
    }

    public Date(String date) {
        String[] dateArray = date.split("/");
        this.day = Integer.parseInt(dateArray[0]);
        this.month = Integer.parseInt(dateArray[1]);
        this.year = Integer.parseInt(dateArray[2]);
        this.day_of_the_week = dayOfTheWeek();
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

    public int getDay_of_the_week() {
        return day_of_the_week;
    }

    public int dayOfTheWeek() {
        // Imposta la data
        Calendar calendario = Calendar.getInstance();
        calendario.set(year, month, day); // L'anno, il mese (partendo da 0) e il giorno

        // Ottieni il giorno della settimana
        return calendario.get(Calendar.DAY_OF_WEEK);
    }

    public String toString() {
        if (month < 10) {
            if (day < 10) {
                return "0" + day + "/0" + month + "/" + year;
            } else return day + "/0" + month + "/" + year;
        } else
            return day + "/" + month + "/" + year;
    }

    public Date stringToDate(String date) {
        String[] dateArray = date.split("/");
        return new Date(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[0]));
    }

    public boolean after(Date date, boolean print) {
        if (this.year > date.year) {
            if (print)
                System.out.println("La data di inizio è successiva alla data di fine. Riprova.");
            return true;
        } else if (this.year == date.year) {
            if (this.month > date.month) {
                if (print)
                    System.out.println("La data di inizio è successiva alla data di fine. Riprova.");
                return true;
            } else if (this.month == date.month) {
                if (this.day > date.day) {
                    if (print)
                        System.out.println("La data di inizio è successiva alla data di fine. Riprova.");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean between(Date start, Date end) {
        if (this.year > start.year && this.year < end.year) {
            return true;
        } else if (this.year == start.year && this.year < end.year) {
            if (this.month > start.month) {
                return true;
            } else if (this.month == start.month) {
                if (this.day >= start.day) {
                    return true;
                }
            }
        } else if (this.year > start.year && this.year == end.year) {
            if (this.month < end.month) {
                return true;
            } else if (this.month == end.month) {
                if (this.day <= end.day) {
                    return true;
                }
            }
        } else if (this.year == start.year && this.year == end.year) {
            if (this.month > start.month && this.month < end.month) {
                return true;
            } else if (this.month == start.month && this.month < end.month) {
                if (this.day >= start.day) {
                    return true;
                }
            } else if (this.month > start.month && this.month == end.month) {
                if (this.day <= end.day) {
                    return true;
                }
            } else if (this.month == start.month && this.month == end.month) {
                if (this.day >= start.day && this.day <= end.day) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCurrentWeek() {
        LocalDate date = LocalDate.of(this.year, this.month, this.day);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return LocalDate.now().get(weekFields.weekOfWeekBasedYear()) == date.get(weekFields.weekOfWeekBasedYear());
    }
}

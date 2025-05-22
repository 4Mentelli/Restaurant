package it.unibs.se.model;

import java.util.List;

public interface ReservationReader {
    int[] parametersReader();
    List<Recipe> recipesReader();
    List<Menu> themedMenusReader();
    List<Date> holidaysReader();
    List<Reservation> reservationsReader();
}

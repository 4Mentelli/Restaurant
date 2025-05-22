package it.unibs.se.model;

public interface ReservationUpdater {
    void reservationWriter(Reservation reservation) throws Exception;
    void deleteElement(String type, int id) throws Exception;
}

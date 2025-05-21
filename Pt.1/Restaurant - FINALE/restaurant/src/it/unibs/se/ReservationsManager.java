package it.unibs.se;

import it.unibs.fp.mylib.InputDati;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static it.unibs.se.Reservation.deleteReservation;
import static it.unibs.se.Xml.*;

public class ReservationsManager {

    public static void initialize(){
        int option;
        List<Date> holidays = holidaysReader();

        do{
            List<Reservation> reservations = reservationReader();



            System.out.println("\n" + LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toUpperCase());
            System.out.println("1 - Visualizza Prenotazioni");
            System.out.println("2 - Aggiungi Prenotazione");
            System.out.println("3 - Cancella Prenotazione");
            System.out.println("0 - Menu principale");

            option = InputDati.leggiIntero("Scelta: ", 0, 3);
            System.out.print("\n");

            switch (option) {
                case 1 -> {
                    for (Reservation reservation : reservations)
                        reservation.printReservation();
                    Restaurant.waitComand();
                }
                case 2 -> {
                    try {
                        reservationWriter(reservations, holidays);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> deleteReservation(reservations);
            }

        }while (option != 0);

    }
}

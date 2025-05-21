package it.unibs.se;

import it.unibs.fp.mylib.InputDati;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;

public class Restaurant {

    public static void main(String[] args) throws Exception {

        int option;
        do {
            System.out.println(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toUpperCase());
            System.out.println("1 - GESTORE");
            System.out.println("2 - ADDETTO ALLE PRENOTAZIONI");
            System.out.println("3 - MAGAZZINIERE");
            System.out.println("0 - ESCI");
            option = InputDati.leggiIntero("Scegli: ", 0, 3);


            switch (option) {
                case 0 -> System.out.println("Arrivederci!");
                case 1 -> Manager.initialize();
                case 2 -> ReservationsManager.initialize();
                case 3 -> StoreManager.initialize();
            }
        } while (option != 0);
    }

    public static void waitComand() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Premi INVIO per continuare");
        scanner.nextLine();
        System.out.println("\n\n\n");
    }
}

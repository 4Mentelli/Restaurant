package it.unibs.se.view;

import it.unibs.fp.mylib.InputDati;
import it.unibs.se.model.Menu;
import it.unibs.se.model.Recipe;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;

public interface View {

    void showMenu();

    default void showMessage(String message) {
        System.out.println(message);
    }

    default void showCurrentDate() {
        newLine();
        showMessage(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)).toUpperCase());
    }

    default void showSuccessfulOperationMessage() {
        showMessage("Operazione conclusa con successo!");
    }

    default void showNoIDMatchMessage() {
        showMessage("Nessun elemento corrisponde all'id!");
    }

    default void printAllThemedMenuInfo(Menu menu) {
        newLine();
        showMessage("MENU " + menu.getId());
        showMessage("Nome: " + menu.getName());
        showMessage("Piatti:");
        for (Recipe piatto : menu.getDishes()) {
            showMessage("- " + piatto.getName());
        }
        if (menu.getStartDate().isAfter(menu.getEndDate()))
            showMessage("Il menù non è disponibile");
        else
            showMessage("Dal " + menu.getStartDate().getString() + " al " + menu.getEndDate().getString());
        newLine();
    }

    default int readOption(int minimum, int maximum) {
        return InputDati.leggiIntero("Scegli: ", minimum, maximum);
    }

    default int readIdToDelete() {
        newLine();
        return InputDati.leggiInteroConMinimo("Inserisci l'id da eliminare: ", 0);
    }

    default void waitForEnter() {
        Scanner scanner = new Scanner(System.in);
        showMessage("Premi INVIO per continuare");
        scanner.nextLine();
    }

    default void newLine() {
        System.out.println();
    }

}

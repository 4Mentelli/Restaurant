package it.unibs.se.view;

public class RestaurantView implements View {

    @Override
    public void showMenu() {
        showMessage("1 - GESTORE");
        showMessage("2 - ADDETTO ALLE PRENOTAZIONI");
        showMessage("3 - MAGAZZINIERE");
        showMessage("0 - ESCI");
    }

    public void showGoodbyeMessage() {
        showMessage("Arrivederci!");
    }

}


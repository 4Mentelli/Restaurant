package it.unibs.fp.mylib;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.datatype.DatatypeConfigurationException;


public class InputDati {
	private static Scanner lettore = creaScanner();

	private final static String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
	private final static String ERRORE_MINIMO = "Attenzione: e' richiesto un valore maggiore o uguale a ";
	private final static String ERRORE_STRINGA_VUOTA = "Attenzione: non hai inserito alcun carattere";
	private final static String ERRORE_MASSIMO = "Attenzione: e' richiesto un valore minore o uguale a ";
	private final static String ERRORE_DATA_NON_VALIDA = "Attenzione: la data scelta non � valida";
	private final static String MESSAGGIO_AMMISSIBILI = "Attenzione: i caratteri ammissibili sono: ";
	private final static char RISPOSTA_SI = 'S';
	private final static char RISPOSTA_NO = 'N';
	  
	private static Scanner creaScanner() {
		Scanner creato = new Scanner(System.in);
		// creato.useDelimiter(System.getProperty("line.separator"));
		return creato;
	}

	public static String leggiStringa (String messaggio){
		System.out.print(messaggio);
		return lettore.next();
	}

	//chiede input di una stringa non vuota
	public static String leggiStringaNonVuota(String messaggio){
		boolean finito=false;
		String lettura = null;
		do{
			System.out.print(messaggio);
			lettura = lettore.nextLine();
			//elimina spazi iniziali e finali, ed elimina spazi/tab duplicati
			lettura = lettura.trim().replaceAll("( |\t)+", " ");
			if (lettura.length() > 0)
				finito=true;
			else
				System.out.println(ERRORE_STRINGA_VUOTA);
		} while (!finito);
		return lettura;
	}
	
	//accetta nomi che contengono solo lettere
	public static String leggiStringaDiLettere(String messaggio)
	{
		boolean finito=false;
		String lettura = null;
		do
		{
			System.out.print(messaggio);
			lettura = lettore.nextLine();
			//elimina spazi iniziali e finali, ed elimina spazi/tab duplicati
			lettura = lettura.trim().replaceAll("( |\t)+", " ");
			if (lettura.matches(("[a-zA-Z\s]+"))) {
				finito = true;
			} else {
				System.out.println("Il nome deve contenere solo lettere e non deve essere vuota.");
			}
		} while (!finito);

		return lettura;
	}
	  
	public static char leggiChar (String messaggio)
	{
		boolean finito = false;
		char valoreLetto = '\0';
		do
		{
			System.out.print(messaggio);
			String lettura = lettore.next();
			String daButtare = lettore.nextLine();
			daButtare.trim();
			if(!(daButtare.isBlank()) || lettura.length() != 1) {
				System.out.println(ERRORE_FORMATO);
			} else {
				valoreLetto = lettura.charAt(0);
				finito = true;
			}
		} while (!finito);
		return valoreLetto;
	}
	  
	public static char leggiUpperChar (String messaggio, String ammissibili)
	{
		boolean finito = false;
		char valoreLetto = '\0';
		do
		{
			valoreLetto = leggiChar(messaggio);
			valoreLetto = Character.toUpperCase(valoreLetto);
			if (ammissibili.indexOf(valoreLetto) != -1)
				finito  = true;
			else
				System.out.println(MESSAGGIO_AMMISSIBILI + ammissibili);
		} while (!finito);
		return valoreLetto;
	}
	  
	public static int leggiIntero(String messaggio) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			System.out.print(messaggio);
			try {
				valoreLetto = lettore.nextInt();
				String daButtare = lettore.nextLine();
				daButtare.trim();
				if (!(daButtare.isBlank())) {
					System.out.println(ERRORE_FORMATO);
					finito = false;
				} else {
					finito = true;
				}
			} catch (InputMismatchException e) {
				System.out.println(ERRORE_FORMATO);
				String daButtare = lettore.nextLine();
			}
		} while (!finito);
		return valoreLetto;
	}

	public static LocalDate leggiData(String messaggio) {
		boolean finito = false;
		LocalDate data = null;
		int giorno, mese, anno;
		do {
			System.out.println(messaggio);
			try {
				giorno = leggiInteroConMinimo("Giorno: ", 1);
				mese = leggiInteroConMinimo("Mese: ", 1);
				anno = leggiIntero("Anno: ");
				data = LocalDate.of(anno, mese, giorno);
				finito = true;
			} catch (DateTimeException e) {
				System.out.println(ERRORE_DATA_NON_VALIDA);
			}
		} while (!finito);
		return data;
	}

	public static int leggiInteroPositivo(String messaggio) {
		return leggiInteroConMinimo(messaggio, 1);
	}

	public static int leggiInteroNonNegativo(String messaggio) {
		return leggiInteroConMinimo(messaggio, 0);
	}

	public static int leggiInteroConMinimo(String messaggio, int minimo) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			valoreLetto = leggiIntero(messaggio);
			if (valoreLetto >= minimo)
				finito = true;
			else
				System.out.println(ERRORE_MINIMO + minimo);
		} while (!finito);

		return valoreLetto;
	}
	  
	public static int leggiIntero(String messaggio, int minimo, int massimo) {
		boolean finito = false;
		int valoreLetto = 0;
		do {
			valoreLetto = leggiIntero(messaggio);
			if (valoreLetto >= minimo && valoreLetto <= massimo)
				finito = true;
			else if (valoreLetto < minimo)
				System.out.println(ERRORE_MINIMO + minimo);
			else
				System.out.println(ERRORE_MASSIMO + massimo);
		} while (!finito);

		return valoreLetto;
	}

	public static double leggiDouble(String messaggio) {
		boolean finito = false;
		double valoreLetto = 0;
		do {
			System.out.print(messaggio);
			try {
				valoreLetto = lettore.nextDouble();
				String daButtare = lettore.nextLine();
				if (!(daButtare.isBlank())) {
					System.out.println(ERRORE_FORMATO);
					finito = false;
				} else {
					finito = true;
				}
			} catch (InputMismatchException e) {
				System.out.println(ERRORE_FORMATO);
				String daButtare = lettore.next();
			}
		} while (!finito);
		return valoreLetto;
	}

	public static double leggiDoubleConMinimo(String messaggio, double minimo) {
		boolean finito = false;
		double valoreLetto = 0;
		do {
			valoreLetto = leggiDouble(messaggio);
			if (valoreLetto >= minimo)
				finito = true;
			else
				System.out.println(ERRORE_MINIMO + minimo);
		} while (!finito);

		return valoreLetto;
	}

	public static float leggiFloat(String messaggio) {
		boolean finito = false;
		float valoreLetto = 0;
		do {
			System.out.print(messaggio);
			try {
				valoreLetto = lettore.nextFloat();
				String daButtare = lettore.nextLine();
				if (!(daButtare.isBlank())) {
					System.out.println(ERRORE_FORMATO);
					finito = false;
				} else {
					finito = true;
				}
			} catch (InputMismatchException e) {
				System.out.println(ERRORE_FORMATO);
				String daButtare = lettore.next();
			}
		} while (!finito);
		return valoreLetto;
	}

	public static float leggiFloatConMinimo(String messaggio, float minimo) {
		boolean finito = false;
		float valoreLetto = 0;
		do {
			valoreLetto = leggiFloat(messaggio);
			if (valoreLetto >= minimo)
				finito = true;
			else
				System.out.println(ERRORE_MINIMO + minimo);
		} while (!finito);

		return valoreLetto;
	}

	public static float leggiFloatPositivo(String messaggio) {
		return leggiFloatConMinimo(messaggio, 0.001f);
	}

	public static boolean yesOrNo(String messaggio) {
		String mioMessaggio = messaggio + "(" + RISPOSTA_SI + "/" + RISPOSTA_NO + ") ";
		char valoreLetto = leggiUpperChar(mioMessaggio, String.valueOf(RISPOSTA_SI) + String.valueOf(RISPOSTA_NO));

		if (valoreLetto == RISPOSTA_SI)
			return true;
		else
			return false;
	}

}

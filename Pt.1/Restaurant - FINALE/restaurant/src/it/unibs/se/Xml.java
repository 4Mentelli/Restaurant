package it.unibs.se;

import it.unibs.fp.mylib.InputDati;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xml {

    private static final String RESERVATIONS_PATH = "restaurant/src/it/unibs/xml/Reservations.xml";
    private static final String RECIPES_PATH = "restaurant/src/it/unibs/xml/Ricettario.xml";
    private static final String DRINKS_PATH = "restaurant/src/it/unibs/xml/Drinks.xml";
    private static final String EXTRAS_PATH = "restaurant/src/it/unibs/xml/Extras.xml";
    private static final String HOLIDAYS_PATH = "restaurant/src/it/unibs/xml/Holidays.xml";
    private static final String TEMATIC_MENU_PATH = "restaurant/src/it/unibs/xml/TematicMenu.xml";
    private static final String STORAGE_PATH = "restaurant/src/it/unibs/xml/Storage.xml";

    public static List<Recipe> recipeReader() {
        File xmlFile = new File(RECIPES_PATH);
        List<Recipe> recipes = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList recipeList = document.getElementsByTagName("recipe");

            for (int i = 0; i < recipeList.getLength(); i++) {
                Element recipe = (Element) recipeList.item(i);
                String name = recipe.getAttribute("name");
                int id = Integer.parseInt(recipe.getAttribute("id"));
                int workload = Integer.parseInt(recipe.getAttribute("workload"));
                Date start_date = new Date(recipe.getAttribute("period").split(" - ")[0]);
                Date end_date = new Date(recipe.getAttribute("period").split(" - ")[1]);

                NodeList ingredientList = recipe.getElementsByTagName("ingredient");
                List<Ingredient> ingredients = new ArrayList<>();

                for (int j = 0; j < ingredientList.getLength(); j++) {
                    Element ingredient = (Element) ingredientList.item(j);
                    String ingredient_name = ingredient.getAttribute("name");
                    float quantity = Float.parseFloat(ingredient.getAttribute("quantity"));
                    String unit = ingredient.getAttribute("unit");
                    ingredients.add(new Ingredient(ingredient_name, quantity, unit));
                }

                recipes.add(new Recipe(name, id, workload, ingredients, start_date, end_date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public static List<Ingredient> xmlReader(boolean drink) {
        File xmlFile = new File(DRINKS_PATH);
        if (!drink) xmlFile = new File(EXTRAS_PATH);
        List<Ingredient> ingredients = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList ingredientList = document.getElementsByTagName("drink");
            if (!drink) ingredientList = document.getElementsByTagName("extra");

            for (int j = 0; j < ingredientList.getLength(); j++) {
                Element ingredient = (Element) ingredientList.item(j);
                String ingredient_name = ingredient.getAttribute("name");
                float quantity = Float.parseFloat(ingredient.getAttribute("quantity"));
                int id = Integer.parseInt(ingredient.getAttribute("id"));
                ingredients.add(new Ingredient(ingredient_name, quantity, "pezzi", id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public static List<Date> holidaysReader() {
        File xmlFile = new File(HOLIDAYS_PATH);
        List<Date> holidays = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList holidayList = document.getElementsByTagName("holiday");

            for (int i = 0; i < holidayList.getLength(); i++) {
                Element holiday = (Element) holidayList.item(i);
                String date = holiday.getAttribute("date");
                holidays.add(new Date(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holidays;
    }

    public static List<Menu> menuReader() {
        File xmlFile = new File(TEMATIC_MENU_PATH);
        List<Menu> tematic_menu = new ArrayList<>();

        //Legge tutte le ricette per aggiungerle poi ai menu
        List<Recipe> recipes = recipeReader();


        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList menu_list = document.getElementsByTagName("menu");

            for (int i = 0; i < menu_list.getLength(); i++) {
                Element menu = (Element) menu_list.item(i);
                String name = menu.getAttribute("name");
                int id = Integer.parseInt(menu.getAttribute("id"));
                int workload = Integer.parseInt(menu.getAttribute("workload"));

                NodeList dishes_list = menu.getElementsByTagName("dish");
                List<Recipe> dishes = new ArrayList<>();

                for (int j = 0; j < dishes_list.getLength(); j++) {

                    Element dish = (Element) dishes_list.item(j);
                    //Cerca la ricetta con lo stesso nome e la aggiunge al menu
                    for (Recipe recipe : recipes) {
                        if (recipe.getName().equals(dish.getAttribute("name"))) {
                            dishes.add(recipe);
                        }
                    }
                }
                tematic_menu.add(new Menu(name, dishes, id, workload));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tematic_menu;
    }

    public static List<Ingredient> storageReader() {
        File xmlFile = new File(STORAGE_PATH);
        List<Ingredient> storage = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList ingredientList = document.getElementsByTagName("ingredient");

            for (int j = 0; j < ingredientList.getLength(); j++) {
                Element ingredient = (Element) ingredientList.item(j);
                String ingredient_name = ingredient.getAttribute("name");
                float quantity = Float.parseFloat(ingredient.getAttribute("quantity"));
                int id = Integer.parseInt(ingredient.getAttribute("id"));
                String unit = ingredient.getAttribute("unit");
                storage.add(new Ingredient(ingredient_name, quantity, unit, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storage;
    }

    public static void recipeWriter() throws Exception {

        // Carica il documento XML dal file esistente
        File xmlFile = new File(RECIPES_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        // Crea la ricetta e assegna tutti gli attributi
        Recipe recipe = new Recipe(InputDati.leggiStringaNonVuota("Inserisci il nome della nuova ricetta: "));
        if (recipeReader().size() == 0)
            recipe.setId(1);
        else
            recipe.setId(recipeReader().get(recipeReader().size() - 1).getId() + 1);
        while (true) {
            String name = InputDati.leggiStringaNonVuota("Inserisci un ingrediente (oppure 'N' per terminare): ");
            if (name.equals("N") || name.equals("n")) break;
            float quantity = InputDati.leggiFloatPositivo("Inserisci la sua quantita: ");
            int unit = InputDati.leggiIntero("1 - grammi\n2 - millilitri\n3 - pezzi\nScegli l'unità di misura: ", 1, 3);
            recipe.addIngredients(new Ingredient(name, quantity, unit));
        }
        recipe.setPeriod(new Date("01/01/2024"), new Date("31/12/2025"));
        if (InputDati.leggiIntero("Il piatto è disponibile per tutto l'anno? (1 - Si; 2 - No): ", 1, 2) == 2) {

            do {
                String start = String.valueOf(InputDati.leggiData("Disponibile da: "));
                Date start_date = new Date(start.split("-")[2] + "/" + start.split("-")[1] + "/" + start.split("-")[0]);
                String end = String.valueOf(InputDati.leggiData("Fino a: "));
                Date end_date = new Date(end.split("-")[2] + "/" + end.split("-")[1] + "/" + end.split("-")[0]);
                recipe.setPeriod(start_date, end_date);
            } while (recipe.getPeriod()[0].after(recipe.getPeriod()[1], true));
        }

        // Crea l'elemento per la nuova ricetta
        Element new_recipe = doc.createElement("recipe");
        new_recipe.setAttribute("name", recipe.getName());
        new_recipe.setAttribute("id", String.valueOf(recipe.getId()));
        new_recipe.setAttribute("workload", String.valueOf(recipe.getIngredients().size() * 2));
        new_recipe.setAttribute("period", recipe.getPeriod()[0].toString() + " - " + recipe.getPeriod()[1].toString());
        new_recipe.appendChild(doc.createTextNode("\n"));

        // Crea gli elementi per gli ingredienti
        List<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            new_recipe.appendChild(doc.createTextNode("\t\t"));
            Element new_ingredient = doc.createElement("ingredient");
            new_ingredient.setAttribute("name", ingredient.getName());
            new_ingredient.setAttribute("quantity", String.valueOf(ingredient.getQuantity()));
            new_ingredient.setAttribute("unit", ingredient.getUnit());
            new_recipe.appendChild(new_ingredient);
            new_recipe.appendChild(doc.createTextNode("\n"));
        }
        new_recipe.appendChild(doc.createTextNode("\t"));
        // Aggiunge la nuova ricetta al documento XML
        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(new_recipe);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

        // Scrive il documento XML aggiornato sul file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

    public static void xmlWriter(boolean drinks) throws Exception {
        // Conta il numero di ricette presenti nel file per assegnare l'id
        int id = 1;
        if (xmlReader(drinks).size() > 0)
            id = xmlReader(drinks).get(xmlReader(drinks).size() - 1).getId() + 1;

        // Carica il documento XML dal file esistente
        File xmlFile = new File(DRINKS_PATH);
        if (!drinks) xmlFile = new File(EXTRAS_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Ingredient ingredient = new Ingredient();
        if (drinks) {
            ingredient.setName(InputDati.leggiStringaNonVuota("Inserisci il nome del nuovo drink: "));
        } else {
            ingredient.setName(InputDati.leggiStringaNonVuota("Inserisci il nome del nuovo extra: "));
        }
        ingredient.setQuantity(InputDati.leggiFloatPositivo("Inserisci la sua quantita in magazzino (in pezzi): "));
        ingredient.setId(id);

        // Crea l'elemento nuovo e lo aggiunge al documento XML
        Element new_ingredient = doc.createElement("drink");
        if (!drinks) {
            new_ingredient = doc.createElement("extra");
        }

        new_ingredient.setAttribute("name", ingredient.getName());
        new_ingredient.setAttribute("id", String.valueOf(id));
        new_ingredient.setAttribute("quantity", String.valueOf(ingredient.getQuantity()));

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(new_ingredient);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

        // Scrive il documento XML aggiornato sul file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

    public static List<Reservation> reservationReader(){

        File xmlFile = new File(RESERVATIONS_PATH);
        List<Reservation> reservations_list = new ArrayList<>();


        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList reservationList = document.getElementsByTagName("reservation");

            for (int i = 0; i < reservationList.getLength(); i++) {

                Map<Integer, Integer> menusMap = null;
                Map<Integer, List<Integer>> dishesMap = null;
                List<Map<Integer, Integer>> menusList = new ArrayList<>();
                List<Map<Integer, List<Integer>>> dishesList = new ArrayList<>();

                Reservation reservation_obj;

                Element reservation = (Element) reservationList.item(i);
                int id = Integer.parseInt(reservation.getAttribute("id"));
                int n_people = Integer.parseInt(reservation.getAttribute("n_people"));
                Date date = new Date(reservation.getAttribute("date"));
                float workload = Float.parseFloat(reservation.getAttribute("workload"));
                reservation_obj = new Reservation(id, n_people, workload, date);

                List<Integer> dishes_list = new ArrayList<>();
                NodeList orders_list = reservation.getElementsByTagName("order");

                for (int j = 0; j < orders_list.getLength(); j++) {
                    //Legge l'attributo n_people dell'ordine
                    Element order = (Element) orders_list.item(j);
                    int n_people_order = Integer.parseInt(order.getAttribute("n_people"));

                    //Controlla se l'ordine contiene un dish o un menu
                    if (order.getElementsByTagName("dish").getLength() > 0) {
                        dishes_list = new ArrayList<>();
                        dishesMap = new HashMap<Integer, List<Integer>>();
                        for (int k = 0; k < order.getElementsByTagName("dish").getLength(); k++) {
                            Element dish = (Element) order.getElementsByTagName("dish").item(k);
                            dishes_list.add(Integer.parseInt(dish.getAttribute("id")));
                        }
                        dishesMap.put(n_people_order, dishes_list);
                        dishesList.add(dishesMap);

                    } else {
                        menusMap = new HashMap<Integer, Integer>();
                        for (int k = 0; k < order.getElementsByTagName("menu").getLength(); k++) {
                            Element menu = (Element) order.getElementsByTagName("menu").item(k);
                            menusMap.put(n_people_order, Integer.parseInt(menu.getAttribute("id")));
                            menusList.add(menusMap);
                        }
                    }
                }

                if (dishesMap != null)
                    reservation_obj.setDishesMap(dishesList);
                if (menusMap != null)
                    reservation_obj.setMenusMap(menusList);

                if (LocalDate.of(reservation_obj.getDate().getYear(), reservation_obj.getDate().getMonth(), reservation_obj.getDate().getDay()).isAfter(LocalDate.now()))
                    reservations_list.add(reservation_obj);
                else
                    elementDeleter(4, id);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations_list;
    }

    public static void reservationWriter(List<Reservation> reservations, List<Date> holidays) throws Exception {

        // Carica il documento XML dal file esistente
        File xmlFile = new File(RESERVATIONS_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        //Conta per assegnare l'id
        int id = 1;
        if (reservations.size() > 0)
            id = reservations.get(reservations.size() - 1).getReservation_id() + 1;

        if (Manager.getN_client() == 0) {
            System.out.println("I parametri del ristorante non sono ancora stati inizializzati dal gestore");
            return;
        } else if (recipeReader().isEmpty()) {
            System.out.println("Non sono presenti ricette nel ricettario");
            return;
        }

        LocalDate date = InputDati.leggiData("Inserisci la data della prenotazione: ");
        String date_string = date.toString().split("-")[2] + "/" + date.toString().split("-")[1] + "/" + date.toString().split("-")[0];
        Date new_date = new Date(date_string);

        // Controlla che ci sia disponibilita' per la data scelta
        if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()) || date.getYear() > 2025) {
            System.out.println("Non e' possibile prenotare per la data scelta");
            return;
        }

        // Controlla che la data scelta non sia una festivita'
        for (Date holiday : holidays) {
            if (holiday.toString().equals(date_string)) {
                System.out.println("La data scelta e' una festivita', non e' possibile prenotare");
                return;
            }
        }

        // Controlla che non sia sabato o domenica
        if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
            System.out.println("Non e' possibile prenotare per il weekend");
            return;
        }

        int n_people = InputDati.leggiIntero("Inserisci il numero di persone: ", 1, Manager.getN_client());
        float max_workload = n_people * Manager.getWorkload_for_client();

        int total_people = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getDate().toString().equals(date_string)) {
                total_people += reservation.getNumber_of_people();
            }
        }

        if (total_people + n_people > Manager.getN_client()) {
            System.out.println("I posti disponibili per la data scelta sono solo: " + (Manager.getN_client() - total_people));
            System.out.println("Prenotazione non effettuata");
            return;
        }

        // Crea l'elemento nuovo e lo aggiunge al documento XML
        Element new_reservation = doc.createElement("reservation");
        new_reservation.setAttribute("id", String.valueOf(id));
        new_reservation.setAttribute("date", date_string);
        new_reservation.setAttribute("n_people", String.valueOf(n_people));
        new_reservation.setAttribute("workload", String.valueOf(max_workload));
        new_reservation.appendChild(doc.createTextNode("\n\t\t"));


        List<Recipe> recipes = recipeReader();
        List<Recipe> available_recipes = new ArrayList<>();
        for (Recipe recipe: recipes) {
            if (new_date.between(recipe.getPeriod()[0], recipe.getPeriod()[1]))
                available_recipes.add(recipe);
        }
        List<Menu> menus = menuReader();
        List<Menu> available_menus = new ArrayList<>();
        for (Menu menu: menus) {
            if (new_date.between(menu.getStart(), menu.getEnd()))
                available_menus.add(menu);
        }
        if (available_recipes.isEmpty()) {
            System.out.println("\nNon ci sono ricette per la data scelta");
            return;
        }

        // Chiede il menu' per la prenotazione
        int counter = n_people;
        while (counter > 0) {

            int option;
            System.out.println("\n1 - Ordinazione alla carta");
            if (!available_menus.isEmpty()) {
                System.out.println("2 - Menu' tematico");
                option = InputDati.leggiIntero("Scegli: ", 1, 2);
            } else
                option = InputDati.leggiIntero("Scegli: ", 1, 1);

            if (option == 1) {
                int expected_workload = 0;
                List<Recipe> dishes = new ArrayList<>();
                int recipe_id;
                do {
                    System.out.println("\nRicette disponibili il " + date_string + ":");
                    for (Recipe r : available_recipes)
                        System.out.println(r.getId() + " - " + r.getName());
                    System.out.println("0 - Termina Menu");

                    //Si assicura di scegliere una ricetta disponibile per la data scelta
                    boolean found = false;
                    Recipe selected_recipe = null;
                    do {
                        recipe_id = InputDati.leggiIntero("Scegli l'id di una ricetta valido: ", 0, recipes.get(recipes.size() - 1).getId());
                        for (Recipe recipe : available_recipes) {
                            if (recipe.getId() == recipe_id || recipe_id == 0) {
                                selected_recipe = recipe;
                                expected_workload += selected_recipe.getWorkload();
                                found = true;

                                //Se il carico di lavoro supera il limite, non permette di aggiungere il piatto
                                if (recipe_id != 0) {
                                    if (expected_workload <= Manager.getWorkload_for_client()) {
                                        dishes.add(selected_recipe);
                                        System.out.println("Aggiunto!");
                                    } else {
                                        System.out.println(expected_workload + " / " + Manager.getWorkload_for_client());
                                        expected_workload -= selected_recipe.getWorkload();
                                        System.out.println("Il piatto non può essere aggiunto a causa dei limiti di carico di lavoro per persone\n");
                                        Restaurant.waitComand();
                                    }
                                }
                                break;
                            }
                        }
                    } while (!found);
                } while (recipe_id != 0);

                int n_people_menu = 1;
                if (counter > 1)
                    n_people_menu = InputDati.leggiIntero("Inserisci il numero di persone che prenderanno questa ordinazione: ", 1, counter);

                Element new_order = doc.createElement("order");
                new_order.setAttribute("n_people", String.valueOf(n_people_menu));
                counter -= n_people_menu;

                for (Recipe dish : dishes) {
                    new_order.appendChild(doc.createTextNode("\n\t\t\t"));
                    Element new_dish = doc.createElement("dish");
                    new_dish.setAttribute("id", String.valueOf(dish.getId()));
                    new_order.appendChild(new_dish);
                }
                new_order.appendChild(doc.createTextNode("\n\t\t"));
                new_reservation.appendChild(new_order);
                if (counter > 0)
                    new_reservation.appendChild(doc.createTextNode("\n\t\t"));
                else
                    new_reservation.appendChild(doc.createTextNode("\n\t"));
            } else {
                System.out.println("Menu' disponibili:");
                for (Menu menu : available_menus)
                    menu.printMenu();

                int menu_id;
                boolean found = false;
                do {
                    menu_id = InputDati.leggiIntero("Scegli un id valido: ", 1, menus.get(menus.size() - 1).getId());
                    for (Menu menu : available_menus) {
                        if (menu.getId() == menu_id) {
                            found = true;
                            break;
                        }
                    }
                } while (!found);

                int n_people_menu = 1;
                if (counter > 1)
                    n_people_menu = InputDati.leggiIntero("Inserisci il numero di persone che prenderanno questo menu': ", 1, counter);

                Element new_order = doc.createElement("order");
                new_order.setAttribute("n_people", String.valueOf(n_people_menu));
                new_order.appendChild(doc.createTextNode("\n\t\t\t"));
                counter -= n_people_menu;

                Element new_menu = doc.createElement("menu");
                new_menu.setAttribute("id", String.valueOf(menu_id));
                new_order.appendChild(new_menu);
                new_order.appendChild(doc.createTextNode("\n\t\t"));
                new_reservation.appendChild(new_order);
                if (counter > 0)
                    new_reservation.appendChild(doc.createTextNode("\n\t\t"));
                else
                    new_reservation.appendChild(doc.createTextNode("\n\t"));
            }
        }

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(new_reservation);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

        // Scrive il documento XML aggiornato sul file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

    public static void elementDeleter(int option, int idValue) {
        String elementName = "recipe";
        String xmlFilePath = RECIPES_PATH;
        try {

            switch (option) {
                case 2 -> {
                    elementName = "drink";
                    xmlFilePath = DRINKS_PATH;
                }
                case 3 -> {
                    elementName = "extra";
                    xmlFilePath = EXTRAS_PATH;
                }
                case 4 -> {
                    elementName = "reservation";
                    xmlFilePath = RESERVATIONS_PATH;
                }
                case 5 -> {
                    elementName = "menu";
                    xmlFilePath = TEMATIC_MENU_PATH;
                }
            }

            File inputFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            NodeList nodeList = doc.getElementsByTagName(elementName);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    if (String.valueOf(idValue).equals(id)) {
                        element.getParentNode().removeChild(element);
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void menuWriter() throws Exception {

        // Carica il documento XML dal file esistente
        File xmlFile = new File(TEMATIC_MENU_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        // Legge i dati del nuovo menù tematico
        if (recipeReader().isEmpty()) {
            System.out.println("Non sono presenti ricette inserite dal gestore, non è possibile creare un menù tematico!");
            return;
        }
        String name = InputDati.leggiStringaNonVuota("Inserisci il nome del menù tematico: ");
        int workload = (Manager.getWorkload_for_client() * 4) / 3;
        int id = 1;
        if (menuReader().size() > 0)
            id = menuReader().get(menuReader().size() - 1).getId() + 1;

        int option;
        int count = 0;
        List<Recipe> recipes = recipeReader();
        List<Recipe> dishes = new ArrayList<>();
        do {
            for (Recipe r : recipes)
                System.out.println(r.getId() + " - " + r.getName() + " - [carico di lavoro: " + r.getWorkload() + "]");
            System.out.println("0 - Termina Menu");
            option = InputDati.leggiIntero("\nScegli l'id del piatto da inserire nel menù tematico: ", 0, recipes.size());
            for (Recipe r : recipes) {
                if (r.getId() == option && r.getWorkload() <= workload) {
                    dishes.add(r);
                    workload -= r.getWorkload();
                    count += r.getWorkload();
                } else if (r.getId() == option && r.getWorkload() > workload)
                    System.out.println("Il piatto non può essere inserito perchè supera il carico di lavoro per una persona\n");
                }
        } while (option != 0);

        // Crea l'elemento per la nuova ricetta
        Element menu = doc.createElement("menu");
        menu.setAttribute("name", name);
        menu.setAttribute("id", String.valueOf(id));
        menu.setAttribute("workload", String.valueOf((Manager.getWorkload_for_client() * 4) / 3));
        Menu m = new Menu(name, dishes, id, workload);
        menu.setAttribute("period", m.getStart().toString() + " - " + m.getEnd().toString());
        menu.appendChild(doc.createTextNode("\n"));

        // Crea gli elementi per i piatti
        for (Recipe r: dishes) {
            menu.appendChild(doc.createTextNode("\t\t"));
            Element new_ingredient = doc.createElement("dish");
            new_ingredient.setAttribute("name",r.getName());
            new_ingredient.setAttribute("workload", String.valueOf(r.getWorkload()));
            menu.appendChild(new_ingredient);
            menu.appendChild(doc.createTextNode("\n"));
        }
        menu.appendChild(doc.createTextNode("\t"));
        // Aggiunge la nuova ricetta al documento XML
        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(menu);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

        // Scrive il documento XML aggiornato sul file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

    public static void attributeUpdater(int id) throws Exception {

        // Legge la quantità da aggiungere
        float upgrade = InputDati.leggiFloatPositivo("Quantità: ");

        // Carica il file XML
        File xmlFile = new File(STORAGE_PATH);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xmlFile);

        // Ottieni l'elemento XML che vuoi modificare
        NodeList nodeList = doc.getElementsByTagName("ingredient");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String ingredient_id = element.getAttribute("id");
                if (String.valueOf(id).equals(ingredient_id)) {
                    // Modifica l'attributo desiderato
                    upgrade += Float.parseFloat(element.getAttribute("quantity"));
                    element.setAttribute("quantity", String.valueOf(upgrade));
                }
            }
        }

        // Salva il file XML modificato
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }

    public static void storageUpdater(int new_id, List<Ingredient> storage) throws Exception {

        //Aggiunge un nuovo ingrediente al file XML
        File xmlFile = new File(STORAGE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        // Legge i dati del nuovo ingrediente
        String name = InputDati.leggiStringaNonVuota("Inserisci il nome dell'ingrediente: ");
        for (Ingredient i : storage)
            if (i.getName().equalsIgnoreCase(name)) {
                System.out.println("L'ingrediente è già presente nel magazzino");
                return;
            }
        float quantity = InputDati.leggiFloatPositivo("Inserisci la quantità: ");
        int unit = InputDati.leggiIntero("1 - grammi\n2 - millilitri\n3 - pezzi\nScegli l'unità di misura: ", 1, 3);

        // Crea l'elemento per il nuovo ingrediente
        Element ingredient = doc.createElement("ingredient");
        ingredient.setAttribute("name", name);
        ingredient.setAttribute("quantity", String.valueOf(quantity));
        ingredient.setAttribute("unit", "g");
        if (unit == 2) ingredient.setAttribute("unit", "ml");
        else ingredient.setAttribute("unit", "pezzi");
        ingredient.setAttribute("id", String.valueOf(new_id));

        // Aggiunge il nuovo ingrediente al documento XML
        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(ingredient);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

        // Scrive il documento XML aggiornato sul file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

    public static int[] parametersReader() {
        File xmlFile = new File(RECIPES_PATH);
        int[] parameters_array = new int[4];

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList parameters = document.getElementsByTagName("seats");
            Element seats = (Element) parameters.item(0);
            parameters_array[0] = Integer.parseInt(seats.getAttribute("value"));

            parameters = document.getElementsByTagName("workload");
            Element workload = (Element) parameters.item(0);
            parameters_array[1] = Integer.parseInt(workload.getAttribute("value"));

            parameters = document.getElementsByTagName("drinks_per_person");
            Element drinks_per_person = (Element) parameters.item(0);
            parameters_array[2] = Integer.parseInt(drinks_per_person.getAttribute("value"));

            parameters = document.getElementsByTagName("extras_per_person");
            Element extras_per_person = (Element) parameters.item(0);
            parameters_array[3] = Integer.parseInt(extras_per_person.getAttribute("value"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters_array;
    }

    public static void parametersWriter(int seats_value, int workload_value, int drinks_value, int extras_value) throws Exception {
        File xmlFile = new File(RECIPES_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Element seats = (Element) doc.getElementsByTagName("seats").item(0);
        seats.setAttribute("value", String.valueOf(seats_value));

        Element workload = (Element) doc.getElementsByTagName("workload").item(0);
        workload.setAttribute("value", String.valueOf(workload_value));

        Element drinks_per_person = (Element) doc.getElementsByTagName("drinks_per_person").item(0);
        drinks_per_person.setAttribute("value", String.valueOf(drinks_value));

        Element extras_per_person = (Element) doc.getElementsByTagName("extras_per_person").item(0);
        extras_per_person.setAttribute("value", String.valueOf(extras_value));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

}

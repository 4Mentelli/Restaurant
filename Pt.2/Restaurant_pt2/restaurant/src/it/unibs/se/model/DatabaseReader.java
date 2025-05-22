package it.unibs.se.model;

import it.unibs.se.exception.ConvertException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class DatabaseReader implements ManagerReader, ReservationReader, StorageReader {

    private static final String RESERVATIONS_PATH = "restaurant/src/it/unibs/xml/Reservations.xml";
    private static final String RECIPES_PATH = "restaurant/src/it/unibs/xml/Ricettario.xml";
    private static final String DRINKS_PATH = "restaurant/src/it/unibs/xml/Drinks.xml";
    private static final String EXTRAS_PATH = "restaurant/src/it/unibs/xml/Extras.xml";
    private static final String HOLIDAYS_PATH = "restaurant/src/it/unibs/xml/Holidays.xml";
    private static final String THEMED_MENU_PATH = "restaurant/src/it/unibs/xml/ThemedMenu.xml";
    private static final String STORAGE_PATH = "restaurant/src/it/unibs/xml/Storage.xml";
    private static final String PARAMETERS_PATH = "restaurant/src/it/unibs/xml/Parameters.xml";

    @Override
    public int[] parametersReader() {
        File xmlFile = new File(PARAMETERS_PATH);
        int[] parameters_array = new int[]{0, 0, 0, 0};

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
            throw new ConvertException("Errore nella lettura dei parametri", e);
        }
        return parameters_array;
    }

    @Override
    public List<Recipe> recipesReader() {
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
                List<GenericIngredient> ingredients = new ArrayList<>();

                ingredientsReader(ingredients, ingredientList);

                recipes.add(new Recipe(name, id, workload, ingredients, start_date, end_date));
            }
        } catch (Exception e) {
            throw new ConvertException("Errore nella lettura delle ricette dal database", e);
        }
        return recipes;
    }

    @Override
    public List<IdentifiableIngredient> drinksReader() {
        File xmlFile = new File(DRINKS_PATH);
        List<IdentifiableIngredient> drinks = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList ingredientList = document.getElementsByTagName("drink");

            for (int j = 0; j < ingredientList.getLength(); j++) {
                Element ingredient = (Element) ingredientList.item(j);
                String name = ingredient.getAttribute("name");
                float quantity = Float.parseFloat(ingredient.getAttribute("quantity"));
                int id = Integer.parseInt(ingredient.getAttribute("id"));
                drinks.add(IngredientFactory.createDrink(name, quantity, id));
            }
        } catch (Exception e) {
            throw new ConvertException("Errore nella lettura delle bevande dal database", e);
        }
        return drinks;
    }

    @Override
    public List<IdentifiableIngredient> extrasReader() {
        File xmlFile = new File(EXTRAS_PATH);
        List<IdentifiableIngredient> extras = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList ingredientList = document.getElementsByTagName("extra");

            for (int j = 0; j < ingredientList.getLength(); j++) {
                Element ingredient = (Element) ingredientList.item(j);
                String name = ingredient.getAttribute("name");
                float quantity = Float.parseFloat(ingredient.getAttribute("quantity"));
                int id = Integer.parseInt(ingredient.getAttribute("id"));
                extras.add(IngredientFactory.createExtra(name, quantity, id));
            }
        } catch (Exception e) {
            throw new ConvertException("Errore nella lettura degli extra dal database", e);
        }
        return extras;
    }

    @Override
    public List<Menu> themedMenusReader() {
        File xmlFile = new File(THEMED_MENU_PATH);
        List<Menu> thematic_menus = new ArrayList<>();

        List<Recipe> recipes = recipesReader();


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
                    for (Recipe recipe : recipes) {
                        if (recipe.getName().equals(dish.getAttribute("name"))) {
                            dishes.add(recipe);
                        }
                    }
                }
                thematic_menus.add(new Menu(name, dishes, id, workload));
            }
        } catch (Exception e) {
            throw new ConvertException("Errore nella lettura dei menù tematici dal database", e);
        }
        return thematic_menus;
    }

    @Override
    public List<Date> holidaysReader() {
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
            throw new ConvertException("Errore durante il controllo della data", e);
        }
        return holidays;
    }

    @Override
    public List<Reservation> reservationsReader() {

        File xmlFile = new File(RESERVATIONS_PATH);
        List<Reservation> reservations_list = new ArrayList<>();
        ReservationUpdater updater = new DatabaseUpdater();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList reservationList = document.getElementsByTagName("reservation");

            for (int i = 0; i < reservationList.getLength(); i++) {

                List<OrderedMenu> menusList = new ArrayList<>();
                List<OrderedDishes> dishesList = new ArrayList<>();

                Reservation reservation_obj;

                Element reservation = (Element) reservationList.item(i);
                int id = Integer.parseInt(reservation.getAttribute("id"));
                int n_people = Integer.parseInt(reservation.getAttribute("n_people"));
                Date date = new Date(reservation.getAttribute("date"));
                float workload = Float.parseFloat(reservation.getAttribute("workload"));
                reservation_obj = new Reservation(id, n_people, workload, date);

                List<Integer> dishes_list_ids;
                NodeList orders_list = reservation.getElementsByTagName("order");

                for (int j = 0; j < orders_list.getLength(); j++) {
                    Element order = (Element) orders_list.item(j);
                    int n_people_order = Integer.parseInt(order.getAttribute("n_people"));

                    if (order.getElementsByTagName("dish").getLength() > 0) {

                        dishes_list_ids = new ArrayList<>();
                        for (int k = 0; k < order.getElementsByTagName("dish").getLength(); k++) {
                            Element dish = (Element) order.getElementsByTagName("dish").item(k);
                            dishes_list_ids.add(Integer.parseInt(dish.getAttribute("id")));
                        }
                        OrderedDishes ordered_dishes = new OrderedDishes(dishes_list_ids, n_people_order);
                        dishesList.add(ordered_dishes);

                    } else {

                        for (int k = 0; k < order.getElementsByTagName("menu").getLength(); k++) {
                            Element menu = (Element) order.getElementsByTagName("menu").item(k);
                            OrderedMenu orderedMenu = new OrderedMenu(Integer.parseInt(menu.getAttribute("id")), n_people_order);
                            menusList.add(orderedMenu);
                        }
                    }
                }

                if (!dishesList.isEmpty())
                    reservation_obj.setOrderedDishes(dishesList);
                if (!menusList.isEmpty())
                    reservation_obj.setOrderedMenus(menusList);

                if (LocalDate.of(reservation_obj.getDate().getYear(), reservation_obj.getDate().getMonth(), reservation_obj.getDate().getDay()).isAfter(LocalDate.now()))
                    reservations_list.add(reservation_obj);
                else
                    updater.deleteElement("reservation", id);

            }
        } catch (Exception e) {
            throw new ConvertException("Errore nella lettura delle prenotazioni dal database", e);
        }
        return reservations_list;
    }

    @Override
    public List<GenericIngredient> storageReader() {
        File xmlFile = new File(STORAGE_PATH);
        List<GenericIngredient> storage = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList ingredientList = document.getElementsByTagName("ingredient");

            ingredientsReader(storage, ingredientList);
        } catch (Exception e) {
            throw new ConvertException("Errore nella lettura degli elementi in magazzino dal database", e);
        }
        return storage;
    }

    private void ingredientsReader(List<GenericIngredient> storage, NodeList ingredientList) {
        for (int j = 0; j < ingredientList.getLength(); j++) {
            Element ingredient = (Element) ingredientList.item(j);
            String ingredient_name = ingredient.getAttribute("name");
            float quantity = Float.parseFloat(ingredient.getAttribute("quantity"));
            String unit = ingredient.getAttribute("unit");
            storage.add(new GenericIngredient(ingredient_name, quantity, unit));
        }
    }

}

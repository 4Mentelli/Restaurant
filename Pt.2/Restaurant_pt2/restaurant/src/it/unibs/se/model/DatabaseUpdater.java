package it.unibs.se.model;

import it.unibs.se.exception.ConvertException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

public class DatabaseUpdater implements ManagerUpdater, ReservationUpdater, StorageUpdater {

    private static final String RESERVATIONS_PATH = "restaurant/src/it/unibs/xml/Reservations.xml";
    private static final String RECIPES_PATH = "restaurant/src/it/unibs/xml/Ricettario.xml";
    private static final String DRINKS_PATH = "restaurant/src/it/unibs/xml/Drinks.xml";
    private static final String EXTRAS_PATH = "restaurant/src/it/unibs/xml/Extras.xml";
    private static final String THEMED_MENU_PATH = "restaurant/src/it/unibs/xml/ThemedMenu.xml";
    private static final String STORAGE_PATH = "restaurant/src/it/unibs/xml/Storage.xml";
    private static final String PARAMETERS_PATH = "restaurant/src/it/unibs/xml/Parameters.xml";

    @Override
    public void parametersWriter(int seats_value, int workload_value, int drinks_value, int extras_value) throws Exception {
        File xmlFile = new File(PARAMETERS_PATH);
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

    @Override
    public void recipeWriter(Recipe recipe) throws Exception {

        File xmlFile = new File(RECIPES_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Element new_recipe = doc.createElement("recipe");
        new_recipe.setAttribute("name", recipe.getName());
        new_recipe.setAttribute("id", String.valueOf(recipe.getId()));
        new_recipe.setAttribute("workload", String.valueOf(recipe.getIngredients().size() * 2));
        new_recipe.setAttribute("period", recipe.getStartDate().getString() + " - " + recipe.getEndDate().getString());
        new_recipe.appendChild(doc.createTextNode("\n"));

        List<GenericIngredient> ingredients = recipe.getIngredients();
        for (GenericIngredient ingredient : ingredients) {
            new_recipe.appendChild(doc.createTextNode("\t\t"));
            Element new_ingredient = doc.createElement("ingredient");
            new_ingredient.setAttribute("name", ingredient.getName());
            new_ingredient.setAttribute("quantity", String.valueOf(ingredient.getQuantity()));
            new_ingredient.setAttribute("unit", ingredient.getUnitOfMeasure());
            new_recipe.appendChild(new_ingredient);
            new_recipe.appendChild(doc.createTextNode("\n"));
        }
        new_recipe.appendChild(doc.createTextNode("\t"));

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(new_recipe);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
        updateDatabase(xmlFile, doc);
    }

    @Override
    public void drinkWriter(IdentifiableIngredient drink) throws Exception {

        File xmlFile = new File(DRINKS_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Element new_ingredient = doc.createElement("drink");
        appendIngredient(xmlFile, doc, new_ingredient, drink.getName(), drink.getId(), drink.getQuantity());
    }

    @Override
    public void extraWriter(IdentifiableIngredient extra) throws Exception {
        File xmlFile = new File(EXTRAS_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Element new_ingredient = doc.createElement("extra");
        appendIngredient(xmlFile, doc, new_ingredient, extra.getName(), extra.getId(), extra.getQuantity());
    }

    @Override
    public void menuWriter(Menu new_menu) throws Exception {

        File xmlFile = new File(THEMED_MENU_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Element menu = doc.createElement("menu");
        menu.setAttribute("name", new_menu.getName());
        menu.setAttribute("id", String.valueOf(new_menu.getId()));
        menu.setAttribute("workload", String.valueOf(new_menu.getWorkload()));
        menu.setAttribute("period", new_menu.getStartDate().getString() + " - " + new_menu.getEndDate().getString());
        menu.appendChild(doc.createTextNode("\n"));

        for (Recipe recipe: new_menu.getDishes()) {
            menu.appendChild(doc.createTextNode("\t\t"));
            Element new_ingredient = doc.createElement("dish");
            new_ingredient.setAttribute("name",recipe.getName());
            new_ingredient.setAttribute("workload", String.valueOf(recipe.getWorkload()));
            menu.appendChild(new_ingredient);
            menu.appendChild(doc.createTextNode("\n"));
        }
        menu.appendChild(doc.createTextNode("\t"));

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(menu);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
        updateDatabase(xmlFile, doc);
    }

    @Override
    public void reservationWriter(Reservation reservation) throws Exception {

        File xmlFile = new File(RESERVATIONS_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        String date_string = reservation.getDate().getDay() + "/" + reservation.getDate().getMonth() + "/" + reservation.getDate().getYear();

        Element new_reservation = doc.createElement("reservation");
        new_reservation.setAttribute("id", String.valueOf(reservation.getId()));
        new_reservation.setAttribute("date", date_string);
        new_reservation.setAttribute("n_people", String.valueOf(reservation.getNumber_of_people()));
        new_reservation.setAttribute("workload", String.valueOf(reservation.getWorkload()));
        new_reservation.appendChild(doc.createTextNode("\n"));

        for (int i = 0; i < reservation.getOrderedDishes().size(); i++) {
            List<Integer> ordered_dishes_ids = reservation.getOrderedDishes().get(i).getDish_ids();
            int number_of_people = reservation.getOrderedDishes().get(i).getQuantity();
            new_reservation.appendChild(doc.createTextNode("\t\t"));
            appendALaCarteOrder(doc, new_reservation, ordered_dishes_ids, number_of_people);
        }

        for (int i = 0; i < reservation.getOrderedMenus().size(); i++) {
            int menu_id = reservation.getOrderedMenus().get(i).menu_id();
            int number_of_people = reservation.getOrderedMenus().get(i).quantity();
            new_reservation.appendChild(doc.createTextNode("\t\t"));
            appendThemedMenuOrder(doc, new_reservation, menu_id, number_of_people);
        }

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(new_reservation);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

    @Override
    public void storageWriter(GenericIngredient new_ingredient, int new_id) throws Exception {

        File xmlFile = new File(STORAGE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(xmlFile);

        Element ingredient = doc.createElement("ingredient");
        ingredient.setAttribute("name", new_ingredient.getName());
        ingredient.setAttribute("quantity", String.valueOf(new_ingredient.getQuantity()));
        ingredient.setAttribute("unit", new_ingredient.getUnitOfMeasure());
        ingredient.setAttribute("id", String.valueOf(new_id));

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(ingredient);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
        updateDatabase(xmlFile, doc);
    }

    @Override
    public void updateIngredientQuantity(String type, int id, float increment) throws Exception {

        String path = switch (type) {
            case "drink" -> DRINKS_PATH;
            case "extra" -> EXTRAS_PATH;
            default -> STORAGE_PATH;
        };

        File xmlFile = new File(path);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xmlFile);

        NodeList nodeList = doc.getElementsByTagName(type);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String ingredient_id = element.getAttribute("id");
                if (String.valueOf(id).equals(ingredient_id)) {
                    increment += Float.parseFloat(element.getAttribute("quantity"));
                    element.setAttribute("quantity", String.valueOf(increment));
                }
            }
        }

        updateDatabase(xmlFile, doc);
    }

    @Override
    public void deleteElement(String element_name, int idValue) {
        String xmlFilePath;
        try {
            switch (element_name) {
                case "drink" -> xmlFilePath = DRINKS_PATH;
                case "extra" -> xmlFilePath = EXTRAS_PATH;
                case "reservation" -> xmlFilePath = RESERVATIONS_PATH;
                case "menu" -> xmlFilePath = THEMED_MENU_PATH;
                default -> xmlFilePath = RECIPES_PATH;
            }

            File inputFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            NodeList nodeList = doc.getElementsByTagName(element_name);
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
            throw new ConvertException("Errore nella rimozione dell'elemento #" + idValue, e);
        }
    }

    private static void appendIngredient(File xmlFile, Document doc, Element new_ingredient, String name, int id, float quantity) throws IOException, TransformerException {
        new_ingredient.setAttribute("name", name);
        new_ingredient.setAttribute("id", String.valueOf(id));
        new_ingredient.setAttribute("quantity", String.valueOf(quantity));

        doc.getDocumentElement().appendChild(doc.createTextNode("\t"));
        doc.getDocumentElement().appendChild(new_ingredient);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
        updateDatabase(xmlFile, doc);
    }

    private static void appendALaCarteOrder(Document doc, Element new_reservation, List<Integer> ordered_dishes_ids, int number_of_people) {

        Element new_order = doc.createElement("order");
        new_order.appendChild(doc.createTextNode("\t\t"));
        new_order.setAttribute("n_people", String.valueOf(number_of_people));

        for (Integer dish_id : ordered_dishes_ids) {
            new_order.appendChild(doc.createTextNode("\n\t\t\t"));
            Element new_dish = doc.createElement("dish");
            new_dish.setAttribute("id", String.valueOf(dish_id));
            new_order.appendChild(new_dish);
        }
        new_order.appendChild(doc.createTextNode("\n\t\t"));
        new_reservation.appendChild(new_order);

        new_reservation.appendChild(doc.createTextNode("\n\t"));
    }

    private static void appendThemedMenuOrder(Document doc, Element new_reservation, int ordered_menu_id, int number_of_people) {

        Element new_order = doc.createElement("order");
        new_order.appendChild(doc.createTextNode("\t\t"));
        new_order.setAttribute("n_people", String.valueOf(number_of_people));
        new_order.appendChild(doc.createTextNode("\n\t\t\t"));

        Element new_menu = doc.createElement("menu");
        new_menu.setAttribute("id", String.valueOf(ordered_menu_id));
        new_order.appendChild(new_menu);
        new_order.appendChild(doc.createTextNode("\n\t\t"));
        new_reservation.appendChild(new_order);

        new_reservation.appendChild(doc.createTextNode("\n\t"));
    }

    private static void updateDatabase(File xmlFile, Document doc) throws IOException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileWriter(xmlFile));
        transformer.transform(source, result);
    }

}

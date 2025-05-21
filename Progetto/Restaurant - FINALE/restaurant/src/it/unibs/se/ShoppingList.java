package it.unibs.se;

public class ShoppingList extends Ingredient{

    private float quantity;
    private String unit;

    public ShoppingList(String name, int quantity, String unit) {
        super();
        this.quantity = quantity;
        this.unit = unit;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void printShoppingList() {
        System.out.println(this.getName() + " " + this.getQuantity() + " " + this.getUnit());
    }
}

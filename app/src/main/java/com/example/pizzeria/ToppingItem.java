package com.example.pizzeria;

/**
 * Topping Item class that will act as each item in the recycler adapter class for the pizza
 * toppings to select.
 * @author Frank Garcia
 */
public class ToppingItem {
    private String toppingName, toppingPrice;
    private int image;
    private String selectionStatus;
    private final String TOPPINGPRICE = "$1.69";

    /**
     * This constructor initializes the ToppingItem instance with the details of the topping, which
     * include the name, the image id, and the selection status of the topping.
     * @param toppingName The name of the topping.
     * @param image The image id of the topping.
     * @param selectionStatus The selection status of whether the topping is added to the order.
     */
    public ToppingItem (String toppingName, int image, String selectionStatus) {
        this.toppingName = toppingName;
        this.toppingPrice = TOPPINGPRICE;
        this.image = image;
        this.selectionStatus = selectionStatus;
    }

    /**
     * Sets the toppingPrice string tot he final String TOPPINGPRICE string.
     */
    public void resetPrice() {
        toppingPrice = TOPPINGPRICE;
    }

    /**
     * Returns the name of the topping.
     * @return The name of the topping.
     */
    public String getToppingName() { return this.toppingName; }

    /**
     * Returns the topping price.
     * @return The price of the topping.
     */
    public String getToppingPrice() { return this.toppingPrice; }

    /**
     * Returns the id of the image for the topping.
     * @return The image id for the topping.
     */
    public int getImage() { return this.image; }

    /**
     * Returns the selection status of the topping.
     * @return The status of whether the topping is added to the order.
     */
    public String getSelectionStatus() { return this.selectionStatus; }

    /**
     * Sets the toppingPrice string to the string specified in the parameter
     */
    public void setToppingPrice(String priceString) {
        toppingPrice = priceString;
    }

    /**
     * Sets the String value of selectionStatus.
     * @param selected The boolean that determines if selectionStatus will be assigned the String
     *                 "Select" or "Unselected"
     */
    public void setSelected(boolean selected) {
        if (selected) this.selectionStatus = "Selected";
        else this.selectionStatus = "Unselected";
    }

    /**
     * Returns true if the selectionStatus has value "Selected", else false.
     * @return True if selectionStatus has "Selected" as its string value, else false.
     */
    public boolean isSelected() {
        return this.selectionStatus.equals("Selected");
    }
}

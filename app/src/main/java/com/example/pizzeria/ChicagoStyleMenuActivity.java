package com.example.pizzeria;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rupizzaria.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Activity for configuring and ordering Chicago-style pizzas.
 * @author Jorgeluis Done
 */
public class ChicagoStyleMenuActivity extends AppCompatActivity {

    private Spinner pizzaTypeSpinner, sizeSpinner;
    private EditText crustValEditText, quantityEditTextNumber, pizzaSubTotal;
    private CheckBox cheddarCheckBox, sausageCheckBox, pepperoniCheckBox, greenPepperCheckBox, onionCheckBox, mushroomCheckBox, bbqChickenCheckBox, provoloneCheckBox, beefCheckBox, hamCheckBox;
    private Button backButton, addOrderButton, plusButton, minusButton;

    private Pizza currentPizza;
    private OrderManager orderManager;

    private HashMap<Topping, CheckBox> toppingToCheckboxMap;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicagostyle);

        initializeViews();
        setupPizzaTypeSpinner();
        setupSizeSpinner();
        setupToppingCheckboxes();
        setupButtonHandlers();

        pizzaTypeSpinner.setSelection(((ArrayAdapter<String>) pizzaTypeSpinner.getAdapter()).getPosition("Build Your Own"));
        sizeSpinner.setSelection(((ArrayAdapter<String>) sizeSpinner.getAdapter()).getPosition("Small"));
        selectPizza("Build Your Own", "Pan");
        orderManager = GlobalDataManager.getInstance().getOrderManager();
    }

    /**
     * Initializes UI components by finding them by their IDs and setting up initial states.
     */
    private void initializeViews() {
        pizzaTypeSpinner = findViewById(R.id.ChicagoPizzaTypeSpinner);
        sizeSpinner = findViewById(R.id.ChicagoSizeSpinner);
        crustValEditText = findViewById(R.id.ChicagoCrustValEditText);
        quantityEditTextNumber = findViewById(R.id.ChicagoQuantityEditTextNumber);
        pizzaSubTotal = findViewById(R.id.ChicagoPizzaSubTotal);

        crustValEditText.setEnabled(false);
        quantityEditTextNumber.setText("1");
        quantityEditTextNumber.setEnabled(false);

        backButton = findViewById(R.id.ChicagoBackButton);
        addOrderButton = findViewById(R.id.ChicagoAddOrderButton);
        plusButton = findViewById(R.id.ChicagoPlusButton);
        minusButton = findViewById(R.id.ChicagoMinusButton);

        cheddarCheckBox = findViewById(R.id.ChicagoCheddarCheckBox);
        sausageCheckBox = findViewById(R.id.ChicagoSausageCheckBox);
        pepperoniCheckBox = findViewById(R.id.ChicagoPepperoniCheckBox);
        greenPepperCheckBox = findViewById(R.id.ChicagoGreenPepperCheckBox);
        onionCheckBox = findViewById(R.id.ChicagoOnionCheckBox);
        mushroomCheckBox = findViewById(R.id.ChicagoMushroomCheckBox);
        bbqChickenCheckBox = findViewById(R.id.ChicagoBBQChickenCheckBox);
        provoloneCheckBox = findViewById(R.id.ChicagoProvoloneCheckBox);
        beefCheckBox = findViewById(R.id.ChicagoBeefCheckBox);
        hamCheckBox = findViewById(R.id.ChicagoHamCheckBox);

        toppingToCheckboxMap = new HashMap<>();
        toppingToCheckboxMap.put(Topping.CHEDDAR, cheddarCheckBox);
        toppingToCheckboxMap.put(Topping.SAUSAGE, sausageCheckBox);
        toppingToCheckboxMap.put(Topping.PEPPERONI, pepperoniCheckBox);
        toppingToCheckboxMap.put(Topping.GREENPEPPER, greenPepperCheckBox);
        toppingToCheckboxMap.put(Topping.ONION, onionCheckBox);
        toppingToCheckboxMap.put(Topping.MUSHROOM, mushroomCheckBox);
        toppingToCheckboxMap.put(Topping.BBQCHICKEN, bbqChickenCheckBox);
        toppingToCheckboxMap.put(Topping.PROVOLONE, provoloneCheckBox);
        toppingToCheckboxMap.put(Topping.BEEF, beefCheckBox);
        toppingToCheckboxMap.put(Topping.HAM, hamCheckBox);
    }

    /**
     * Sets up the spinner for selecting pizza types.
     */
    private void setupPizzaTypeSpinner() {
        List<String> pizzaTypes = Arrays.asList("Deluxe", "Build Your Own", "BBQ Chicken", "Meatzza");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(adapter);
        pizzaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPizzaType = pizzaTypes.get(position);
                Log.d("DEBUG", "Selected pizza type: " + selectedPizzaType);
                updatePizzaSelection(selectedPizzaType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("DEBUG", "No pizza type selected");
            }
        });
    }

    /**
     * Sets up the spinner for selecting pizza sizes.
     */
    private void setupSizeSpinner() {
        List<String> sizes = Arrays.asList("Small", "Medium", "Large");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Size selectedSize = Size.valueOf(sizes.get(position).toUpperCase());
                Log.d("DEBUG", "Selected size: " + selectedSize);
                if (currentPizza != null) {
                    currentPizza.setSize(selectedSize);
                    updateTotalPrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("DEBUG", "No size selected");
            }
        });
    }

    /**
     * Sets up checkbox listeners for each topping, allowing changes only if the current pizza supports it.
     */
    private void setupToppingCheckboxes() {
        toppingToCheckboxMap.forEach((topping, checkBox) -> {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (currentPizza instanceof BuildYourOwn) {
                    handleToppingChange(topping, isChecked);
                }
            });
        });
    }

    /**
     * Handles changes to topping selections, adding or removing them from the current pizza.
     *
     * @param topping   The topping that was changed.
     * @param isChecked True if the topping was added, false if removed.
     */
    private void handleToppingChange(Topping topping, boolean isChecked) {
        CheckBox checkBox = getCheckBoxForTopping(topping);
        if (checkBox == null) {
            Log.e("DEBUG", "Checkbox for topping " + topping + " not found.");
            return;
        }

        if (isChecked && currentPizza.getToppings().size() >= 7) {
            Toast.makeText(this, "You can only select up to 7 toppings.", Toast.LENGTH_SHORT).show();
            checkBox.setChecked(false);
            return;
        }

        if (isChecked) {
            currentPizza.addTopping(topping);
            Log.d("DEBUG", "Added " + topping + " to pizza.");
        } else {
            currentPizza.removeTopping(topping);
            Log.d("DEBUG", "Removed " + topping + " from pizza.");
        }
        updateTotalPrice();
    }

    /**
     * Updates the selection of pizza based on the selected type and updates UI accordingly.
     *
     * @param pizzaType The type of pizza selected.
     */
    private void updatePizzaSelection(String pizzaType) {
        String crustType = getCrustType(pizzaType);
        crustValEditText.setText(crustType);
        selectPizza(pizzaType, crustType);
        updateToppingsAccessibility(pizzaType);
    }

    /**
     * Retrieves the crust type based on the pizza type.
     *
     * @param pizzaType The type of pizza.
     * @return The corresponding crust type as a string.
     */
    private String getCrustType(String pizzaType) {
        switch (pizzaType) {
            case "Deluxe": return "Deep Dish";
            case "BBQ Chicken": return "Pan";
            case "Meatzza": return "Stuffed";
            case "Build Your Own": return "Pan";
            default: return null;
        }
    }

    /**
     * Gets the CheckBox associated with a specific topping.
     *
     * @param topping The topping to find the CheckBox for.
     * @return The corresponding CheckBox.
     */
    private CheckBox getCheckBoxForTopping(Topping topping) {
        return toppingToCheckboxMap.get(topping);
    }

    /**
     * Updates the accessibility of topping checkboxes based on the current pizza type.
     *
     * @param pizzaType The current pizza type.
     */
    private void updateToppingsAccessibility(String pizzaType) {
        boolean isBuildYourOwn = "Build Your Own".equals(pizzaType);
        // Disable all checkboxes initially
        toppingToCheckboxMap.values().forEach(checkBox -> {
            checkBox.setEnabled(isBuildYourOwn);
            checkBox.setChecked(false);
        });

        // If it's not a "Build Your Own" pizza, check and disable the checkboxes for the default toppings
        if (!isBuildYourOwn && currentPizza != null) {
            for (Topping topping : currentPizza.getToppings()) {
                CheckBox checkBox = toppingToCheckboxMap.get(topping);
                if (checkBox != null) {
                    checkBox.setChecked(true);
                    checkBox.setEnabled(false);
                }
            }
        }
    }

    /**
     * Selects a pizza based on type and crust, clearing old toppings and setting new ones.
     *
     * @param pizzaType The type of pizza.
     * @param crustType The crust type.
     */
    private void selectPizza(String pizzaType, String crustType) {
        clearToppings();
        Crust crust = Crust.valueOf(crustType.replace(" ", "_").toUpperCase());
        Size selectedSize = getSelectedSize();

        switch (pizzaType) {
            case "Deluxe":
                currentPizza = new Deluxe(crust, selectedSize);
                break;
            case "BBQ Chicken":
                currentPizza = new BBQChicken(crust, selectedSize);
                break;
            case "Meatzza":
                currentPizza = new Meatzza(crust, selectedSize);
                break;
            case "Build Your Own":
                currentPizza = new BuildYourOwn(crust, selectedSize);
                break;
            default:
                Log.e("DEBUG", "Unknown pizza type: " + pizzaType);
                currentPizza = null;
                return;
        }

        if (currentPizza != null) {
            currentPizza.setCrust(crust);
            updateTotalPrice();
        }
    }

    /**
     * Sets up listeners for all button actions in the UI.
     */
    private void setupButtonHandlers() {
        plusButton.setOnClickListener(v -> increaseQuantity());
        minusButton.setOnClickListener(v -> decreaseQuantity());
        addOrderButton.setOnClickListener(v -> addToOrder());
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Updates the displayed total price based on the current pizza and quantity.
     */
    private void updateTotalPrice() {
        if (currentPizza != null) {
            double totalPrice = currentPizza.price() * Integer.valueOf(quantityEditTextNumber.getText().toString());
            pizzaSubTotal.setText(String.format("%.2f", totalPrice));
        }
    }

    /**
     * Increases the quantity of the current pizza order by one.
     */
    private void increaseQuantity() {
        int currentQuantity = Integer.parseInt(quantityEditTextNumber.getText().toString());
        quantityEditTextNumber.setText(String.valueOf(currentQuantity + 1));
        updateTotalPrice();
    }

    /**
     * Decreases the quantity of the current pizza order by one, if it is greater than one.
     */

    private void decreaseQuantity() {
        int currentQuantity = Integer.parseInt(quantityEditTextNumber.getText().toString());
        if (currentQuantity > 1) {
            quantityEditTextNumber.setText(String.valueOf(currentQuantity - 1));
            updateTotalPrice();
        }
    }

    /**
     * Adds the current configured pizza to the order, handling multiple quantities.
     */
    private void addToOrder() {
        if (currentPizza == null) {
            Log.e("DEBUG", "No current pizza to add to order.");
            Toast.makeText(this, "No pizza selected to add to order!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int orderQuantity = Integer.parseInt(quantityEditTextNumber.getText().toString());
            for (int i = 0; i < orderQuantity; i++) {
                Pizza pizzaToAdd = clonePizza(currentPizza);
                if (pizzaToAdd == null) {
                    Log.e("DEBUG", "Failed to clone pizza");
                    continue;  // Skip adding if clone fails
                }
                orderManager.getCurrentOrder().addPizza(pizzaToAdd);
                Log.d("DEBUG", "Added pizza to order, quantity: " + orderQuantity);
            }
            Toast.makeText(this, "Pizza successfully added to order.", Toast.LENGTH_LONG).show();
            resetOrder(); // Resets UI after adding the pizza to the order
        } catch (Exception e) {
            Log.e("DEBUG", "Error adding pizza to order: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Clones the current pizza, creating a new instance with the same properties.
     *
     * @param original The original pizza to clone.
     * @return A new pizza instance with the same properties as the original.
     */
    private Pizza clonePizza(Pizza original) {
        if (original == null) {
            Log.e("DEBUG", "Original pizza is null, cannot clone.");
            return null;
        }

        Pizza cloned = null;
        // Clone based on the type of the original pizza
        try {
            switch (original.getClass().getSimpleName()) {
                case "BuildYourOwn":
                    cloned = new BuildYourOwn(original.getCrust(), original.getSize());
                    for (Topping topping : original.getToppings()) {
                        cloned.addTopping(topping); // Ensure toppings are not null
                    }
                    break;
                case "Deluxe":
                    cloned = new Deluxe(original.getCrust(), original.getSize());
                    break;
                case "BBQChicken":
                    cloned = new BBQChicken(original.getCrust(), original.getSize());
                    break;
                case "Meatzza":
                    cloned = new Meatzza(original.getCrust(), original.getSize());
                    break;
                default:
                    Log.e("DEBUG", "Unknown pizza type: " + original.getClass().getSimpleName());
                    return null;
            }
        } catch (Exception e) {
            Log.e("DEBUG", "Error in cloning pizza: " + e.getMessage());
            return null;
        }
        return cloned;
    }

    /**
     * Resets the order after a pizza is added to the order, clearing selections and resetting the UI.
     */
    private void resetOrder() {
        Log.d("DEBUG", "Resetting order...");
        if (currentPizza != null) {
            Log.d("DEBUG", "Current pizza before reset: " + currentPizza.toString());
        } else {
            Log.d("DEBUG", "No current pizza to reset.");
        }
        quantityEditTextNumber.setText("1");
        if ("Build Your Own".equals(pizzaTypeSpinner.getSelectedItem().toString())) {
            clearToppings();
        }
        Log.d("DEBUG", "Order reset successfully.");
    }

    /**
     * Clears all selected toppings from the UI.
     */
    private void clearToppings() {
        toppingToCheckboxMap.values().forEach(checkBox -> checkBox.setChecked(false));
    }

    /**
     * Gets the selected size from the size spinner.
     *
     * @return The selected pizza size.
     */
    private Size getSelectedSize() {
        String selectedSize = sizeSpinner.getSelectedItem().toString();
        switch (selectedSize) {
            case "Large": return Size.LARGE;
            case "Medium": return Size.MEDIUM;
            case "Small": return Size.SMALL;
            default: return Size.SMALL;
        }
    }
}

package com.example.pizzeria;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rupizzaria.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activity for configuring and ordering NY-style pizzas.
 * @author Jorgeluis Done and Frank Garcia
 */
public class NYPizzaStyleMenuActivity extends AppCompatActivity
        implements ToppingsAdapter.onToppingsInteractionsListener {
    private ArrayList<ToppingItem> toppings = new ArrayList<>();
    ToppingsAdapter toppingsAdapter;
    private int[] toppingImages = {R.drawable.sausage, R.drawable.pepperoni, R.drawable.greenpepper,
            R.drawable.onion, R.drawable.mushroom, R.drawable.cheddar, R.drawable.bbqchicken,
            R.drawable.provolone, R.drawable.beef, R.drawable.ham};
    private Spinner pizzaTypeSpinner, sizeSpinner;
    private EditText crustValEditText, quantityEditTextNumber, nyPizzaSubTotal;
    private Button addOrderButton, plusButton, minusButton;
    private RecyclerView rcview;
    private static Pizza currentPizza;
    private OrderManager orderManager;


    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nystyle);

        initializeViews();
        setupPizzaTypeSpinner();
        setupSizeSpinner();
        setupButtonHandlers();

        pizzaTypeSpinner.setSelection(((ArrayAdapter<String>) pizzaTypeSpinner.getAdapter()).
                getPosition("Build Your Own"));
        sizeSpinner.setSelection(((ArrayAdapter<String>) sizeSpinner.getAdapter()).
                getPosition("Small"));
        selectPizza("Build Your Own", "Hand Tossed");
        orderManager = GlobalDataManager.getInstance().getOrderManager();
    }

    /**
     * Initializes UI components by finding them by their IDs and setting up initial states.
     */
    private void initializeViews() {
        pizzaTypeSpinner = findViewById(R.id.PizzaTypeSpinner);
        sizeSpinner = findViewById(R.id.SizeSpinner);
        crustValEditText = findViewById(R.id.CrustValEditText);
        quantityEditTextNumber = findViewById(R.id.QuantityEditTextNumber);
        nyPizzaSubTotal = findViewById(R.id.NYPizzaSubTotal);

        crustValEditText.setEnabled(false);
        quantityEditTextNumber.setText("1");
        quantityEditTextNumber.setEnabled(false);

        rcview = findViewById(R.id.rc_view_menu);
        toppingsAdapter = new ToppingsAdapter(this, toppings, this, this);
        rcview.setAdapter(toppingsAdapter);
        rcview.setLayoutManager(new LinearLayoutManager(this));

        addOrderButton = findViewById(R.id.AddOrderButton);
        plusButton = findViewById(R.id.PlusButton);
        minusButton = findViewById(R.id.MinusButton);
        setUpToppingItems();
    }

    /**
     * Initializes and populates the RecyclerView with the topping items.
     */
    private void setUpToppingItems() {
        String[] toppingNames = getResources().getStringArray(R.array.toppingNames);

        for (int i = 0; i < toppingNames.length; i++) {
            toppings.add(new ToppingItem(toppingNames[i], toppingImages[i],
                    "Unselected"));
        }
    }

    /**
     * Sets up the spinner for selecting pizza types.
     */
    private void setupPizzaTypeSpinner() {
        List<String> pizzaTypes = Arrays.asList("Deluxe", "Build Your Own", "BBQ Chicken",
                "Meatzza");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pizzaTypes);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sizes);
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
     * Updates the selection buttons in the RecyclerView to correspond with the selected
     * pizza type.
     */
    private void updateToppingsAccessibility(String pizzaType) {
        if (!pizzaType.equals("Build Your Own")) {
            List<String> stringOfToppings = new ArrayList<>();
            for (Topping topping : currentPizza.getToppings())
                stringOfToppings.add(topping.toString());
            toppingsAdapter.updateSelections(stringOfToppings);
        }
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
            case "Deluxe": return "Brooklyn";
            case "BBQ Chicken": return "Thin";
            case "Meatzza": return "Hand Tossed";
            case "Build Your Own": return "Hand Tossed";
            default: return null;
        }
    }

    /**
     * Selects a pizza based on type and crust, clearing old toppings and setting new ones.
     *
     * @param pizzaType The type of pizza.
     * @param crustType The crust type.
     */
    private void selectPizza(String pizzaType, String crustType) {
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
                toppingsAdapter.resetSelections();
                toppingsAdapter.enableSelectButtons();
                break;
            default:
                Log.e("DEBUG", "Unknown pizza type: " + pizzaType);
                currentPizza = null;
                return;
        }
        updateToppingsAccessibility(pizzaType);

        if (currentPizza != null) {
            currentPizza.setCrust(crust);
            updateTotalPrice();
        }

        if (!pizzaType.equals("Build Your Own")) {
            toppingsAdapter.disableSelectButtons();
        }
    }

    /**
     * Updates the displayed total price based on the current pizza and quantity.
     */
    private void updateTotalPrice() {
        if (currentPizza != null) {
            double totalPrice = currentPizza.price() * Integer.valueOf(
                    quantityEditTextNumber.getText().toString());
            nyPizzaSubTotal.setText(String.format("%.2f", totalPrice));
        }
    }

    /**
     * Sets up listeners for all button actions in the UI.
     */
    private void setupButtonHandlers() {
        plusButton.setOnClickListener(v -> increaseQuantity());
        minusButton.setOnClickListener(v -> decreaseQuantity());
        addOrderButton.setOnClickListener(v -> addToOrder());
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
            Toast.makeText(this, "No pizza selected to add to order!",
                    Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Pizza successfully added to order.",
                    Toast.LENGTH_LONG).show();
            resetOrder();
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
                    Log.e("DEBUG", "Unknown pizza type: " +
                            original.getClass().getSimpleName());
                    return null;
            }
        } catch (Exception e) {
            Log.e("DEBUG", "Error in cloning pizza: " + e.getMessage());
            return null;
        }
        return cloned;
    }

    /**
     * Resets the order after a pizza is added to the order, clearing selections
     * and resetting the UI.
     */
    private void resetOrder() {
        quantityEditTextNumber.setText("1");
        if ("Build Your Own".equals(pizzaTypeSpinner.getSelectedItem().toString())) {
            clearToppings();
        }
        selectPizza(pizzaTypeSpinner.getSelectedItem().toString(),
                getCrustType(pizzaTypeSpinner.getSelectedItem().toString()));
    }

    /**
     * Clears all selected toppings from the UI.
     */
    private void clearToppings() {
        toppingsAdapter.resetSelections();
        Log.e("DEBUG", "Reset the toppings");
        updateTotalPrice();
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

    /**
     * Handler for when toppings are selected from the RecyclerView.
     * @param topping The topping item selected in the RecyclerView.
     */
    @Override
    public void selectTopping(Topping topping) {
        currentPizza.addTopping(topping);
        updateTotalPrice();
    }

    /**
     * Handler for when toppings are unselected from the RecyclerView.
     * @param topping The topping item unselected in the RecyclerView.
     */
    @Override
    public void unselectTopping(Topping topping) {
        Log.d("DEBUG", "Current selected pizza in unselectTopping method: " + currentPizza);
        if (currentPizza instanceof BuildYourOwn) {
                currentPizza.removeTopping(topping);
            updateTotalPrice();
        }
    }
}

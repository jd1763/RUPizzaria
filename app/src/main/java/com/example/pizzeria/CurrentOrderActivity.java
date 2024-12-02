package com.example.pizzeria;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rupizzaria.R;

import java.util.ArrayList;

/**
 * Activity displaying the current order and allowing modifications.
 * @author Jorgeluis Done
 */
public class CurrentOrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private OrderManager orderManager;
    private ArrayList<Pizza> currentOrderItems;
    private ListView currentOrderList;
    private EditText subTotalTextField;
    private EditText salesTaxTextField;
    private EditText totalAmountTextField;
    private ArrayAdapter<Pizza> adapter;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentorder);

        // Initialize UI components
        currentOrderList = findViewById(R.id.currentOrderList);
        subTotalTextField = findViewById(R.id.currentOrderSubTotal);
        salesTaxTextField = findViewById(R.id.salesTaxEditText);
        totalAmountTextField = findViewById(R.id.totalAmountEditText);

        // Initialize buttons
        Button placeOrderButton = findViewById(R.id.placeOrderButton);

        // Initialize OrderManager
        orderManager = GlobalDataManager.getInstance().getOrderManager();

        // Initialize observable list and adapter
        currentOrderItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentOrderItems);
        currentOrderList.setAdapter(adapter);

        // Set click listeners
        currentOrderList.setOnItemClickListener(this);
        placeOrderButton.setOnClickListener(v -> placeOrder());

        // Update the view
        updateView();
    }

    /**
     * Updates the view, including the list of current order items and the totals.
     */
    private void updateView() {
        updateObservableList();
        updateTotals();
        adapter.notifyDataSetChanged();
    }

    /**
     * Updates the observable list of order items.
     */
    private void updateObservableList() {
        currentOrderItems.clear();
        currentOrderItems.addAll(orderManager.getCurrentOrder().getPizzas());
    }

    /**
     * Updates the subtotal, sales tax, and total amount displayed in the UI.
     */
    private void updateTotals() {
        double subtotal = OrderManager.calculateSubtotal(orderManager.getCurrentOrder());
        double salesTax = OrderManager.calculateSalesTax(subtotal);
        double totalAmount = OrderManager.calculateTotalAmount(subtotal, salesTax);

        subTotalTextField.setText(String.format(getString(R.string.sub_total_format), subtotal));
        salesTaxTextField.setText(String.format(getString(R.string.sales_tax_format), salesTax));
        totalAmountTextField.setText(String.format(getString(R.string.total_amount_format), totalAmount));
    }

    /**
     * Handles item clicks in the order list. Prompts the user to confirm removal of a selected item.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Pizza");
        builder.setMessage("Are you sure you want to remove this pizza?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Pizza selectedPizza = currentOrderItems.get(position);
            orderManager.getCurrentOrder().removePizza(selectedPizza);
            updateView();
            Toast.makeText(this, "Pizza removed from order.", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.create().show();
    }

    /**
     * Places the current order and updates the view.
     */
    private void placeOrder() {
        if (currentOrderItems.isEmpty()) {
            Toast.makeText(this, "Your order is empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        orderManager.placeCurrentOrder();
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
        updateView();
    }

    /**
     * Navigates back to the main menu activity.
     */
    private void navigateToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

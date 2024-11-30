package com.example.pizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rupizzaria.R;

/**
 * Main activity class for the pizzeria app, serving as the entry point for the user interface.
 * It provides navigation to various parts of the application, including different pizza styles,
 * current order viewing, and order history.
 * @author Jorgeluis Done
 */
public class MainActivity extends AppCompatActivity {
    private OrderManager orderManager;

    /**
     * Called when the activity is starting. Initializes the activity, OrderManager, and UI components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the global OrderManager
        orderManager = GlobalDataManager.getInstance().getOrderManager();

        // Initialize buttons and set click listeners for different sections
        ImageButton chicagoStyleButton = findViewById(R.id.ChicagoStylePizzaButton);
        ImageButton nyStyleButton = findViewById(R.id.NYStylePizzaButton);
        ImageButton currentOrdersButton = findViewById(R.id.CurrentOrdersButton);
        ImageButton placedOrdersButton = findViewById(R.id.PlacedOrdersButton);

        chicagoStyleButton.setOnClickListener(this::openChicagoStyle);
        nyStyleButton.setOnClickListener(this::openNYStyle);
        currentOrdersButton.setOnClickListener(this::openCurrentOrders);
        placedOrdersButton.setOnClickListener(this::openPlacedOrders);

        // Set TextViews for visual clarity
        TextView chicagoText = findViewById(R.id.ChicagoStylePizzaTextView);
        TextView nyText = findViewById(R.id.NYStylePizzaTextView);
        TextView currentOrdersText = findViewById(R.id.CurrentOrdersTextView);
        TextView placedOrdersText = findViewById(R.id.OrdersPlacedTextView);

        chicagoText.setText("Chicago Style Pizza");
        nyText.setText("NY Style Pizza");
        currentOrdersText.setText("Current Orders");
        placedOrdersText.setText("Orders Placed");
    }

    /**
     * Opens the Chicago Style Pizza activity.
     *
     * @param view The view that was clicked.
     */
    private void openChicagoStyle(View view) {
        Intent intent = new Intent(this, ChicagoStyleMenuActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the NY Style Pizza activity.
     *
     * @param view The view that was clicked.
     */
    private void openNYStyle(View view) {
        Intent intent = new Intent(this, NYPizzaStyleMenuActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the Current Orders activity.
     *
     * @param view The view that was clicked.
     */
    private void openCurrentOrders(View view) {
        Intent intent = new Intent(this, CurrentOrderActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the Placed Orders activity.
     *
     * @param view The view that was clicked.
     */
    private void openPlacedOrders(View view) {
        Intent intent = new Intent(this, AllOrdersActivity.class);
        startActivity(intent);
    }
}

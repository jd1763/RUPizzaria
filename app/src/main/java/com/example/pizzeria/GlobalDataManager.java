package com.example.pizzeria;

/**
 * Singleton class for managing global data throughout the pizzeria application.
 */
public class GlobalDataManager {
    private static GlobalDataManager instance;
    private OrderManager orderManager;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the OrderManager.
     */
    private GlobalDataManager() {
        orderManager = new OrderManager();
    }

    /**
     * Get the singleton instance of GlobalDataManager.
     *
     * @return The GlobalDataManager instance.
     */
    public static synchronized GlobalDataManager getInstance() {
        if (instance == null) {
            instance = new GlobalDataManager();
        }
        return instance;
    }

    /**
     * Get the OrderManager instance.
     *
     * @return The OrderManager instance.
     */
    public OrderManager getOrderManager() {
        return orderManager;
    }
}
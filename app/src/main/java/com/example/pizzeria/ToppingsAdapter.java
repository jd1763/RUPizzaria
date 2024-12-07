package com.example.pizzeria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rupizzaria.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for instantiating the adapter for the RecyclerView.
 * @author Frank Garcia
 */
public class ToppingsAdapter extends RecyclerView.Adapter<ToppingsAdapter.ToppingsHolder> {
    private Context context;
    private onToppingsInteractionsListener listener;
    private ArrayList<ToppingItem> toppings;
    private Topping topping;
    private int toppingCount;
    private boolean buttonEnabled = true;
    private AppCompatActivity activityClass;

    /**
     * Constructor for the toppings adapter which takes in the context accessing the adapter
     * and the arraylist of toppings.
     * @param context The context that is calling the ToppingsAdapter to initialize it.
     * @param toppings The toppings that will be initializing the ArrayList.
     */
    public ToppingsAdapter(Context context, ArrayList<ToppingItem>toppings,
                           onToppingsInteractionsListener listener,
                           AppCompatActivity activityClass) {
        this.context = context;
        this.toppings = toppings;
        this.listener = listener;
        this.activityClass = activityClass;
        toppingCount = 0;
    }

    /**
     * Sets the buttonEnabled boolean value to true.
     */
    public void enableSelectButtons() {
        buttonEnabled = true;
    }

    public void disableSelectButtons() {
        buttonEnabled = false;
    }

    /**
     * Updates the strings of the addRemove buttons if its corresponding topping is in the list
     * of toppings.
     */
    public void updateSelections(List<String> toppingsList) {
        for (ToppingItem topping : toppings) {
            if (toppingsList.contains(topping.getToppingName())) topping.setSelected(true);
            else topping.setSelected(false);
            topping.setToppingPrice("");
        }
        notifyDataSetChanged();
    }

    /**
     * Sets the buttonEnabled boolean value to false.
     */
    public void resetSelections() {
        for (ToppingItem topping : toppings) {
            topping.setSelected(false);
            listener.unselectTopping(Topping.fromString(topping.getToppingName()));
            topping.resetPrice();
        }
        toppingCount = 0;
        notifyDataSetChanged();
    }

    /**
     * Method that inflates the row layout for the items in the RecyclerView.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the View item obtained through the inflate method.
     */
    @NonNull
    @Override
    public ToppingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topping_card, parent, false);
        return new ToppingsHolder(view);
    }

    /**
     * Assigns data values for each row according to their "position" when the item becomes
     * visible on the screen and handles their behavior when clicked by the user.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ToppingsHolder holder, int position) {
        ToppingItem toppingItem = toppings.get(position);
        holder.toppingName.setText(toppings.get(position).getToppingName());
        holder.toppingPrice.setText(toppings.get(position).getToppingPrice());
        holder.toppingImage.setImageResource(toppings.get(position).getImage());

        holder.addRemove.setText(toppingItem.isSelected() ? "Selected" : "Unselected");
        holder.addRemove.setClickable(buttonEnabled);

        if (buttonEnabled) {
            holder.addRemove.setOnClickListener(v -> {
                if (toppingItem.isSelected()) {
                    toppingItem.setSelected(false);
                    holder.addRemove.setText("Unselected");
                    listener.unselectTopping(Topping.fromString(toppingItem.getToppingName()));
                    toppingCount--;
                } else {
                    if (toppingCount >= 7) {
                        Toast.makeText(context,
                                "Cannot add anymore toppings. Maximum 7 toppings allowed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        toppingItem.setSelected(true);
                        holder.addRemove.setText("Selected");
                        listener.selectTopping(Topping.fromString(toppingItem.getToppingName()));
                        toppingCount++;
                    }
                }
            });
        }
    }

    /**
     * Returns the number of toppings in the arraylist.
     * @return The number of the toppings in the arraylist.
     */
    @Override
    public int getItemCount() { return toppings.size(); }

    /**
     * Interface for using Activity class to use in order to listen for click interactions for
     * items in the RecyclerView.
     */
    public interface onToppingsInteractionsListener {
        void selectTopping(Topping topping);
        void unselectTopping(Topping topping);
    }

    /**
     * Gets the views from the topping card layout file.
     */
    public class ToppingsHolder extends RecyclerView.ViewHolder {
        private TextView toppingName, toppingPrice;
        private ImageView toppingImage;
        private Button addRemove;
        private ConstraintLayout parentLayout; // the row layout

        /**
         * Initializer for the fields associated with the adapter.
         * @param itemView The views from the topping card layout file.
         */
        public ToppingsHolder(@NonNull View itemView) {
            super(itemView);
            toppingName = itemView.findViewById(R.id.tv_name);
            toppingPrice = itemView.findViewById(R.id.tv_price);
            toppingImage = itemView.findViewById(R.id.tv_image);
            addRemove = itemView.findViewById(R.id.tv_add_remove);
            parentLayout = itemView.findViewById(R.id.row_layout);
        }
    }
}

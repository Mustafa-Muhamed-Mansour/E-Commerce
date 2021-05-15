package com.ecommerce.shopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.model.CartModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CartAdapter extends FirebaseRecyclerAdapter<CartModel, CartAdapter.CartViewHolder>
{

    public static int totalPrice = 0, productTotalPrice;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public CartAdapter(@NonNull FirebaseRecyclerOptions<CartModel> options)
    {
        super(options);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartModel model)
    {
        holder.cartNameTextView.setText(model.getProduct_Name());
        holder.cartPriceTextView.setText("Price = " + model.getProduct_Price() + " £");
        holder.cartQuantityTextView.setText("Quantity = " + model.getProduct_Quantity());
        holder.cartDiscountTextView.setText(model.getProduct_Discount());

        productTotalPrice = ((Integer.parseInt(model.getProduct_Price()))) * Integer.parseInt(model.getProduct_Quantity());

        totalPrice = totalPrice + productTotalPrice;

    }

    public class CartViewHolder extends RecyclerView.ViewHolder
    {

        TextView cartNameTextView, cartPriceTextView, cartQuantityTextView, cartDiscountTextView;

        public CartViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cartNameTextView = itemView.findViewById(R.id.txt_cart_item_name);
            cartPriceTextView = itemView.findViewById(R.id.txt_cart_item_price);
            cartQuantityTextView = itemView.findViewById(R.id.txt_cart_item_quantity);
            cartDiscountTextView = itemView.findViewById(R.id.txt_cart_item_discoount);
        }
    }
}

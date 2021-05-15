package com.ecommerce.shopping.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.model.OrderModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel, OrderAdapter.OrderViewHolder>
{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");

    public OrderAdapter(@NonNull FirebaseRecyclerOptions<OrderModel> options)
    {
        super(options);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_item, parent, false);
        return new OrderViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderModel model)
    {
        holder.textViewFullName.setText(model.getOrder_Full_Name());
        holder.textViewPhoneNumber.setText(model.getOrder_Phone_Number());
        holder.textViewHomeAddress.setText("Shipping Address = " + model.getOrder_Home_Address());
        holder.textViewCity.setText("City = " + model.getOrder_City());
        holder.textViewTotalPrice.setText("Total Price = " + model.getOrder_Total_Amount() + " £");
        holder.textViewDate.setText("Date = " + model.getOrder_Date());
        holder.textViewTime.setText("Time = " + model.getOrder_Time());
        holder.buttonShowOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // setFragment(new SlideshowFragment());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CharSequence sequence [] = new CharSequence[]
                        {
                                "Yes",
                                "No"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Have You shipped this order product?");
                builder.setItems(sequence, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which == 0)
                        {
                            String U_ID = getRef(position).getKey();
                            orderReference.child(U_ID).removeValue();
                        }
                        else
                        {
                            Navigation.findNavController(v).navigate(R.id.signInFragment);
                        }
                    }
                });

                builder.show();
            }
        });
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewFullName, textViewPhoneNumber, textViewHomeAddress;
        TextView textViewCity, textViewTotalPrice, textViewDate;
        TextView textViewTime;
        Button buttonShowOrder;

        public OrderViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.txt_new_order_item_full_name);
            textViewPhoneNumber = itemView.findViewById(R.id.txt_new_order_item_phone_number);
            textViewHomeAddress = itemView.findViewById(R.id.txt_new_order_item_home_address);
            textViewCity = itemView.findViewById(R.id.txt_new_order_item_city);
            textViewTotalPrice = itemView.findViewById(R.id.txt_new_order_item_total_price);
            textViewDate = itemView.findViewById(R.id.txt_new_order_item_date);
            textViewTime = itemView.findViewById(R.id.txt_new_order_item_time);
            buttonShowOrder = itemView.findViewById(R.id.btn_show_order);
        }
    }

}

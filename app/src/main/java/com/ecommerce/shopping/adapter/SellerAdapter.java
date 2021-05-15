package com.ecommerce.shopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.model.SellerModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerAdapter extends FirebaseRecyclerAdapter<SellerModel, SellerAdapter.SellerViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public SellerAdapter(@NonNull FirebaseRecyclerOptions<SellerModel> options)
    {
        super(options);
    }


    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item, parent, false);
        return new SellerViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull SellerModel model)
    {
        holder.productNameTextView.setText(model.getProduct_Seller_Name());
        holder.productDescriptionTextView.setText(model.getProduct_Seller_Description());
        holder.productPriceTextView.setText(model.getProduct_Seller_Price() + " £");
        Picasso.get()
                .load(model.getProduct_Seller_Image())
                .into(holder.productImageView);
        holder.imageViewFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(holder.itemView.getContext(), "Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class SellerViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView productImageView;
        TextView productNameTextView, productDescriptionTextView, productPriceTextView;
        ImageView imageViewFavorite;

        public SellerViewHolder(@NonNull View itemView)
        {
            super(itemView);

            productImageView = itemView.findViewById(R.id.seller_item_image);
            productNameTextView = itemView.findViewById(R.id.txt_seller_item_name);
            productDescriptionTextView = itemView.findViewById(R.id.txt_seller_item_description);
            productPriceTextView = itemView.findViewById(R.id.txt_seller_item_price);
            imageViewFavorite = itemView.findViewById(R.id.img_seller_item_favorite);
        }
    }
}

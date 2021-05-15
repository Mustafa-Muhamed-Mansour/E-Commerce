package com.ecommerce.shopping.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends FirebaseRecyclerAdapter<ProductModel, ProductAdapter.ProdctViewHolder>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<ProductModel> options)
    {
        super(options);
    }


    @NonNull
    @Override
    public ProdctViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProdctViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProdctViewHolder holder, int position, @NonNull ProductModel model)
    {
        holder.productNameTextView.setText(model.getProduct_Name());
        holder.productDescriptionTextView.setText(model.getProduct_Description());
        holder.productPriceTextView.setText(model.getProduct_Price() + " £");
        Picasso.get()
                .load(model.getProduct_Image())
                .into(holder.productImageView);
        holder.imageViewFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(holder.itemView.getContext(), "Comming Soon", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundleData = new Bundle();
                bundleData.putString("Product Name", model.getProduct_Name());
                bundleData.putString("Product Description", model.getProduct_Description());
                bundleData.putString("Product Price", model.getProduct_Price());
                bundleData.putString("Product Image", model.getProduct_Image());
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_productDetailsFragment, bundleData);
            }
        });
    }

    public class ProdctViewHolder extends RecyclerView.ViewHolder
    {

        CircleImageView productImageView;
        TextView productNameTextView, productDescriptionTextView, productPriceTextView;
        ImageView imageViewFavorite;

        public ProdctViewHolder(@NonNull View itemView)
        {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_item_image);
            productNameTextView = itemView.findViewById(R.id.txt_product_item_name);
            productDescriptionTextView = itemView.findViewById(R.id.txt_product_item_description);
            productPriceTextView = itemView.findViewById(R.id.txt_product_item_price);
            imageViewFavorite = itemView.findViewById(R.id.img_product_item_favorite);
        }
    }
}

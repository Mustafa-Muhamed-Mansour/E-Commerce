package com.ecommerce.shopping.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.model.SellerModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MaintainProductAdapter extends FirebaseRecyclerAdapter<SellerModel, MaintainProductAdapter.ProductVH>
{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public MaintainProductAdapter(@NonNull FirebaseRecyclerOptions<SellerModel> options)
    {
        super(options);
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item, parent, false);
        return new ProductVH(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductVH holder, int position, @NonNull SellerModel model)
    {
        holder.productNameTextView.setText(model.getProduct_Seller_Name());
        holder.productDescriptionTextView.setText(model.getProduct_Seller_Description());
        holder.productPriceTextView.setText(model.getProduct_Seller_Price() + " £");
        Picasso.get()
                .load(model.getProduct_Seller_Image())
                .into(holder.productImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle editProduct = new Bundle();
                editProduct.putString("Edit_U_ID", getRef(position).getKey());
                editProduct.putString("Edit_Product_Name", model.getProduct_Seller_Name());
                editProduct.putString("Edit_Product_Description", model.getProduct_Seller_Description());
                editProduct.putString("Edit_Product_Price", model.getProduct_Seller_Price());
                editProduct.putString("Edit_Product_Image", model.getProduct_Seller_Image());
                Navigation.findNavController(v).navigate(R.id.action_maintainProductFragment_to_changeProductFragment, editProduct);
            }

        });
    }

    public class ProductVH extends RecyclerView.ViewHolder
    {
        CircleImageView productImageView;
        TextView productNameTextView, productDescriptionTextView, productPriceTextView;


        public ProductVH(@NonNull View itemView)
        {
            super(itemView);

            productImageView = itemView.findViewById(R.id.seller_item_image);
            productNameTextView = itemView.findViewById(R.id.txt_seller_item_name);
            productDescriptionTextView = itemView.findViewById(R.id.txt_seller_item_description);
            productPriceTextView = itemView.findViewById(R.id.txt_seller_item_price);
        }
    }
}

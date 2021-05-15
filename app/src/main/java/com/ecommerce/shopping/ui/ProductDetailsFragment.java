package com.ecommerce.shopping.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ecommerce.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailsFragment extends Fragment
{

    View root;
    NavController navController;
    CircleImageView circleImageProductDetails;
    TextView textViewProductNameDetails, textViewProductDescriptionDetails, textViewProductPriceDetails;
    ElegantNumberButton elegantNumberButtonProduct;
    FloatingActionButton fabAddToCart;
    String randomKey;
    FirebaseAuth firebaseAuth;
    DatabaseReference cartListReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_product_details, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        initDB();
        clickView();

    }

    private void initViews()
    {
        circleImageProductDetails = root.findViewById(R.id.img_product_details);
        textViewProductNameDetails = root.findViewById(R.id.txt_view_product_name_details);
        textViewProductDescriptionDetails = root.findViewById(R.id.txt_view_product_description_details);
        textViewProductPriceDetails = root.findViewById(R.id.txt_view_product_price_details);
        elegantNumberButtonProduct = root.findViewById(R.id.elegant_number_btn);
        fabAddToCart = root.findViewById(R.id.fab_add_to_cart);
        textViewProductNameDetails.setText(getArguments().getString("Product Name"));
        textViewProductDescriptionDetails.setText(getArguments().getString("Product Description"));
        textViewProductPriceDetails.setText(getArguments().getString("Product Price"));
        Picasso.get().load(getArguments().getString("Product Image")).into(circleImageProductDetails);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        cartListReference = FirebaseDatabase.getInstance().getReference();
        randomKey = FirebaseDatabase.getInstance().getReference().push().getKey();
    }

    private void clickView()
    {
        fabAddToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("ID", firebaseAuth.getUid());
                cartMap.put("Product_Name", textViewProductNameDetails.getText().toString());
                cartMap.put("Product_Description", textViewProductDescriptionDetails.getText().toString());
                cartMap.put("Product_Price", textViewProductPriceDetails.getText().toString());
                cartMap.put("Product_Image", circleImageProductDetails.toString());
                cartMap.put("Product_Quantity", elegantNumberButtonProduct.getNumber());
                cartMap.put("Product_Discount", "");

                cartListReference
                        .child("Cart List")
                        .child(firebaseAuth.getUid())
                        .child(randomKey)
                        .setValue(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(root.getContext(), "Added To Cart List", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.action_productDetailsFragment_to_mainFragment);
                                }

                                else
                                {
                                    Toast.makeText(root.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
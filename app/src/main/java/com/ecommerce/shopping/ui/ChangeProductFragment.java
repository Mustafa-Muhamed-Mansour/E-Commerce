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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ecommerce.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProductFragment extends Fragment
{
    View root;
    NavController navController;
    EditText editTextProductName, editTextProductDescription, editTextProductPrice;
    CircleImageView circleEditImageProduct;
    ImageButton imageButtonGOBackEditAdmin;
    Button buttonApplyChange;
    String productName, productDescription, productPrice, productImage, randomKey, U_ID;
    FirebaseAuth firebaseAuth;
    DatabaseReference editProductReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_change_product, container, false);
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
        imageButtonGOBackEditAdmin = root.findViewById(R.id.img_btn_go_back_edit_admin);
        editTextProductName = root.findViewById(R.id.edit_product_name);
        editTextProductDescription = root.findViewById(R.id.edit_product_description);
        editTextProductPrice = root.findViewById(R.id.edit_product_price);
        circleEditImageProduct = root.findViewById(R.id.img_edit_product);
        Bundle getDataBundle = getArguments();
        productName = getDataBundle.getString("Edit_Product_Name");
        editTextProductName.setText(productName);
        productDescription = getDataBundle.getString("Edit_Product_Description");
        editTextProductDescription.setText(productDescription);
        productPrice = getDataBundle.getString("Edit_Product_Price");
        editTextProductPrice.setText(productPrice);
        productImage = getDataBundle.getString("Edit_Product_Image");
        Picasso.get()
                .load(productImage)
                .into(circleEditImageProduct);
        U_ID = getDataBundle.getString("Edit_U_ID");
        buttonApplyChange = root.findViewById(R.id.btn_apply_change);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        editProductReference = FirebaseDatabase.getInstance().getReference();
        randomKey = editProductReference.push().getKey();
    }

    private void clickView()
    {
        imageButtonGOBackEditAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().onBackPressed();
            }
        });

        buttonApplyChange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nameField = editTextProductName.getText().toString();
                String descriptionField = editTextProductDescription.getText().toString();
                String priceField = editTextProductPrice.getText().toString();


                HashMap<String , Object> updateData = new HashMap<>();
                updateData.put("ID", randomKey);
                updateData.put("Product_Seller_Name", nameField);
                updateData.put("Product_Seller_Description", descriptionField);
                updateData.put("Product_Seller_Price", priceField);
                updateData.put("Product_Seller_Image", productImage);

                editProductReference
                        .child("Products Seller")
                        .child(U_ID)
                        .setValue(updateData)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(root.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
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
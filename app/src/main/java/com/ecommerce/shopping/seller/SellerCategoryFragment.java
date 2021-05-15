package com.ecommerce.shopping.seller;

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

import com.ecommerce.shopping.R;
import com.google.firebase.auth.FirebaseAuth;

public class SellerCategoryFragment extends Fragment
{
    View root;
    NavController navController;
    Button buttonMaintainProduct, buttonCheckOrder, buttonLogOUT;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_seller_category, container, false);
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
        clickViews();

    }

    private void initViews()
    {
        buttonMaintainProduct = root.findViewById(R.id.btn_maintain_product);
        buttonCheckOrder = root.findViewById(R.id.btn_check_orders);
        buttonLogOUT = root.findViewById(R.id.btn_log_out);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
    }


    private void clickViews()
    {

        buttonMaintainProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_sellerCategoryFragment_to_maintainProductFragment);
            }
        });
        buttonCheckOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_sellerCategoryFragment_to_checkOrderFragment);
            }
        });
        buttonLogOUT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_sellerCategoryFragment_to_signInFragment);
                firebaseAuth.signOut();
            }
        });
    }


}
package com.ecommerce.shopping.seller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.adapter.SellerAdapter;
import com.ecommerce.shopping.model.SellerModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerMainFragment extends Fragment
{
    View root;
    NavController navController;
    BottomNavigationView navView;
    FirebaseAuth firebaseAuth;
    DatabaseReference sellerDatabaseReference;
    RecyclerView recyclerViewSeller;
    SellerAdapter sellerAdapter;
    FirebaseRecyclerOptions<SellerModel> options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_seller_main, container, false);
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
        navView = root.findViewById(R.id.bottom_nav_view);
        recyclerViewSeller = root.findViewById(R.id.r_v_seller);
        recyclerViewSeller.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));
        recyclerViewSeller.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        sellerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products Seller");

        options = new FirebaseRecyclerOptions
                .Builder<SellerModel>()
                .setQuery(sellerDatabaseReference, SellerModel.class)
                .build();
        sellerAdapter = new SellerAdapter(options);
    }

    private void clickView()
    {
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        navController.navigate(R.id.action_sellerMainFragment_self);
                        break;
                    case R.id.navigation_add:
                        navController.navigate(R.id.action_sellerMainFragment_to_sellerAddNewProductFragment);
                        break;
                    case R.id.navigation_logout:
                        firebaseAuth.signOut();
                        getActivity().finish();
                        break;
                    default:
                        Toast.makeText(root.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();

        recyclerViewSeller.setAdapter(sellerAdapter);
        sellerAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        sellerAdapter.stopListening();
    }
}
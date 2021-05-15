package com.ecommerce.shopping.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.adapter.OrderAdapter;
import com.ecommerce.shopping.model.OrderModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CheckOrderFragment extends Fragment
{

    View root;
    NavController navController;
    RecyclerView recyclerView;
    DatabaseReference orderReference;
    FirebaseAuth firebaseAuth;
    OrderAdapter orderAdapter;
    FirebaseRecyclerOptions<OrderModel> options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_check_order, container, false);
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


    }

    private void initViews()
    {
        recyclerView = root.findViewById(R.id.recycler_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");

        options = new FirebaseRecyclerOptions
                .Builder<OrderModel>()
                .setQuery(orderReference, OrderModel.class)
                .build();
        orderAdapter = new OrderAdapter(options);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        recyclerView.setAdapter(orderAdapter);
        orderAdapter.startListening();
    }
}
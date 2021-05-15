package com.ecommerce.shopping.ui;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.adapter.MaintainProductAdapter;
import com.ecommerce.shopping.model.SellerModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MaintainProductFragment extends Fragment
{

    View root;
    NavController navController;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, productDatabaseReference;
    ImageButton imageButtonBack;
    RecyclerView recyclerViewMaintainProduct;
    MaintainProductAdapter maintainProductAdapter;
    FirebaseRecyclerOptions<SellerModel> options;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_maintain_product, container, false);
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

        getActivity().setTitle(null);

        initViews();
        initDB();
        clickView();


  }

    private void initViews()
    {
        imageButtonBack = root.findViewById(R.id.img_btn_back);
        recyclerViewMaintainProduct = root.findViewById(R.id.recycler_maintain_product);
        recyclerViewMaintainProduct.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewMaintainProduct.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products Seller");

        options = new FirebaseRecyclerOptions
                .Builder<SellerModel>()
                .setQuery(productDatabaseReference, SellerModel.class)
                .build();
        maintainProductAdapter = new MaintainProductAdapter(options);
        maintainProductAdapter.stopListening();
    }

    private void clickView()
    {
        imageButtonBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                maintainProductAdapter.stopListening();
                navController.navigate(R.id.action_maintainProductFragment_to_sellerCategoryFragment);
                imageButtonBack.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onStart()
    {
        super.onStart();

        recyclerViewMaintainProduct.setAdapter(maintainProductAdapter);
        maintainProductAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        maintainProductAdapter.stopListening();
    }

}
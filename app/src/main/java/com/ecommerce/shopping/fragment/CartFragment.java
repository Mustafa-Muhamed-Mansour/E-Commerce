package com.ecommerce.shopping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.shopping.adapter.CartAdapter;
import com.ecommerce.shopping.model.CartModel;
import com.ecommerce.shopping.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartFragment extends Fragment
{
    View root;
    NavController navController;
    Button buttonNextShipmentDetails;
    RecyclerView recyclerCartView;
    DatabaseReference cartReference;
    CartAdapter cartAdapter;
    FirebaseRecyclerOptions<CartModel> options;
    FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         root = inflater.inflate(R.layout.fragment_cart, container, false);
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

        initView();
        initDB();
        clickView();

    }


    private void initView()
    {
        recyclerCartView = root.findViewById(R.id.recycler_cart);
        recyclerCartView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerCartView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        buttonNextShipmentDetails = root.findViewById(R.id.btn_next_shipment);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child(firebaseAuth.getCurrentUser().getUid());

        CartAdapter.totalPrice = 0;
        options = new FirebaseRecyclerOptions
                .Builder<CartModel>()
                .setQuery(cartReference, CartModel.class)
                .build();
        cartAdapter = new CartAdapter(options);
    }

    private void clickView()
    {
        buttonNextShipmentDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("totalPrice", String.valueOf(CartAdapter.totalPrice));
                navController.navigate(R.id.action_cartFragment_to_orderFragment, bundle);
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();

        recyclerCartView.setAdapter(cartAdapter);
        cartAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        cartAdapter.stopListening();
    }
}
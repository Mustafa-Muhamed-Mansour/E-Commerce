package com.ecommerce.shopping.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.adapter.ProductAdapter;
import com.ecommerce.shopping.model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchFragment extends Fragment
{
    View view;
    EditText editTextSearch;
    RecyclerView recyclerViewSearch;
    ProductAdapter productAdapter;
    FirebaseRecyclerOptions<ProductModel> options;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        initView();
        searchView();


    }

    private void initView()
    {
        editTextSearch = view.findViewById(R.id.edit_txt_search);
        recyclerViewSearch = view.findViewById(R.id.r_v_search);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
    }

    private void searchView()
    {
        editTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString() != null)
                {
                    loadData(s.toString());
                }
            }
        });
    }

    private void loadData(String date)
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Product_Name").startAt(date).endAt(date + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(query, ProductModel.class).build();
        productAdapter = new ProductAdapter(options);
        recyclerViewSearch.setAdapter(productAdapter);
        productAdapter.startListening();
    }

}
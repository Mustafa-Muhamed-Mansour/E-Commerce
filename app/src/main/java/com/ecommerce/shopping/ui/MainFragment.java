package com.ecommerce.shopping.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.shopping.R;
import com.ecommerce.shopping.adapter.ProductAdapter;
import com.ecommerce.shopping.model.ProductModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainFragment extends Fragment
{

    View root;
    NavController navController;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, productDatabaseReference;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    FirebaseRecyclerOptions<ProductModel> options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_main, container, false);
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
        retriveView();

    }

    private void initViews()
    {
        toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        drawer = root.findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = root.findViewById(R.id.nav_view);
        recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2, RecyclerView.VERTICAL, false));
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        options = new FirebaseRecyclerOptions
                .Builder<ProductModel>()
                .setQuery(productDatabaseReference, ProductModel.class)
                .build();
        productAdapter = new ProductAdapter(options);
    }

    private void clickView()
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        navController.navigate(R.id.action_mainFragment_self);
                        break;

                    case R.id.nav_cart:
                        navController.navigate(R.id.action_mainFragment_to_cartFragment);
                        productAdapter.stopListening();
                        toolbar.setTitle("Cart");
                        break;
                    case R.id.nav_search:
                        navController.navigate(R.id.action_mainFragment_to_searchFragment);
                        productAdapter.stopListening();
                        toolbar.setTitle("Search");
                        break;
                    case R.id.nav_favorite:
                        Toast.makeText(root.getContext(), "Comming Soon", Toast.LENGTH_SHORT).show();
                        productAdapter.stopListening();
                        toolbar.setTitle("The Favorite");
                        break;
                    case R.id.nav_settings:
                        navController.navigate(R.id.action_mainFragment_to_settingsFragment);
                        productAdapter.stopListening();
                        toolbar.setTitle("Setting");
                        break;
                    case R.id.nav_logout:
                        alertDialog();
                        break;
                    default:
                        Toast.makeText(root.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void retriveView()
    {

        View headerView = navigationView.getHeaderView(0);
        CircleImageView circleImageView = headerView.findViewById(R.id.circle_user_profile_picture);
        TextView textViewUserName = headerView.findViewById(R.id.txt_view_user_profile_name);
        TextView textViewUserEmail = headerView.findViewById(R.id.txt_view_user_profile_email);

        databaseReference
                .child("Create Account User")
                .child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if ((snapshot.hasChild("name")) && (snapshot.hasChild("email")) && (snapshot.hasChild("image")))
                        {
                            String name = snapshot.child("name").getValue().toString();
                            textViewUserName.setText(name);
                            String email = snapshot.child("email").getValue().toString();
                            textViewUserEmail.setText(email);
                            String image = snapshot.child("image").getValue().toString();
                            Picasso
                                    .get()
                                    .load(image)
                                    .into(circleImageView);
                        }
                        else
                        {
                            textViewUserName.setText("What's your name ?");
                            textViewUserEmail.setText("What's your email ?");
                            Picasso
                                    .get()
                                    .load("No Photo");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(root.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void alertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setTitle("Are you Sure to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        navController.navigate(R.id.action_mainFragment_to_signInFragment);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            // any view in layout in menu is called (main)
           /* case R.id.:
                Toast.makeText(this, "this is Bla Bla Bla", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break; */
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        recyclerView.setAdapter(productAdapter);
        productAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        productAdapter.stopListening();
    }
}
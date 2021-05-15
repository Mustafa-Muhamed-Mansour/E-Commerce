package com.ecommerce.shopping.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ecommerce.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class OrderFragment extends Fragment
{
    View root;
    NavController navController;
    ImageButton imageGoBack;
    EditText editTextYourName, editTextYourPhoneNumber, editTextYourHomeAddress, editTextYourCityName;
    Button buttonCofirmOrder;
    String totalprice, randomKEY, saveCurrentTime, saveCurrentDate;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, orderReference, cartListReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_order, container, false);
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
        clickViews();
        getBundle();
        initCalendar();


    }

    private void initViews()
    {
        imageGoBack = root.findViewById(R.id.img_btn_go_back_gallery);
        editTextYourName = root.findViewById(R.id.edit_txt_name_confirm_order);
        editTextYourPhoneNumber = root.findViewById(R.id.edit_txt_phone_number_confirm_order);
        editTextYourHomeAddress = root.findViewById(R.id.edit_txt_address_confirm_order);
        editTextYourCityName = root.findViewById(R.id.edit_txt_city_confirm_order);
        buttonCofirmOrder = root.findViewById(R.id.btn_confirm_order);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        orderReference = FirebaseDatabase.getInstance().getReference();
        cartListReference = FirebaseDatabase.getInstance().getReference();
        randomKEY = databaseReference.push().getKey();
    }

    private void clickViews()
    {
        imageGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_orderFragment_to_cartFragment);
            }
        });

        buttonCofirmOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fullNameField = editTextYourName.getText().toString();
                String phoneNumberField = editTextYourPhoneNumber.getText().toString();
                String homeAddressField = editTextYourHomeAddress.getText().toString();
                String cityField = editTextYourCityName.getText().toString();

                if (TextUtils.isEmpty(fullNameField))
                {
                    Toast.makeText(root.getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    editTextYourName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phoneNumberField))
                {
                    Toast.makeText(root.getContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    editTextYourPhoneNumber.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(homeAddressField))
                {
                    Toast.makeText(root.getContext(), "Please enter your home address", Toast.LENGTH_SHORT).show();
                    editTextYourHomeAddress.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cityField))
                {
                    Toast.makeText(root.getContext(), "Please enter your city", Toast.LENGTH_SHORT).show();
                    editTextYourCityName.requestFocus();
                    return;
                }
                else
                {
                    HashMap<String, Object> orderMap = new HashMap<>();
                    orderMap.put("ID", firebaseAuth.getUid());
                    orderMap.put("Order_Total_Amount", totalprice);
                    orderMap.put("Order_Full_Name", fullNameField);
                    orderMap.put("Order_Phone_Number", phoneNumberField);
                    orderMap.put("Order_Home_Address", homeAddressField);
                    orderMap.put("Order_City", cityField);
                    orderMap.put("Order_Date", saveCurrentDate);
                    orderMap.put("Order_Time", saveCurrentTime);

                    orderReference
                            .child("Orders")
                            .child(randomKEY)
                            .setValue(orderMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(root.getContext(), "The order has been successfully added", Toast.LENGTH_SHORT).show();
                                        navController.navigate(R.id.action_orderFragment_to_mainFragment);
                                    }
                                    else
                                    {
                                        Toast.makeText(root.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void getBundle()
    {
        Bundle bundle = getArguments();
        totalprice = bundle.getString("totalPrice");
        Toast.makeText(root.getContext(), totalprice, Toast.LENGTH_SHORT).show();
    }

    private void initCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat(" MMM:dd:yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat(" hh-mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }
}
package com.ecommerce.shopping.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminFragment extends Fragment
{
    View root;
    NavController navController;
    EditText editTextEmailAdmin, editTextPasswordAdmin;
    Button buttonSignInAdmin;
    ImageButton imageButtonBack;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_admin, container, false);
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
        editTextEmailAdmin = root.findViewById(R.id.edit_txt_email_sign_in_admin);
        editTextPasswordAdmin = root.findViewById(R.id.edit_txt_password_sign_in_admin);
        buttonSignInAdmin = root.findViewById(R.id.btn_sign_in_admin);
        imageButtonBack = root.findViewById(R.id.img_btn_go_back);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void clickViews()
    {
        buttonSignInAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String emailFieldAdmin = editTextEmailAdmin.getText().toString();
                String passwordFieldAdmin = editTextPasswordAdmin.getText().toString();

                if (TextUtils.isEmpty(emailFieldAdmin))
                {
                    editTextEmailAdmin.setError(getString(R.string.please_enter_your_email));
                    editTextEmailAdmin.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(passwordFieldAdmin))
                {
                    editTextPasswordAdmin.setError(getString(R.string.please_enter_your_password));
                    editTextPasswordAdmin.requestFocus();
                    return;
                }

                else
                {
                    firebaseAuth
                            .signInWithEmailAndPassword(emailFieldAdmin, passwordFieldAdmin)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        HashMap<String, Object> userData = new HashMap<>();
                                        userData.put("ID", task.getResult().getUser().getUid());
                                        userData.put("email", emailFieldAdmin);
                                        databaseReference
                                                .child("Account Admin")
                                                .child(task.getResult().getUser().getUid())
                                                .setValue(userData);
                                        navController.navigate(R.id.action_adminFragment_to_sellerCategoryFragment);
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

        imageButtonBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_adminFragment_to_signInFragment);
            }
        });
    }

}
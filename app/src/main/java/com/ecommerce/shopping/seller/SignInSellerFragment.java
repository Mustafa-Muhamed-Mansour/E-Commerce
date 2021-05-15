package com.ecommerce.shopping.seller;

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

public class SignInSellerFragment extends Fragment
{
    View root;
    NavController navController;
    EditText editTextEmail, editTextPassword;
    ImageButton imageButtonSellerSignIn;
    Button buttonLogIn;
    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_sign_in_seller, container, false);
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
        editTextEmail = root.findViewById(R.id.edit_txt_email_sign_in_seller);
        editTextPassword = root.findViewById(R.id.edit_txt_password_sign_in_seller);
        buttonLogIn = root.findViewById(R.id.btn_sign_in_seller);
        imageButtonSellerSignIn = root.findViewById(R.id.img_btn_close_sign_in_seller);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void clickView()
    {
        imageButtonSellerSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signInSellerFragment_to_signUpSellerFragment);
            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String emailField = editTextEmail.getText().toString();
                String passwordField = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(emailField))
                {
                    editTextEmail.setError(getString(R.string.please_enter_your_email));
                    editTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(passwordField))
                {
                    editTextPassword.setError(getString(R.string.please_enter_your_password));
                    editTextPassword.requestFocus();
                    return;
                }

                else
                {
                    firebaseAuth
                            .signInWithEmailAndPassword(emailField, passwordField)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        navController.navigate(R.id.action_signInSellerFragment_to_sellerMainFragment);
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
}
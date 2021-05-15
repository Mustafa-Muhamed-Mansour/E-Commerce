package com.ecommerce.shopping.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ecommerce.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignInFragment extends Fragment
{

    View root;
    NavController navController;
    ImageButton imageButtonCloseSignIn;
    EditText editTextEmailSignIn, editTextPasswordSignIn;
    TextView textViewDontHaveAccount, textViewAdmin, textViewSeller;
    Button buttonSignIn;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_sign_in, container, false);
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
        initProgress();
        initDB();
        clickViews();


    }

    private void initViews()
    {
        imageButtonCloseSignIn = root.findViewById(R.id.img_btn_close_sign_in);
        editTextEmailSignIn = root.findViewById(R.id.edit_txt_email_sign_in);
        editTextPasswordSignIn = root.findViewById(R.id.edit_txt_password_sign_in);
        textViewDontHaveAccount = root.findViewById(R.id.txt_view_sign_up);
        textViewSeller = root.findViewById(R.id.txt_view_seller);
        buttonSignIn = root.findViewById(R.id.btn_sign_in);
        textViewAdmin = root.findViewById(R.id.txt_view_admin);
    }

    private void initProgress()
    {
        progressDialog = new ProgressDialog(root.getContext());
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void clickViews()
    {
        textViewSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signInFragment_to_signUpSellerFragment);
            }
        });

        textViewDontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAccount();
            }
        });

        imageButtonCloseSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            }
        });

        textViewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signInFragment_to_adminFragment);
            }
        });
    }

    private void signInAccount()
    {
        String emailField = editTextEmailSignIn.getText().toString();
        String passwordField = editTextPasswordSignIn.getText().toString();

        if (TextUtils.isEmpty(emailField)) {
            editTextEmailSignIn.setError(getString(R.string.please_enter_your_email));
            editTextEmailSignIn.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwordField)) {
            editTextPasswordSignIn.setError(getString(R.string.please_enter_your_password));
            editTextPasswordSignIn.requestFocus();
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
                                navController.navigate(R.id.action_signInFragment_to_mainFragment);

                            }
                            else
                            {
                                progressDialog.cancel();
                                Toast.makeText(root.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
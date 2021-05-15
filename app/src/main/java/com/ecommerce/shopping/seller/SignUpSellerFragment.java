package com.ecommerce.shopping.seller;

import android.app.ProgressDialog;
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

public class SignUpSellerFragment extends Fragment
{
    View root;
    NavController navController;

    EditText editTextName, editTextPhone, editTextEmail, editTextPassword, editTextAddress;
    Button buttonRegister, buttonHaveAccount;
    ImageButton imageButtonSellerSignUp;
    FirebaseAuth firebaseAuth;
    DatabaseReference sellerSignUpReference;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_sign_up_seller, container, false);
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
        editTextName = root.findViewById(R.id.edit_txt_name_sign_up_seller);
        editTextPhone = root.findViewById(R.id.edit_txt_phone_sign_up_seller);
        editTextEmail = root.findViewById(R.id.edit_txt_email_sign_up_seller);
        editTextPassword = root.findViewById(R.id.edit_txt_password_sign_up_seller);
        editTextAddress = root.findViewById(R.id.edit_txt_address_sign_up_seller);
        imageButtonSellerSignUp = root.findViewById(R.id.img_btn_close_sign_up_seller);
        buttonRegister = root.findViewById(R.id.btn_register_seller);
        buttonHaveAccount = root.findViewById(R.id.btn_have_account_seller);
    }

    private void initProgress()
    {
        progressDialog = new ProgressDialog(root.getContext());
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        sellerSignUpReference = FirebaseDatabase.getInstance().getReference();
    }

    private void clickViews()
    {
        imageButtonSellerSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signUpSellerFragment_to_signInFragment);
            }
        });

        buttonHaveAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signUpSellerFragment_to_signInSellerFragment);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nameField = editTextName.getText().toString();
                String phoneField = editTextPhone.getText().toString();
                String emailField = editTextEmail.getText().toString();
                String passwordField = editTextPassword.getText().toString();
                String addressField = editTextAddress.getText().toString();

                if (TextUtils.isEmpty(nameField))
                {
                    editTextName.setError(getString(R.string.please_enter_your_name));
                    editTextName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(phoneField))
                {
                    editTextPhone.setError(getString(R.string.please_enter_your_mobile_phone));
                    editTextPhone.requestFocus();
                    return;
                }

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

                if (TextUtils.isEmpty(addressField))
                {
                    editTextAddress.setError(getString(R.string.please_enter_your_address));
                    editTextAddress.requestFocus();
                    return;
                }

                else
                {
                    progressDialog.setTitle("New Account");
                    progressDialog.setMessage("Please Wait while we are creating a new account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    firebaseAuth
                            .createUserWithEmailAndPassword(emailField, passwordField)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        HashMap<String, Object> addSellerData = new HashMap<>();

                                        addSellerData.put("ID", task.getResult().getUser().getUid());
                                        addSellerData.put("name", nameField);
                                        addSellerData.put("mobile phone", phoneField);
                                        addSellerData.put("email", emailField);
                                        addSellerData.put("password", passwordField);
                                        addSellerData.put("address", addressField);

                                        sellerSignUpReference
                                                .child("Create Account Seller")
                                                .child(task.getResult().getUser().getUid())
                                                .setValue(addSellerData);
                                        navController.navigate(R.id.action_signUpSellerFragment_to_sellerMainFragment);
                                        getActivity().finish();
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
        });
    }
}
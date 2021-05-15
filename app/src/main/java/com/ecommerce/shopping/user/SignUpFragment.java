package com.ecommerce.shopping.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class SignUpFragment extends Fragment
{

    View root;
    NavController navController;
    EditText editTextEmailSignUp, editTextFullNameSignUp, editTextPasswordSignUp, editTextConfirmPasswordSignUp;
    TextView textViewHaveAccount;
    Button buttonSignUp;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_sign_up, container, false);
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
        editTextEmailSignUp = root.findViewById(R.id.edit_txt_mail_sign_up);
        editTextFullNameSignUp = root.findViewById(R.id.edit_txt_full_name_sign_up);
        editTextPasswordSignUp = root.findViewById(R.id.edit_txt_password_sign_up);
        editTextConfirmPasswordSignUp = root.findViewById(R.id.edit_txt_confirm_password_sign_up);
        textViewHaveAccount = root.findViewById(R.id.txt_view_sign_in);
        buttonSignUp = root.findViewById(R.id.btn_sign_up);
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
        textViewHaveAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createAccount();
            }
        });

    }

    private void createAccount()
    {

        String emailField = editTextEmailSignUp.getText().toString();
        String fullNameField = editTextFullNameSignUp.getText().toString();
        String passwordField = editTextPasswordSignUp.getText().toString();
        String confirmPasswordField = editTextConfirmPasswordSignUp.getText().toString();

        if (TextUtils.isEmpty(emailField))
        {
            editTextEmailSignUp.setError(getString(R.string.please_enter_your_email));
            editTextEmailSignUp.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(fullNameField))
        {
            editTextFullNameSignUp.setError(getString(R.string.please_enter_your_name));
            editTextFullNameSignUp.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(passwordField))
        {
            editTextPasswordSignUp.setError(getString(R.string.please_enter_your_password));
            editTextPasswordSignUp.requestFocus();
            return;
        }

        if (!passwordField.equals(confirmPasswordField))
        {
            editTextConfirmPasswordSignUp.setError(getString(R.string.please_enter_your_confirm_passowrd));
            editTextConfirmPasswordSignUp.requestFocus();
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
                                HashMap<String, Object> addData = new HashMap<>();

                                addData.put("ID", task.getResult().getUser().getUid());
                                addData.put("name", fullNameField);
                                addData.put("email", emailField);
                                addData.put("password", passwordField);

                                 databaseReference
                                        .child("Create Account User")
                                        .child(task.getResult().getUser().getUid())
                                        .setValue(addData);
                                 navController.navigate(R.id.action_signUpFragment_to_mainFragment);
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
}
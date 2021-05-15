package com.ecommerce.shopping.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ecommerce.shopping.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment
{
    View root;
    NavController navController;
    CircleImageView circleImageView;
    EditText editTextUserName;
    Button buttonUpdate;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String downloadUrl;
    static final int GALLERY_PICK = 1;
    DatabaseReference profileDatabaseReference;
    StorageReference profileImageStorageReference;
    Uri photoProfilePathUri;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_settings, container, false);
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
        retriveDB();

    }

    private void initViews()
    {
        circleImageView = root.findViewById(R.id.circle_user_picture_home);
        editTextUserName = root.findViewById(R.id.edit_txt_user_name_home);
        buttonUpdate = root.findViewById(R.id.btn_update_home);
    }

    private void initDB()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        profileDatabaseReference = FirebaseDatabase.getInstance().getReference();
        profileImageStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void clickView()
    {
        circleImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkRequestPermission();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = editTextUserName.getText().toString();


                if (photoProfilePathUri == null)
                {
                    Toast.makeText(root.getContext(), "please enter your image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("ID", firebaseAuth.getCurrentUser().getUid());
                    updateMap.put("name", name);
                    updateMap.put("email", firebaseAuth.getCurrentUser().getEmail());
                    databaseReference
                            .child("Create Account User")
                            .child(firebaseAuth.getUid())
                            .setValue(updateMap);

                    StorageReference filePath = profileImageStorageReference.child("Images_Profile").child(photoProfilePathUri.getLastPathSegment() /* + ".jpg" */);
                    UploadTask uploadTask = filePath.putFile(photoProfilePathUri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                    {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {

                                Uri downloadUri = task.getResult();

                                downloadUrl = downloadUri.toString();

                                profileDatabaseReference
                                        .child("Create Account User")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .child("image")
                                        .setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(getContext(), "Image Save In Database, Done", Toast.LENGTH_SHORT).show();
                                                    navController.navigate(R.id.action_settingsFragment_to_mainFragment);
                                                }
                                                else
                                                {
                                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkRequestPermission()
    {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PICK);
        }
        else
        {
            openGallery();
        }
    }

    private void openGallery()
    {
        CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .start(getActivity(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            photoProfilePathUri = activityResult.getUri();
        }

        Picasso.get()
                .load(photoProfilePathUri)
                .into(circleImageView);
    }

    private void retriveDB()
    {
        databaseReference
                .child("Create Account User")
                .child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if ((snapshot.hasChild("name")) && (snapshot.hasChild("image")))
                        {
                            String nameRetrive = snapshot.child("name").getValue().toString();
                            editTextUserName.setText(nameRetrive);
                            String imageRetrive = snapshot.child("image").getValue().toString();
                            Picasso
                                    .get()
                                    .load(imageRetrive)
                                    .into(circleImageView);
                        }
                        else
                        {
                            editTextUserName.setText("What's your name ?");
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

}
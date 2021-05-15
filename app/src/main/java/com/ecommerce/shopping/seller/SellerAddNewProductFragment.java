package com.ecommerce.shopping.seller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class SellerAddNewProductFragment extends Fragment
{

    View root;
    NavController navController;
    ImageButton imageButtonGoBack;
    CircleImageView imageViewSelectProduct;
    EditText editTextProductName, editTextProductDescription, editTextProductPrice;
    Button buttonAddProduct;
    String currentUserID, downloadUrl, randomKey, productNameField, productDescriptionField, productPriceField;
    static final int GALLERY_PICK = 1;
    DatabaseReference productDatabaseReference, sellerDatabaseReference;
    StorageReference productImageReference;
    Uri photoProductPathUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_seller_add_new_product, container, false);
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
        imageButtonGoBack = root.findViewById(R.id.img_btn_go_back_admin_category);
        editTextProductName = root.findViewById(R.id.edit_txt_product_name);
        editTextProductDescription = root.findViewById(R.id.edit_txt_product_description);
        editTextProductPrice = root.findViewById(R.id.edit_txt_product_price);
        imageViewSelectProduct = root.findViewById(R.id.img_select_product);
        buttonAddProduct = root.findViewById(R.id.btn_add_product);
    }

    private void initDB()
    {
        currentUserID = FirebaseAuth.getInstance().getUid();
        productDatabaseReference = FirebaseDatabase.getInstance().getReference();
        productImageReference = FirebaseStorage.getInstance().getReference();
        sellerDatabaseReference = FirebaseDatabase.getInstance().getReference();
        randomKey = productDatabaseReference.push().getKey();
        randomKey = sellerDatabaseReference.push().getKey();
    }

    private void clickViews()
    {
        imageButtonGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_sellerAddNewProductFragment_to_sellerMainFragment);
            }
        });


        imageViewSelectProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkRequestPermission();
            }
        });

        buttonAddProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                productNameField = editTextProductName.getText().toString();
                productDescriptionField = editTextProductDescription.getText().toString();
                productPriceField = editTextProductPrice.getText().toString();

                if (TextUtils.isEmpty(productNameField))
                {
                    editTextProductName.setError(getString(R.string.please_enter_your_product_name));
                    editTextProductName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(productDescriptionField))
                {
                    editTextProductDescription.setError(getString(R.string.please_enter_your_product_description));
                    editTextProductDescription.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(productPriceField))
                {
                    editTextProductPrice.setError(getString(R.string.please_enter_your_product_price));
                    editTextProductPrice.requestFocus();
                    return;
                }

                else
                {
                    HashMap<String, Object> productHashMap = new HashMap<>();
                    productHashMap.put("ID", currentUserID);
                    productHashMap.put("Product_Name", productNameField);
                    productHashMap.put("Product_Description", productDescriptionField);
                    productHashMap.put("Product_Price", productPriceField);
                    productDatabaseReference
                            .child("Products")
                            .child(randomKey)
                            .updateChildren(productHashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(root.getContext(), "hhh", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    HashMap<String, Object> sellerHashMap = new HashMap<>();
                    sellerHashMap.put("ID", currentUserID);
                    sellerHashMap.put("Product_Seller_Name", productNameField);
                    sellerHashMap.put("Product_Seller_Description", productDescriptionField);
                    sellerHashMap.put("Product_Seller_Price", productPriceField);
                    sellerDatabaseReference
                            .child("Products Seller")
                            .child(randomKey)
                            .updateChildren(sellerHashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(root.getContext(), "hhh", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    navController.navigate(R.id.action_sellerAddNewProductFragment_to_sellerMainFragment);
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
        CropImage.activity()
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
            photoProductPathUri = activityResult.getUri();
            final StorageReference filePath = productImageReference.child("Images Products").child(photoProductPathUri.getLastPathSegment() /*+ ".jpg" */);
            final UploadTask uploadTask = filePath.putFile(photoProductPathUri);
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

                        productDatabaseReference
                                .child("Products")
                                .child(randomKey)
                                .child("Product_Image")
                                .setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(root.getContext(), "Image Save In Database, Done", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(root.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        sellerDatabaseReference
                                .child("Products Seller")
                                .child(randomKey)
                                .child("Product_Seller_Image")
                                .setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(root.getContext(), "Image Save In Database, Done", Toast.LENGTH_SHORT).show();
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


            Picasso.get()
                    .load(photoProductPathUri)
                    .into(imageViewSelectProduct);

            Picasso.get()
                    .load(photoProductPathUri)
                    .into(imageViewSelectProduct);
        }
    }
}
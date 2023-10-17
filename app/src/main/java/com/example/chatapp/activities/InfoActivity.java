package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityInfoBinding;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;

    private User receiveUser;

    private PreferenceManager preferenceManager;

    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadReceiverDetails();
        setListeners();
    }

    private void loadReceiverDetails(){
        receiveUser = (User) getIntent().getSerializableExtra("receiverProfile");
        listenEmail();
        System.out.println(receiveUser.name);
        System.out.println(receiveUser.email);
//        binding.userImage.setImageBitmap(getBitmapFromEncodedString(receiveUser.image));

        byte[] bytes = Base64.decode(receiveUser.image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
        binding.userImage.setImageBitmap(bitmap);
    }

    private void listenEmail(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(
                receiveUser.id
        ).addSnapshotListener(InfoActivity.this, (value, error)-> {
            if (error != null){
                return;
            }
            if (value != null){
                if (value.getString(Constants.KEY_EMAIL) != null){
                    String email = Objects.requireNonNull(
                            value.getString(Constants.KEY_EMAIL)
                    );
                    binding.userEmail.setText("Email: " + email);
                    binding.userName.setText("Full Name: " + receiveUser.name);
                    System.out.println(email);
                }
            }
        });
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenEmail();
    }
}
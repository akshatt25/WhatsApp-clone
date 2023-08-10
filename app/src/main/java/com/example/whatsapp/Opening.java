package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.databinding.ActivityOpeningBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Opening extends AppCompatActivity {
    ActivityOpeningBinding binding;
    FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        binding = ActivityOpeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Opening.this,PhoneNumber.class);
                startActivity(intent);

            }
        });
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userUid = preferences.getString("userUid", null);
        if (userUid != null) {
            // User profile exists, launch MainActivity
            Intent intent = new Intent(Opening.this, MainActivity.class);
            startActivity(intent);
            finish(); // This clears the OpeningActivity from back stack
        }

    }
}
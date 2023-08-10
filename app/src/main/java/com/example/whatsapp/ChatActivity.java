package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    TextView uName;
    ImageView udp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        uName= findViewById(R.id.userName);
        udp = findViewById(R.id.udp);
        String uname = getIntent().getStringExtra("uname");
        String userUid = getIntent().getStringExtra("uid");
        String uprof = getIntent().getStringExtra("profileImage");
        uName.setText(uname);
        //setting chaterdp
        Glide.with(ChatActivity.this)
                .load(uprof)
                .into(udp);





    }
}
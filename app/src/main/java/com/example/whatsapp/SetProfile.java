package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.databinding.ActivitySetProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetProfile extends AppCompatActivity {
    ActivitySetProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetProfileBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        setContentView(binding.getRoot());




        int tintColor = 0x7E979797;
        binding.selectphoto.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);

        binding.selectphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent , 45);
            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lay2.setVisibility(View.VISIBLE);
                String uName = binding.username.getText().toString();
                if(uName.isEmpty())
                {
                    binding.username.setError("Please Type a Name");
                    return;
                }
                if(selectedImage!=null)
                {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
//                                        String imageUrl = uri.toString();
                                        String uid =auth.getUid();
                                        String  username = binding.username.getText().toString();
                                        String phoneNumber = auth.getCurrentUser().getPhoneNumber();
                                        String imageUrl = uri.toString();


                                        User user = new User(uid,username,phoneNumber,imageUrl);
                                        database.getReference()
                                                .child("users")
                                                .child(uid
                                                )
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        binding.lay2.setVisibility(View.GONE);
                                                        Toast.makeText(SetProfile.this, "Welcome "+binding.username.getText().toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
                else {
                    String uid =auth.getUid();
                    String  username = binding.username.getText().toString();
                    String phoneNumber = auth.getCurrentUser().getPhoneNumber();
                    String imageUrl = "No Image";


                    User user = new User(uid,username,phoneNumber,imageUrl);
                    database.getReference()
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    binding.lay2.setVisibility(View.GONE);
                                    Toast.makeText(SetProfile.this, "Welcome "+binding.username.getText().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.apply();

                // Launch MainActivity
                Intent intent = new Intent(SetProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null)
        {
            if(data.getData()!=null)
            {
                Glide.with(this)
                        .load(data.getData())
                        .into(binding.selectphoto);
                binding.selectphoto.setColorFilter(null);
                selectedImage=data.getData();

            }
        }
    }
}
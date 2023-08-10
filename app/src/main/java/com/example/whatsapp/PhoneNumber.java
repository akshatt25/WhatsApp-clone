package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;
import com.example.whatsapp.databinding.PhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;


public class PhoneNumber extends AppCompatActivity {

    PhoneNumberBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PhoneNumberBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = binding.number.getText().toString();
                if(a.length()==10)
                {
                    binding.lay2.setVisibility(View.VISIBLE);


                    binding.number.setEnabled(false);

                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber("+91"+binding.number.getText().toString())
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(PhoneNumber.this)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(PhoneNumber.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    binding.lay2.setVisibility(View.GONE);

                                    binding.number.setEnabled(true);
                                }
                                public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(backendOtp, forceResendingToken);

                                    Intent intent = new Intent(PhoneNumber.this , OtpActivity.class);
                                    intent.putExtra("PhoneNo",a);
                                    intent.putExtra("backendOtp",backendOtp );
                                    startActivity(intent);
                                    binding.lay2.setVisibility(View.GONE);

                                    binding.number.setEnabled(true);
                                }

                            }).build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
                else
                {
                    Toast.makeText(PhoneNumber.this, "Enter a Valid Number", Toast.LENGTH_SHORT).show();
                    binding.lay2.setVisibility(View.GONE);

                    binding.number.setEnabled(true);
                }




            }
        });
    }
}
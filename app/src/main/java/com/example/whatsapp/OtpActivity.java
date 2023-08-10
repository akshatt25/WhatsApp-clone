package com.example.whatsapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;


public class OtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    String getOtp;
    FirebaseAuth auth;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        String PhNo = getIntent().getStringExtra("PhoneNo");
        getOtp = getIntent().getStringExtra("backendOtp");

        binding.otpt.setText("OTP is sent to +91"+PhNo);
        long startTimeMillis = 60 * 1000;

        // Set the countdown interval in milliseconds (1 second)
        long countDownInterval = 1000;

        // Initialize the CountDownTimer
        countDownTimer = new CountDownTimer(startTimeMillis, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the TextView with the remaining time in seconds
                int seconds = (int) (millisUntilFinished / 1000);
                binding.time.setText("Wait for " + seconds + "s");
            }

            @Override
            public void onFinish() {
                // Update the TextView when the countdown finishes
                binding.time.setText("Wait for" + 1 + "s");
                binding.time.setVisibility(View.GONE);
                PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+PhNo)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(OtpActivity.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(OtpActivity.this, "Try Again after some time", Toast.LENGTH_SHORT).show();


                            }
                            public void onCodeSent(@NonNull String newbackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(newbackendOtp, forceResendingToken);
                                getOtp=newbackendOtp;
                                Toast.makeText(OtpActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                                binding.time.setVisibility(View.GONE);



                            }

                        }).build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        };
        binding.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lay2.setVisibility(View.VISIBLE);
                String otp = binding.otp.getText().toString();
                if(otp.length()==6)
                {
                    if(getOtp!=null)
                    {
                        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                                getOtp,otp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(OtpActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                            binding.lay2.setVisibility(View.GONE);
                                            Intent intent = new Intent(OtpActivity.this,SetProfile.class);
                                            startActivity(intent);
                                            //to finish all previous acitvity
                                            finishAffinity();

                                        }
                                        else {
                                            Toast.makeText(OtpActivity.this, "Invalid , Try Again", Toast.LENGTH_SHORT).show();
                                            binding.lay2.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }
                else
                {
                    Toast.makeText(OtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    binding.lay2.setVisibility(View.GONE);

                }


            }
        });
        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.start();
                binding.time.setVisibility(View.VISIBLE);



            }

        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
            finish();
            }
        });







    }

}
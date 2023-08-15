package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.example.whatsapp.databinding.ActivityOpeningBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Opening extends AppCompatActivity {
    ActivityOpeningBinding binding;
    FirebaseAuth auth ;
    TextView TextView;

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

        TextView termsTextView = findViewById(R.id.terms);

        String fullText = "Read our Privacy Policy. Tap Agree and continue to accept the Terms of Service";

        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);

        // Define the ClickableSpan for the Privacy Policy
        ClickableSpan privacyPolicySpan = new ClickableSpan() {
            @Override
            public void onClick( View widget) {
                // Handle the privacy policy click action
                openUrl("https://www.whatsapp.com/legal/privacy-policy");
            }
        };

        // Define the ClickableSpan for the Terms of Service
        ClickableSpan termsOfServiceSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle the terms of service click action
                openUrl("https://www.whatsapp.com/legal/terms-of-service");
            }
        };

        // Apply the ClickableSpan to the respective ranges of text
        builder.setSpan(privacyPolicySpan, fullText.indexOf("Privacy Policy"), fullText.indexOf("Privacy Policy") + "Privacy Policy".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(termsOfServiceSpan, fullText.indexOf("Terms of Service"), fullText.indexOf("Terms of Service") + "Terms of Service".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsTextView.setText(builder);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


}
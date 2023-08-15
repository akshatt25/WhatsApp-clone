package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.example.whatsapp.databinding.ActivitySetProfileBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    Toolbar toolbar;
    String date;
    String time;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userUid = preferences.getString("userUid", null);
        database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = usersRef.child(userUid);
        timer = new Timer();
        timer.schedule(new UpdateFirebaseTask(), 0, 5000); // 60000 milliseconds = 1 minute


        binding.chatmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup, null);
                int location[]= new int[2];
                binding.chatmenu.getLocationOnScreen(location);
                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                int y =binding.top.getHeight()+40;
                // show the popup window
                popupWindow.setElevation(30);
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.TOP,300, y+10);


                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });


            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new ChatFragment());
        viewPagerAdapter.addFragment(new StatusFragment());
        viewPagerAdapter.addFragment(new CallsFragment());

        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.viewpager.setUserInputEnabled(true);
        binding.viewpager.setCurrentItem(0);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tab, binding.viewpager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Chats");
                            break;
                        case 1:
                            tab.setText("Status");
                            break;
                        case 2:
                            tab.setText("Calls");
                            break;
                    }
                });
        tabLayoutMediator.attach();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }
    protected void onPause() {
        super.onPause();

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }
    protected void onResume() {
        super.onResume();

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new UpdateFirebaseTask(), 0, 5000);
        }
    }

    private class UpdateFirebaseTask extends TimerTask {
        @Override
        public void run() {
            // Update the "last seen" value in Firebase
            updateLastSeen();
        }
    }
    public void updateLastSeen()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            int year = currentDateTime.getYear();
            int month = currentDateTime.getMonthValue();
            String formattedMonth = String.format("%02d", month);
            int day = currentDateTime.getDayOfMonth();
            String formattedDay = String.format("%02d", day);
            int hour = currentDateTime.getHour();
            String formattedHour = String.format("%02d", hour);
            int minute = currentDateTime.getMinute();
            String formattedMinute = String.format("%02d", minute);
            date = String.valueOf(formattedDay)+"/"+String.valueOf(formattedMonth)+"/"+String.valueOf(year);
            time = String.valueOf(formattedHour)+":"+String.valueOf(formattedMinute);

        }
        LastSeen lastSeen = new LastSeen(date,time);
//
        database.getReference()
                .child("lastseen")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(lastSeen);
    }



}


package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {
    TextView uName;
    ImageView udp;
    ImageView back;
    RecyclerView recyc;
    MessageAdapter adapter;
    ArrayList<Message> messages;
    String senderRoom;
    String receiverRoom;

    ImageView send;
    EditText textBox;
    FirebaseDatabase database;
    String receiverUid;
    TextView lastseen;
    LinearLayout linlay;
    String date;
    String time;

    Timer timer;
   TextView today;
   ImageView sendBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        uName= findViewById(R.id.userName);
        sendBg = findViewById(R.id.send);
        udp = findViewById(R.id.udp);
        back = findViewById(R.id.back);
        linlay = findViewById(R.id.linlay);
        recyc = findViewById(R.id.chatAct);
        send= findViewById(R.id.send);
        today = findViewById(R.id.today);
        lastseen = findViewById(R.id.lastseen);
        textBox = findViewById(R.id.textBox);
        SharedPreferences preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        database = FirebaseDatabase.getInstance();
        messages = new ArrayList<>();
        timer = new Timer();
        timer.schedule(new UpdateFirebaseTask(), 0, 10000); // 60000 milliseconds = 1 minute

        String uname = getIntent().getStringExtra("uname");
        receiverUid = getIntent().getStringExtra("uid");
        String currentUid = FirebaseAuth.getInstance().getUid();
        String uprof = getIntent().getStringExtra("profileImage");
        senderRoom = currentUid+receiverUid;
        receiverRoom=receiverUid+currentUid;
        uName.setText(uname);
        loadData();
        adapter = new MessageAdapter(this,messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyc.setLayoutManager(layoutManager);
        recyc.setAdapter(adapter);





        //setting chaterdp
        Glide.with(ChatActivity.this)
                .load(uprof)
                .into(udp);
        //chat
                database.getReference()
                .child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                            recyc.scrollToPosition(messages.size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                finish();
            }
        });



//            textBox.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    Rect r = new Rect();
//
//                    textBox.getWindowVisibleDisplayFrame(r);
//
//                    int heightDiff = textBox.getRootView().getHeight() - (r.bottom - r.top);
//                    if (heightDiff > 100) {
//                        recyc.smoothScrollToPosition(recyc.getAdapter().getItemCount() - 1);
//
//                    }else{
//
//                    }
//                }});
        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String newText = s.toString();

                if (newText.isEmpty()) {
                    send.setImageResource(R.drawable.send_bg);
                } else {
                    send.setImageResource(R.drawable.send_bg2);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    





        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt = textBox.getText().toString();
                Date date = new Date();
                long time = date.getTime();
                String timee = String.valueOf(time);
                Message message = new Message(messageTxt , currentUid,timee);
                textBox.setText("");
                messages.add(message);
                recyc.scrollToPosition(messages.size() - 1);
                saveData();

                adapter.notifyDataSetChanged();


                database.getReference()
                        .child("chats")
                        .child( senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                            }
                        });

            }
        });
        
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
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
            timer = null;
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
            String date1 = String.valueOf(formattedDay) + "/" + String.valueOf(formattedMonth) + "/" + String.valueOf(year);
            String time1 = String.valueOf(formattedHour) + ":" + String.valueOf(formattedMinute);
            DatabaseReference userRef = database.getReference()
                    .child("lastseen")
                    .child(receiverUid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        LastSeen lastSeen = dataSnapshot.getValue(LastSeen.class);

                        if (lastSeen != null) {
                            date = lastSeen.getDate();
                            time = lastSeen.getTime();

                            if (time.equals(time1))
                            {
                                lastseen.setVisibility(View.VISIBLE);
                                lastseen.setText("online");
                            }
                            else {
                                if(date.equals(date1))
                                {
                                    lastseen.setVisibility(View.VISIBLE);
                                    lastseen.setText("last seen today at "+time);
                                }
                                else{
                                    lastseen.setVisibility(View.VISIBLE);
                                    lastseen.setText("last seen "+date+ "at" + time);

                                }

                            }



                            // Do something with the retrieved date and time
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences(receiverUid, MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString(receiverUid+"a", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<Message>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        messages = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (messages == null) {
            // if the array list is empty
            // creating a new array list.
            messages = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences(receiverUid, MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(messages);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString(receiverUid+"a", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.

    }


}
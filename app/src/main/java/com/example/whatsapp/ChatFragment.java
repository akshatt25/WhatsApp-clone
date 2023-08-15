package com.example.whatsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.shadow.ShadowRenderer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView chatRecyc;


    FirebaseDatabase database;

    public ChatFragment() {

    }


    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        database=FirebaseDatabase.getInstance();
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userUid = preferences.getString("userUid", null);

        chatRecyc = view.findViewById(R.id.chatRecyc);
        chatRecyc.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatAdapter chatAdapter = new ChatAdapter(DatabaseHelper.getAllUsers(getActivity()),getActivity());
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    User user = snapshot1.getValue(User.class);
//                    Toast.makeText(getActivity(), user.getUid(), Toast.LENGTH_SHORT).show();
                    if(user.getUid().compareTo(userUid)!=0)
                    {
                        DatabaseHelper.addUser(getActivity(),user);
                    }

                }
                chatAdapter.notifyDataSetChanged();

            }






            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatRecyc.setAdapter(chatAdapter);


        return view;
    }
}
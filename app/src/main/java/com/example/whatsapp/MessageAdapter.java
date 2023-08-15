package com.example.whatsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.databinding.ReceivingChatBinding;
import com.example.whatsapp.databinding.SendingChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;




    public MessageAdapter(Context context, ArrayList<Message> messages)
    {
        this.context=context;
        this.messages=messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sending_chat,parent,false);
            return new SentViewHolder(view);

        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiving_chat,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId() ))
        {
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message  = messages.get(position);

        if(holder.getClass()==SentViewHolder.class)
        {
            SentViewHolder viewHolder =(SentViewHolder)holder;
            viewHolder.binding.message.setText(message.getMessage());
        }
        else
        {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {

        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder
    {
        SendingChatBinding binding;


        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SendingChatBinding.bind(itemView);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        ReceivingChatBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceivingChatBinding.bind(itemView);
        }
    }


}

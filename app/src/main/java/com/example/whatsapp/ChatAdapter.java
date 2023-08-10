package com.example.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<User> cList;
    private Context context;

    public ChatAdapter(List<User> cList, Context context) {
        this.cList = cList;
        this.context = context;
    }


    @Override
    public MessageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        User obj = cList.get(position);

        holder.cname.setText(obj.getName());
        if (obj.getProfileImage().compareTo("No Image")==0) {
           Glide.with(context)
                   .load("https://firebasestorage.googleapis.com/v0/b/waclone25.appspot.com/o/Profiles%2Fuser.png?alt=media&token=ba4fc0b1-a42b-4d85-aa18-2e590ad9925d")
                   .into(holder.cdp);

        } else{
            Glide.with(context)
                    .load(obj.getProfileImage())
                    .into(holder.cdp);

        }
        //opens chatActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , ChatActivity.class);
                intent.putExtra("uname",obj.getName());
                intent.putExtra("uid",obj.getUid());
                intent.putExtra("profileImage",obj.getProfileImage());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView cdp;
        TextView cname;
        TextView cmsg;
        TextView nofmsg;
        ImageView read;

        MessageViewHolder(View itemView) {
            super(itemView);
            cdp = itemView.findViewById(R.id.cdp);
            cname = itemView.findViewById(R.id.cname);
            cmsg = itemView.findViewById(R.id.cmsg);
            nofmsg = itemView.findViewById(R.id.noofmsg);
            read = itemView.findViewById(R.id.read);

        }
    }
}

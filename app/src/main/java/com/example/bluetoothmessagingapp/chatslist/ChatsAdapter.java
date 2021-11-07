package com.example.bluetoothmessagingapp.chatslist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothmessagingapp.R;
import com.example.bluetoothmessagingapp.chatpage.ChatActivity;

import java.util.List;

class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
    List chatsList;
    Context ctx;
    public ChatsAdapter(List chatsList, Context ctx) {
        this.chatsList = chatsList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates a recyclerView list from the chats_item template
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chats_item, parent, false);
        return new ChatsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatsViewHolder holder, int position) {
        //Sets the correct text of the current item being rendered
        int pos = position;
        String username = (String) chatsList.get(pos);
        holder.username.setText(username);
        Context context = ctx;
        holder.chatsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(context, ChatActivity.class);
                chat.putExtra("username", (String) chatsList.get(pos));
                context.startActivity(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Returns the amount of items in the recyclerView
        return chatsList.size();
    }

    class ChatsViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        View chatsItem;
        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            //A reference to the textView in each list item
            username = itemView.findViewById(R.id.username);
            //The entire chats list item
            chatsItem = itemView.findViewById(R.id.chats_item);
        }
    }
}
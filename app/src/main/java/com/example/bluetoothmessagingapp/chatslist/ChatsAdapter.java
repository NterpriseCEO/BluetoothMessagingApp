package com.example.bluetoothmessagingapp.chatslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothmessagingapp.R;

import java.util.List;

class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {
    List chatsList;

    public ChatsAdapter(List chatsList) {
        this.chatsList = chatsList;
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
        String username = (String) chatsList.get(position);
        holder.username.setText(username);
    }

    @Override
    public int getItemCount() {
        //Returns the amount of items in the recyclerView
        return chatsList.size();
    }

    class ChatsViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            //A reference to the textView in each list item
            username = itemView.findViewById(R.id.username);
        }
    }
}
package com.alain.mk.padiver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.ViewHolder.ChatViewHolder;
import com.alain.mk.padiver.models.Message;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatAdapter extends FirestoreRecyclerAdapter<Message, ChatViewHolder> {

    public interface Listener {
        void onDataChanged();
    }

    private RequestManager glide;
    private Message message;

    //FOR COMMUNICATION
    private Listener callback;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide, Listener callback) {
        super(options);
        this.glide = glide;
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i, @NonNull Message message) {

        chatViewHolder.updateWithUsers(this.getConversation(i), glide);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_item, parent, false));
    }

    public Message getConversation(int position) {
        return this.message = getItem(position);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

}

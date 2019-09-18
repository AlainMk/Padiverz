package com.alain.mk.padiver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.ViewHolder.UsersViewHolder;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UsersViewHolder> {

    public interface Listener {
        void onDataChanged();
        void onClickMessageButton(int position);
        void onClickProfileButton(int position);
    }

    private User user;
    private RequestManager glide;

    //FOR COMMUNICATION
    private Listener callback;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options, RequestManager glide, Listener callback) {
        super(options);
        this.glide = glide;
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull User user) {
        usersViewHolder.updateWithUsers(this.getUser(i), this.glide, this.callback);
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new UsersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_users_item, parent, false));
    }

    public User getUser(int position) {
        return this.user = getItem(position);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}

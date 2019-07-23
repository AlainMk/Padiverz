package com.alain.mk.padiver.fragments.user;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UsersViewHolder> {

    private User user;
    private RequestManager glide;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options, RequestManager glide) {
        super(options);
        this.glide = glide;
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull User user) {
        usersViewHolder.updateWithUsers(this.getUser(i), this.glide);
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
}

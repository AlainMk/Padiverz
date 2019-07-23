package com.alain.mk.padiver.fragments.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_users_item_name) TextView name;
    @BindView(R.id.fragment_users_item_mail) TextView mail;
    @BindView(R.id.fragment_users_item_image) ImageView imageView;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithUsers(User user, RequestManager glide) {

        this.name.setText(user.getUsername());
        this.mail.setText(user.getEmail());
        if (user.getUrlPicture() != null) {
            glide.load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
    }
}

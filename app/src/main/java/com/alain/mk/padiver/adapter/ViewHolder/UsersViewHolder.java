package com.alain.mk.padiver.adapter.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.UserAdapter;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.fragment_users_item_name) TextView name;
    @BindView(R.id.fragment_users_item_mail) TextView mail;
    @BindView(R.id.fragment_users_item_image) ImageView imageView;
    @BindView(R.id.fragment_users_item_view_message) ImageButton layoutMessage;
    @BindView(R.id.fragment_users_item_view_profile) ImageButton layoutProfile;

    private WeakReference<UserAdapter.Listener> callbackWeakRef;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithUsers(User user, RequestManager glide, UserAdapter.Listener callback) {

        this.name.setText(user.getUsername());
        this.mail.setText(user.getEmail());
        if (user.getUrlPicture() != null) {
            glide.load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }

        this.layoutMessage.setOnClickListener(v -> {

            UserAdapter.Listener callbackMessage = callbackWeakRef.get();
            if (callbackMessage != null) callbackMessage.onClickMessageButton(getAdapterPosition());
        });
        this.layoutProfile.setOnClickListener(v -> {

            UserAdapter.Listener callbackProfile = callbackWeakRef.get();
            if (callbackProfile != null) callbackProfile.onClickProfileButton(getAdapterPosition());
        });

        this.callbackWeakRef = new WeakReference<>(callback);
    }

    @Override
    public void onClick(View v) {

        UserAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickMessageButton(getAdapterPosition());
        if (callback != null) callback.onClickProfileButton(getAdapterPosition());
    }

}

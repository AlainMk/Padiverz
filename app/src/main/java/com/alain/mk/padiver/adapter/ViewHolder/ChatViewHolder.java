package com.alain.mk.padiver.adapter.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.models.Message;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_chat_item_username) TextView textViewName;
    @BindView(R.id.fragment_chat_item_last_message) TextView textViewMessage;
    @BindView(R.id.fragment_chat_item_date) TextView textViewDate;
    @BindView(R.id.fragment_chat_item_image) ImageView imageView;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithUsers(Message message, RequestManager glide) {

        this.textViewName.setText(message.getUserSender().getUsername());
        this.textViewMessage.setText(message.getMessage());
        if (message.getDateCreated() != null) this.textViewDate.setText(this.convertDateToHour(message.getDateCreated()));

        if (message.getUserSender().getUrlPicture() != null) {
            glide.load(message.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
    }

    // ---

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }
}

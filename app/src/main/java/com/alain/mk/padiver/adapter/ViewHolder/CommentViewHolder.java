package com.alain.mk.padiver.adapter.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.CommentAdapter;
import com.alain.mk.padiver.models.Comment;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.modal_fragment_comments_item_root_view) RelativeLayout rootView;
    // MESSAGE CONTAINER
    @BindView(R.id.modal_fragment_comments_item_message_container) RelativeLayout messageContainer;
    @BindView(R.id.modal_fragment_comments_item_container_image_sent_cardview) CardView cardViewImageSent;
    @BindView(R.id.modal_fragment_comments_item_user_image) ImageView imageViewUser;
    @BindView(R.id.modal_fragment_comments_item_message_container_image_sent_cardview_image) ImageView imageViewSent;
    @BindView(R.id.modal_fragment_comments_item_user_name) TextView textViewUsername;
    @BindView(R.id.modal_fragment_comments_item_comment) TextView textViewComment;
    // DATE TEXT
    @BindView(R.id.modal_fragment_comments_item_text_view_date) TextView textViewDate;

    private WeakReference<CommentAdapter.Listener> callbackWeakRef;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithComment(Comment comment, RequestManager glide, CommentAdapter.Listener callback) {

        this.textViewUsername.setText(comment.getUserSender().getUsername());
        this.textViewComment.setText(comment.getComment());
        // Update date TextView
        if (comment.getDateCreated() != null) this.textViewDate.setText(this.convertDateToHour(comment.getDateCreated()));
        // Update image sent ImageView
        if (comment.getUrlImage() != null){
            glide.load(comment.getUrlImage())
                    .into(imageViewSent);
            this.imageViewSent.setVisibility(View.VISIBLE);
        } else {
            this.imageViewSent.setVisibility(View.GONE);
        }

        if (comment.getUserSender().getUrlPicture() != null) {
            glide.load(comment.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewUser);
            this.imageViewUser.setVisibility(View.VISIBLE);
        } else {
            this.imageViewUser.setVisibility(View.GONE);
        }

        this.imageViewSent.setOnClickListener(this);
        this.callbackWeakRef = new WeakReference<>(callback);
    }

    // ---

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dfTime.format(date);
    }

    @Override
    public void onClick(View v) {

        CommentAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickCommentImage(getAdapterPosition());
    }
}

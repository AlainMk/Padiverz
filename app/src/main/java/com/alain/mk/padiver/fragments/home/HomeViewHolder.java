package com.alain.mk.padiver.fragments.home;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_home_post_item_user_name) TextView textUsername;
    @BindView(R.id.fragment_home_post_item_image_profile) ImageView imageProfile;
    @BindView(R.id.fragment_home_post_item_tags) TextView textTags;
    @BindView(R.id.fragment_home_post_item_text_title) TextView textTitle;
    @BindView(R.id.fragment_home_post_item_text_description) TextView textDescription;
    @BindView(R.id.fragment_home_post_item_image_post) ImageView imagePost;
    @BindView(R.id.fragment_home_post_item_button_like) ImageButton buttonLike;
    @BindView(R.id.fragment_home_post_item_button_comment) ImageButton buttonComment;
    @BindView(R.id.fragment_home_post_item_text_count_like) TextView textCountLikes;
    @BindView(R.id.fragment_home_post_item_text_count_comments) TextView textCountComments;
    // DATE TEXT
    @BindView(R.id.fragment_home_post_item_text_date) TextView textDate;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithPost(Post post, RequestManager glide) {

        textUsername.setText(post.getUserSender().getUsername());
        textTags.setText(post.getTags());
        textTitle.setText(post.getTitle());
        textDescription.setText(post.getDescription());
        // Update date TextView
        if (post.getDateCreated() != null) this.textDate.setText(this.convertDateToHour(post.getDateCreated()));

        if (post.getUserSender().getUrlPicture() != null) {
            glide.load(post.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageProfile);
        }
        // Update image sent ImageView
        if (post.getUrlImage() != null){
            glide.load(post.getUrlImage())
                    .into(imagePost);
            this.imagePost.setVisibility(View.VISIBLE);
        } else {
            this.imagePost.setVisibility(View.GONE);
        }
    }

    // ---

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }
}

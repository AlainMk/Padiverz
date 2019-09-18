package com.alain.mk.padiver.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.adapter.ViewHolder.CommentViewHolder;
import com.alain.mk.padiver.models.Comment;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentViewHolder> {

    public interface Listener {
        void onDataChanged();
        void onClickCommentImage(int position);
    }

    //FOR DATA
    private final RequestManager glide;
    private Comment comment;

    //FOR COMMUNICATION
    private Listener callback;

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, RequestManager glide, Listener callback) {
        super(options);
        this.glide = glide;
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i, @NonNull Comment comment) {

        commentViewHolder.updateWithComment(this.getComment(i), this.glide, this.callback);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.modal_fragment_comments_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }

    public Comment getComment(int position){

        return this.comment= getItem(position);
    }
}

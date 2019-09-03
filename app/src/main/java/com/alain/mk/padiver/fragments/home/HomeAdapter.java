package com.alain.mk.padiver.fragments.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ArticleHelper;
import com.alain.mk.padiver.models.Post;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class HomeAdapter extends FirestoreRecyclerAdapter<Post, HomeViewHolder> {

    public interface Listener {
        void onDataChanged();
        void onClickLikeButton(int position);
        void onClickCommentButton(int position);
    }

    private Post post;
    private RequestManager glide;
    private final String idCurrentUser;

    //FOR COMMUNICATION
    private Listener callback;

    public HomeAdapter(@NonNull FirestoreRecyclerOptions<Post> options, RequestManager glide, String idCurrentUser, Listener callback) {
        super(options);
        this.glide = glide;
        this.idCurrentUser = idCurrentUser;
        this.callback = callback;
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i, @NonNull Post post) {

        homeViewHolder.updateWithPost(this.getPost(i), this.glide, this.callback);

        ArticleHelper.getLikeReference(this.getPost(i).getTitle()).document(idCurrentUser).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.exists()){
                homeViewHolder.buttonLike.setImageResource(R.drawable.ic_thumb_up_green);
            }else {
                homeViewHolder.buttonLike.setImageResource(R.drawable.ic_thumb_up);
            }
        });

        ArticleHelper.getLikeReference(this.getPost(i).getTitle()).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                int count = queryDocumentSnapshots.size();
                homeViewHolder.textCountLikes.setText(count + " Likes");
            } else {
                homeViewHolder.textCountLikes.setText(0 + " Like");
            }
        });

        ArticleHelper.getCommnetsCollection(this.getPost(i).getTitle()).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                int count = queryDocumentSnapshots.size();
                homeViewHolder.textCountComments.setText(count + " Comments");
            } else {
                homeViewHolder.textCountComments.setText(0 + " Comment");
            }
        });
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_post_item, parent, false));
    }

    public Post getPost(int position) {
        return this.post = getItem(position);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }
}

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

public class HomeAdapter extends FirestoreRecyclerAdapter<Post, HomeViewHolder> {

    public interface Listener {
        void onDataChanged();
        void onClickLikeButton(int position);
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

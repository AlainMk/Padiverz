package com.alain.mk.padiver.fragments.home;


import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.PostHelper;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.post.PostActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements HomeAdapter.Listener{

    @BindView(R.id.fragment_home_recycler_view) RecyclerView recyclerView;

    private HomeAdapter homeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void updateData() {

        this.configureRecyclerView();
    }

    @OnClick(R.id.fragment_home_floating_actiion_button)
    public void onClickFloatingActionButton() {
        startActivity(new Intent(getActivity(), PostActivity.class));
    }

    // --------------------
    // REST REQUESTS
    // --------------------
    private void configureRecyclerView() {

        this.homeAdapter = new HomeAdapter(generateOptionsForAdapter(PostHelper.getPostCollection()), Glide.with(this), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.homeAdapter);
    }

    // --------------------
    // UI
    // --------------------
    // Configure RecyclerView with a Query
    private FirestoreRecyclerOptions<Post> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataChanged() {

    }
}

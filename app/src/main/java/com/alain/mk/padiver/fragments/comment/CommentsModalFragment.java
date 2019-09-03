package com.alain.mk.padiver.fragments.comment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.api.ArticleHelper;
import com.alain.mk.padiver.api.ConvHelper;
import com.alain.mk.padiver.api.MessageHelper;
import com.alain.mk.padiver.api.NotificationHelper;
import com.alain.mk.padiver.api.UserHelper;
import com.alain.mk.padiver.models.Comment;
import com.alain.mk.padiver.models.Message;
import com.alain.mk.padiver.models.Post;
import com.alain.mk.padiver.models.User;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsModalFragment extends BottomSheetDialogFragment implements CommentAdapter.Listener{

    @BindView(R.id.modal_fragment_comments_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.modal_fragment_comments_text_view_recycler_view_empty) TextView textViewRecyclerViewEmpty;
    @BindView(R.id.modal_fragment_comments_image_chosen_preview) ImageView imageViewPreview;
    @BindView(R.id.modal_fragment_comments_edit_text) TextInputEditText editTextComment;

    private BottomSheetBehavior behavior;
    private CommentAdapter commentAdapter;

    private Post post;
    @Nullable
    private User modelCurrentUser;

    public void setPost(Post post) {
        this.post = post;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.modal_fragment_comments, null);
        ButterKnife.bind(this, view);

        dialog.setContentView(view);
        behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        this.configureRecyclerView();
        this.getCurrentUserFromFirestore();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.modal_fragment_comments_send_button)
    public void onClickSendComment() {

        if (!TextUtils.isEmpty(editTextComment.getText())){
            // SEND A TEXT MESSAGE
            if (this.imageViewPreview.getDrawable() == null) {

                ArticleHelper.createComment(editTextComment.getText().toString(), post.getTitle(), modelCurrentUser).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "Une erreur inconue " + e, Toast.LENGTH_SHORT).show());
                this.editTextComment.setText("");
            }else {
                // SEND A IMAGE + TEXT IMAGE
            }
        }
    }

    @OnClick(R.id.modal_fragment_comments_add_file_button)
    public void onClickAddFile() {

    }

    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    // --------------------
    // REST REQUESTS
    // --------------------
    // Get Current User from Firestore
    private void getCurrentUserFromFirestore(){
        UserHelper.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    private void configureRecyclerView() {

        this.commentAdapter = new CommentAdapter(generateOptionsForAdapter(ArticleHelper.getCommnetsReference(post.getTitle())), Glide.with(this), this);

        commentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(commentAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(this.commentAdapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Comment> generateOptionsForAdapter(Query query){

        return new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .setLifecycleOwner(this)
                .build();
    }

    @Override
    public void onDataChanged() {

        // Show TextView in case RecyclerView is empty
        textViewRecyclerViewEmpty.setVisibility(this.commentAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClickCommentImage(int position) {

    }
}

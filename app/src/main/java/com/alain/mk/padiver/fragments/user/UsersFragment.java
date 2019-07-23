package com.alain.mk.padiver.fragments.user;

import androidx.recyclerview.widget.RecyclerView;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.base.BaseFragment;

import butterknife.BindView;

public class UsersFragment extends BaseFragment {

    @BindView(R.id.fragment_users_recycler_view) RecyclerView recyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    protected void updateData() {

    }

}

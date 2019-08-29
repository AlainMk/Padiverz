package com.alain.mk.padiver.fragments.home;


import android.content.Intent;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.base.BaseFragment;
import com.alain.mk.padiver.post.PostActivity;

import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void updateData() {

    }

    @OnClick(R.id.fragment_home_floating_actiion_button)
    public void onClickFloatingActionButton() {
        startActivity(new Intent(getActivity(), PostActivity.class));
    }
}

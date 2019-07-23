package com.alain.mk.padiver.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.alain.mk.padiver.R;
import com.alain.mk.padiver.auth.LoginActivity;
import com.alain.mk.padiver.base.BaseActivity;
import com.alain.mk.padiver.fragments.HomeFragment;
import com.alain.mk.padiver.fragments.MessageFragment;
import com.alain.mk.padiver.fragments.ProfileFragment;
import com.alain.mk.padiver.fragments.user.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.activity_home_bottom_nagivagation) BottomNavigationView bottomNavigationView;

    Fragment homeFragment;
    Fragment messageFragment;
    Fragment usersFragment;
    Fragment profileFragment;
    ActionBar actionBar;
    FirebaseAuth firebaseAuth;

    //FOR DATAS
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MESSAGE = 1;
    private static final int FRAGMENT_USERS = 2;
    public static final int FRAGMENT_PROFILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        this.initToolbar();
        this.configureBottomView();
        this.showFirstFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getFrameLayout() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                return true;
            case R.id.menu_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle("News");
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    private void configureBottomView() {

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    // -------------------
    // UI
    // -------------------

    private Boolean updateMainFragment(Integer integer){
        switch (integer) {
            case R.id.menu_navigation_recent:
                this.showFragment(FRAGMENT_HOME);
                actionBar.setTitle("News");
                break;
            case R.id.menu_navigation_message:
                this.showFragment(FRAGMENT_MESSAGE);
                actionBar.setTitle("Messages");
                break;
            case R.id.menu_navigation_group:
                this.showFragment(FRAGMENT_USERS);
                actionBar.setTitle("Users");
                break;
            case R.id.menu_navigation_account:
                this.showFragment(FRAGMENT_PROFILE);
                actionBar.setTitle("Profile");
                break;
        }
        return true;
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_home_frame_layout);
        if (visibleFragment == null){
            // Show News Fragment
            this.showFragment(FRAGMENT_HOME);
        }
    }

    // Show fragment according an Identifier
    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_HOME :
                this.showHomeFragment();
                break;
            case FRAGMENT_MESSAGE:
                this.showMessageFragment();
                break;
            case FRAGMENT_USERS:
                this.showUsersFragment();
                break;
            case FRAGMENT_PROFILE:
                this.showProfileFragment();
                break;
            default:
                break;
        }
    }

    // ---

    // Create each fragment page and show it

    private void showHomeFragment(){
        if (this.homeFragment == null) this.homeFragment = new HomeFragment();
        this.startTransactionFragment(this.homeFragment);
    }

    private void showMessageFragment(){
        if (this.messageFragment == null) this.messageFragment = new MessageFragment();
        this.startTransactionFragment(this.messageFragment);
    }

    private void showUsersFragment(){
        if (this.usersFragment == null) this.usersFragment = new UsersFragment();
        this.startTransactionFragment(this.usersFragment);
    }

    private void showProfileFragment(){
        if (this.profileFragment == null) this.profileFragment = new ProfileFragment();
        this.startTransactionFragment(this.profileFragment);
    }

    // ---

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_home_frame_layout, fragment).commit();
        }
    }

}
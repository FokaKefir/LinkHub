package com.fokakefir.linkhub.gui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.fragment.PostsFragment;
import com.fokakefir.linkhub.gui.fragment.SearchFragment;
import com.fokakefir.linkhub.gui.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // region 0. Constants

    // endregion

    // region 1. Declaration

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private BottomNavigationView bottomNav;

    private List<Fragment> homeFragments;
    private List<Fragment> searchFragments;
    private List<Fragment> userFragments;

    private List<Fragment> activeFragments;

    // endregion

    // region  2. Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bottomNav = findViewById(R.id.bottom_navigation);
        this.bottomNav.setOnNavigationItemSelectedListener(this);

        this.homeFragments = new ArrayList<>();
        this.searchFragments = new ArrayList<>();
        this.userFragments = new ArrayList<>();

        this.homeFragments.add(new PostsFragment(this));
        this.searchFragments.add(new SearchFragment(this));
        this.userFragments.add(new UserFragment(this));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, this.homeFragments.get(0)).show(this.homeFragments.get(0)).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, this.searchFragments.get(0)).hide(this.searchFragments.get(0)).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, this.userFragments.get(0)).hide(this.userFragments.get(0)).commit();

        this.activeFragments = this.homeFragments;

    }

    @Override
    public void onBackPressed() {
        if (this.activeFragments.size() > 1) {
            removeFromFragments();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    // endregion

    // region 3. Bottom navigation and Fragments

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        List<Fragment> selectedFragments = null;
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                selectedFragments = this.homeFragments;
                selectedFragment = new PostsFragment(this);
                break;
            case R.id.nav_search:
                selectedFragments = this.searchFragments;
                selectedFragment = new SearchFragment(this);
                break;
            case R.id.nav_user:
                selectedFragments = this.userFragments;
                selectedFragment = new UserFragment(this);
                break;
        }

        if (selectedFragments != null) {
            if (selectedFragments == this.activeFragments) {
                for (Fragment fragment : this.activeFragments)
                    this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                this.getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, selectedFragment).commit();
                this.activeFragments.clear();
                this.activeFragments.add(selectedFragment);
            } else {
                for (Fragment fragment : this.activeFragments)
                    this.getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                for (Fragment fragment : selectedFragments)
                    this.getSupportFragmentManager().beginTransaction().show(fragment).commit();
                this.activeFragments = selectedFragments;
            }

            return true;
        }


        return false;
    }

    public void addToFragments(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .add(R.id.fragment_container, fragment).addToBackStack(null).commit();
        this.activeFragments.add(fragment);
    }

    public void removeFromFragments() {
        Fragment fragment = this.activeFragments.get(this.activeFragments.size() - 1);
        this.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.slide_out, R.anim.slide_in, R.anim.fade_out)
                .remove(fragment).commit();
        this.activeFragments.remove(fragment);
    }

    // endregion

    // region 4. Toolbox

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_logout) {
            this.auth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }


    // endregion
}
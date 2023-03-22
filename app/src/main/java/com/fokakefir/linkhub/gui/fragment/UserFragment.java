package com.fokakefir.linkhub.gui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.recyclerview.PlaceAdapter;
import com.fokakefir.linkhub.logic.database.UserDatabaseManager;
import com.fokakefir.linkhub.model.Place;
import com.fokakefir.linkhub.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment implements UserDatabaseManager.OnResponseListener, View.OnClickListener, PlaceAdapter.OnHolderListener {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private MainActivity activity;

    private String userId;
    private List<Place> places;

    private CircleImageView userImage;
    private TextView txtUsername;
    private TextView txtPosts;
    private TextView txtFollowers;
    private TextView txtFollowing;

    private ImageView userSearch;

    private FloatingActionButton fabAddUser;
    private FloatingActionButton fabRemoveUser;

    private RecyclerView recyclerView;
    private PlaceAdapter adapter;

    private UserDatabaseManager databaseManager;
    private View view;


    public UserFragment(MainActivity activity) {
        this.activity = activity;
    }

    public UserFragment(MainActivity activity, String userId) {
        this.activity = activity;
        if (!userId.equals(auth.getUid()))
            this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user, container, false);

        this.places = new ArrayList<>();

        this.userImage = this.view.findViewById(R.id.img_user);
        this.txtUsername = this.view.findViewById(R.id.txt_username);
        this.txtPosts = this.view.findViewById(R.id.txt_user_posts);
        this.txtFollowers = this.view.findViewById(R.id.txt_user_followers);
        this.txtFollowing = this.view.findViewById(R.id.txt_user_following);
        this.userSearch = this.view.findViewById(R.id.user_search);

        this.fabAddUser = this.view.findViewById(R.id.fab_add_user);
        this.fabRemoveUser = this.view.findViewById(R.id.fab_remove_user);
        this.fabAddUser.setOnClickListener(this);
        this.fabRemoveUser.setOnClickListener(this);

        this.recyclerView = this.view.findViewById(R.id.recycler_view_user_places);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.adapter = new PlaceAdapter(this.activity, this.places, this);
        this.recyclerView.setAdapter(this.adapter);

        this.userSearch.setOnClickListener(this);

        if (this.userId == null) {
            this.databaseManager = new UserDatabaseManager(this);
        } else {
            this.databaseManager = new UserDatabaseManager(this, userId);
        }

        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.databaseManager.setSnapshotListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.databaseManager.removeSnapshotListener();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserDataChanged(User user) {
        this.txtUsername.setText(user.getName());
        this.txtPosts.setText(user.getPlaceIdsSize() + "\nreviews");
        this.txtFollowers.setText(user.getFollowerIdsSize() + "\nfollowers");
        this.txtFollowing.setText(user.getFollowingIdsSize() + "\nfollowing");
        if(user.getImageUrl() == null){
            Glide.with(getContext()).load(R.drawable.ic_baseline_user_24).into(this.userImage);
        } else{
            Glide.with(getContext()).load(user.getImageUrl()).into(this.userImage);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.user_search) {
            this.activity.addToFragments(new SearchUserFragment(this.activity));
        } else if (v.getId() == R.id.fab_add_user) {
            this.databaseManager.followUser(this.userId);
        } else if (v.getId() == R.id.fab_remove_user) {
            this.databaseManager.unfollowUser(this.userId);
        }
    }

    @Override
    public void onPlaceClick(int pos) {
        Place place = this.places.get(pos);
        this.activity.addToFragments(new ReviewFragment(this.activity, place));
    }

    @Override
    public void onPlaceAdded(Place place) {
        int pos = this.places.size();
        this.places.add(place);
        this.adapter.notifyItemInserted(pos);
    }

    @Override
    public void onFollowing(boolean following) {
        if (following) {
            this.fabAddUser.setVisibility(View.GONE);
            this.fabRemoveUser.setVisibility(View.VISIBLE);
        } else {
            this.fabAddUser.setVisibility(View.VISIBLE);
            this.fabRemoveUser.setVisibility(View.GONE);
        }

        this.userSearch.setVisibility(View.GONE);
    }

}
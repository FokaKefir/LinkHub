package com.fokakefir.linkhub.gui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.logic.database.UserDatabaseManager;
import com.fokakefir.linkhub.model.User;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment implements UserDatabaseManager.OnResponseListener {

    private MainActivity activity;

    private TextView txtName;
    private TextView txtPosts;
    private TextView txtFollowers;
    private TextView txtFollowing;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UserDatabaseManager databaseManager;

    private View view;
    private CircleImageView userImage;
    private TextView txtUsername;
    public UserFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_user, container, false);

        this.userImage = this.view.findViewById(R.id.img_user);
        this.txtUsername = this.view.findViewById(R.id.txt_username);
        this.txtPosts = this.view.findViewById(R.id.txt_user_posts);
        this.txtFollowers = this.view.findViewById(R.id.txt_user_followers);
        this.txtFollowing = this.view.findViewById(R.id.txt_user_following);
        this.databaseManager = new UserDatabaseManager(this);

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
}
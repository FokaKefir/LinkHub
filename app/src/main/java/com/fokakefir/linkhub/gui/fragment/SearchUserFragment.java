package com.fokakefir.linkhub.gui.fragment;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.recyclerview.UserSearchAdapter;
import com.fokakefir.linkhub.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.core.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class SearchUserFragment extends Fragment {
    private MainActivity activity;
    private RecyclerView searchUserRecyclerView;
    private UserSearchAdapter userSearchAdapter;
    private List<User> users;
    EditText searchUser;
    private View view;

    public SearchUserFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_search_user, container, false);

        this.searchUserRecyclerView = view.findViewById(R.id.recycler_user_search);
        this.searchUserRecyclerView.setHasFixedSize(true);
        this.searchUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.searchUser.findViewById(R.id.txt_user_search);

        this.users = new ArrayList<>();

        return this.view;
    }

}
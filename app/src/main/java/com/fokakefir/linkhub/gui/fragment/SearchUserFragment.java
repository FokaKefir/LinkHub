package com.fokakefir.linkhub.gui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.gui.activity.MainActivity;
import com.fokakefir.linkhub.gui.recyclerview.UserSearchAdapter;
import com.fokakefir.linkhub.logic.database.SearchUserDatabaseManager;
import com.fokakefir.linkhub.model.User;

import java.util.ArrayList;
import java.util.List;


public class SearchUserFragment extends Fragment implements UserSearchAdapter.OnUserListener, View.OnClickListener, SearchUserDatabaseManager.OnSearchUserListener {

    private MainActivity activity;
    private RecyclerView recyclerView;
    private UserSearchAdapter adapter;
    private List<User> users;
    private EditText txtSearchUser;
    private Button btnSearch;
    private View view;

    private SearchUserDatabaseManager databaseManager;

    public SearchUserFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_search_user, container, false);

        this.users = new ArrayList<>();

        this.txtSearchUser = view.findViewById(R.id.txt_user_search);
        this.btnSearch = view.findViewById(R.id.btn_user_search);

        this.btnSearch.setOnClickListener(this);

        this.recyclerView = view.findViewById(R.id.recycler_user_search);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new UserSearchAdapter(this.users, this, this.activity);
        this.recyclerView.setAdapter(this.adapter);

        this.databaseManager = new SearchUserDatabaseManager(this);

        return this.view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_user_search) {
            String inputName = this.txtSearchUser.getText().toString().trim();
            if (inputName.isEmpty()) {
                Toast.makeText(activity, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                this.users.clear();
                this.adapter.notifyDataSetChanged();
                this.databaseManager.searchForUsers(inputName);
            }
        }
    }

    @Override
    public void onUserClick(int pos) {
        User user = this.users.get(pos);
        this.activity.addToFragments(new UserFragment(this.activity, user.getDocId()));
    }

    @Override
    public void onAddUser(User user) {
        int pos = this.users.size();
        this.users.add(user);
        this.adapter.notifyItemInserted(pos);
    }

    @Override
    public void onFailed(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
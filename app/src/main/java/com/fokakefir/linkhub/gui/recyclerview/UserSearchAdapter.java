package com.fokakefir.linkhub.gui.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>  {
    private List<User> users;
    private OnUserListener listener;
    private Context context;

    // endregion

    // region 2. Constructor

    public UserSearchAdapter(List<User> users, OnUserListener onUserListener, Context context) {
        this.users = users;
        this.listener = onUserListener;
        this.context = context;
    }

    // endregion

    // region 3. Adapter


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_place, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchAdapter.UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtName;

        public  TextView txtDescription;
        public CircleImageView imageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.img_user_search);
            this.txtName = itemView.findViewById(R.id.txt_user_name_search);

            Glide.with(itemView).load(R.mipmap.ic_launcher_login).into(this.imageView);

            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == this.itemView) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onUserClick(pos);
                }
            }
        }
    }

    public interface OnUserListener {
        void onUserClick(int pos);
    }

    // endregion

}

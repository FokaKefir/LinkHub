package com.fokakefir.linkhub.gui.recyclerview;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.model.Review;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Context context;
    private List<Review> reviews;
    private OnReviewListener listener;

    public ReviewAdapter(Context context, OnReviewListener listener, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_review, parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = this.reviews.get(position);


        holder.txtAuthor.setText(review.getAuthor());
        holder.txtRate.setText(String.valueOf(review.getRate()));
        holder.txtComment.setText(review.getComment());

        Glide.with(this.context)
                .load(review.getImageUrl())
                .into(holder.imageView);

        if (review.getAuthorId().equals(auth.getUid())) {
            holder.registerForContextMenu();
        } else {
            holder.unregisterForContextMenu();
        }

    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener, View.OnClickListener {
        private View itemView;

        public TextView txtAuthor;
        public TextView txtRate;
        public TextView txtComment;
        public ImageView imageView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.txtAuthor = this.itemView.findViewById(R.id.txt_review_author);
            this.txtComment = this.itemView.findViewById(R.id.txt_review_comment);
            this.txtRate = this.itemView.findViewById(R.id.txt_review_rate);

            this.txtAuthor.setOnClickListener(this);

            this.imageView = this.itemView.findViewById(R.id.img_review);
        }

        public void unregisterForContextMenu() {
            this.itemView.setOnCreateContextMenuListener(null);
        }

        public void registerForContextMenu() {
            this.itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Options");
            MenuItem itemDelete = menu.add(Menu.NONE, 1, 1, "Delete review");

            itemDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                switch (item.getItemId()) {
                    case 1:
                        listener.onDeleteReview(pos);
                        return true;
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.txt_review_author) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onAuthorClick(pos);
                }
            }
        }
    }

    public interface OnReviewListener {
        void onDeleteReview(int pos);
        void onAuthorClick(int pos);
    }
}

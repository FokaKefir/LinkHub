package com.fokakefir.linkhub.gui.recyclerview;

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
import com.fokakefir.linkhub.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
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

    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
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

            this.imageView = this.itemView.findViewById(R.id.img_review);

            this.itemView = itemView;
        }
    }
}

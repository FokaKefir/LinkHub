package com.fokakefir.linkhub.gui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fokakefir.linkhub.R;
import com.fokakefir.linkhub.model.Place;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {


    private List<Place> places;
    private Context context;


    public PlaceAdapter(Context context, List<Place> places){
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_place, parent, false);
        return new PlaceViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);

        holder.txtName.setText(place.getName());
        holder.txtDescription.setText(place.getDescription());

        Picasso.with(this.context).load(place.getImageUrl())
                .placeholder(R.mipmap.ic_launcher).
                fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.places.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;

        public  TextView txtDescription;

        public ImageView imageView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_place);
            this.txtName = itemView.findViewById(R.id.txt_Place_name);
            this.txtDescription = itemView.findViewById(R.id.txt_Place_description);
        }
    }
}

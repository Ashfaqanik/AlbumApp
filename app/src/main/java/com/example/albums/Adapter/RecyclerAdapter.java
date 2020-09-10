package com.example.albums.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.albums.Model.Albums;
import com.example.albums.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Albums> albumList;

    public RecyclerAdapter(Context context, List<Albums> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_data, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final Albums albums = albumList.get(position);
        final String imageUrl,imageUrl1;
        imageUrl = albums.getUrl();
        imageUrl1 = albums.getThumbnailUrl();
        holder.text.setText(albums.getTitle());
        Picasso.get()
                .load(imageUrl)
                .fit()
                .into(holder.image);
        Picasso.get()
                .load(imageUrl1)
                .fit()
                .into(holder.image1);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView image1;
        public TextView text;
        public ViewHolder(@NonNull View itemView,final Context ctx) {
            super(itemView);
            context = ctx;
            image = (ImageView) itemView.findViewById(R.id.imageView);
            image1 = (ImageView) itemView.findViewById(R.id.imageView1);
            text = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}

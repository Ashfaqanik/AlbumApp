package com.example.albums.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ImageView;

import com.example.albums.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image");
        ImageView imageView = findViewById(R.id.imageView2);
        Picasso.get()
                .load(imageUrl)
                .fit().centerInside()
                .into(imageView);
    }
}
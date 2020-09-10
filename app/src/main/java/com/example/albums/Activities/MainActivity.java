package com.example.albums.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.albums.Adapter.RecyclerAdapter;
import com.example.albums.Api.ApiClient;
import com.example.albums.Model.Albums;
import com.example.albums.R;
import com.example.albums.Utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Albums> albumList;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBar;
    private FirebaseUser user;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager LayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mProgressBar = findViewById(R.id.main_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        albumList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(LayoutManager);

        Call<List<Albums>> call = ApiClient.apiInterface().getAlbums();
        call.enqueue(new Callback<List<Albums>>() {
            @Override
            public void onResponse(Call<List<Albums>> call, Response<List<Albums>> response) {
                if(response.isSuccessful())
                {
                    recyclerAdapter = new RecyclerAdapter(MainActivity.this, response.body());
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Albums>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error"+ t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.logout)
        {
            Toast.makeText(getApplicationContext(),"log out",Toast.LENGTH_SHORT).show();
            PreferenceUtils.savePassword(null, this);
            PreferenceUtils.saveEmail(null, this);
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));

        }
        return true;
    }
}
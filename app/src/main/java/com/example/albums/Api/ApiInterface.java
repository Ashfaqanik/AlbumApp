package com.example.albums.Api;

import com.example.albums.Model.Albums;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("photos")
    Call<List<Albums>>getAlbums();
}

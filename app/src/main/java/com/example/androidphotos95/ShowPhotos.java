package com.example.androidphotos95;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.androidphotos95.ShowAlbum.photos;
import static com.example.androidphotos95.ShowAlbum.selectedPhoto;

public class ShowPhotos extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Tag> adapter;
    public static ArrayList<Photo> photos = Photos.selectedAlbum.getListOfPhotos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView v = findViewById(R.id.imageView2);
        v.setImageURI(selectedPhoto.getUri());
        adapter = new ArrayAdapter<Tag>(this, R.layout.list_item, selectedPhoto.getTags());
        listView = (ListView) findViewById(R.id.tagList);
        listView.setAdapter(adapter);


    }

}

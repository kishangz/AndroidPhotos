package com.example.androidphotos95;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ShowAlbum extends AppCompatActivity {

    //public static ArrayList<Photo> photos = Photos.album.getListOfPhotos();
    public static ArrayList<Photo> photos = Photos.selectedAlbum.getListOfPhotos();
    private Photo selectedPhoto;
    private Album thisAlbum;

    private static final int IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridView grid = findViewById(R.id.grid);
        GridAdapter image = new GridAdapter(ShowAlbum.this);
        grid.setAdapter(image);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.deletePhoto:

                //deletePhotoDialog();

                break;
            case R.id.displayItem:
                openAlbum();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void openAlbum() {
        if(selectedPhoto!=null) {
            //Intent openIntent = new Intent(getApplicationContext(), DisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_photo",selectedPhoto);
            bundle.putSerializable("album", thisAlbum);
            bundle.putSerializable("album_list", photos);
            //openIntent.putExtras(bundle);
            //startActivity(openIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            photos.add(new Photo("", data.getData()));

            GridView grid = findViewById(R.id.grid);
            GridAdapter image = new GridAdapter(ShowAlbum.this);
            grid.setAdapter(image);
        }
    }

}

package com.example.androidphotos95;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.androidphotos95.Photos.album;
import static com.example.androidphotos95.Photos.albums;
import static com.example.androidphotos95.Photos.clipboard;

public class ShowAlbum extends AppCompatActivity {

    public static ArrayList<Photo> photos;
    private GridView grid;

    public static Photo selectedPhoto = null;

    private static final int IMAGE_REQUEST_CODE = 1;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photos = album.getListOfPhotos();

        grid = findViewById(R.id.grid);
        GridAdapter image = new GridAdapter(ShowAlbum.this);
        grid.setAdapter(image);
        grid.setOnItemClickListener(
                (parent,view,pos,id) -> {
                    selectedPhoto = photos.get(pos);
                    Intent intent = new Intent(this, ShowPhotos.class);
                    startActivity(intent);
                });

        registerForContextMenu(grid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            photos.add(new Photo("", data.getData()));


            GridAdapter image = new GridAdapter(ShowAlbum.this);
            grid.setAdapter(image);

            try {
                File outputFile = new File(getFilesDir(),"save_object.bin");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
                oos.writeObject(new ArrayList<Album>(albums));
                oos.flush();
                oos.close();
            }catch (java.io.IOException e){


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addPhoto: // User pressed add
                addPhoto();

                break;
            case R.id.paste:
                if (clipboard != null) {
                    photos.add(clipboard);
                    GridAdapter image = new GridAdapter(ShowAlbum.this);
                    grid.setAdapter(image);

                    try {
                        File outputFile = new File(getFilesDir(),"save_object.bin");
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
                        oos.writeObject(new ArrayList<Album>(albums));
                        oos.flush();
                        oos.close();
                    }catch (java.io.IOException e){


                    }
                }

                break;
            case R.id.app_bar_search:
                LayoutInflater la = ShowAlbum.this.getLayoutInflater();
                View vi = la.inflate(R.layout.search_field, null);
                final EditText search_field = vi.findViewById(R.id.edit_text);
                search_field.setHint("type=value [AND/OR type=value]");

                android.app.AlertDialog.Builder build =
                        new android.app.AlertDialog.Builder(ShowAlbum.this);
                build
                        .setTitle("Search")
                        .setView(vi)
                        .setPositiveButton("Search", (d, id) -> {
                            Photos.search = search_field.getText().toString();
                            Intent intent = new Intent(this, Search.class);
                            startActivity(intent);

                        })
                        .setNegativeButton("Cancel", (d, id) -> {});

                AlertDialog di = build.create();
                di.show();



                break;

        }


        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }



    private void addPhoto(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Cut");
        menu.add("Delete");

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        i = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
    }

    public boolean onContextItemSelected (MenuItem item) {

        if (item.getTitle().toString().equals("Cut")){
            clipboard = photos.get(i);


        }

        photos.remove(i);
        GridAdapter image = new GridAdapter(ShowAlbum.this);
        grid.setAdapter(image);

        try {
            File outputFile = new File(getFilesDir(),"save_object.bin");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            oos.writeObject(new ArrayList<Album>(albums));
            oos.flush();
            oos.close();
        }catch (java.io.IOException e){


        }

        return true;
    }


}

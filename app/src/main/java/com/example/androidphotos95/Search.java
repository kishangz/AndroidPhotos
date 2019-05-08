package com.example.androidphotos95;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.androidphotos95.Photos.album;

public class Search extends AppCompatActivity {

    private ArrayList<Photo> results;
    private GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        results = new ArrayList<Photo>();

        String input = Photos.search;

        if (input.equals("")) {
            return;
        }

        String[] s = input.split("AND | and | OR | or");

        String[] s2;
        s2 = s[0].split("=");
        if (s2.length != 2) {
            return;
        }
        String type1 = s2[0].trim();
        String value1 = s2[1].trim();

        if (s.length == 1) {
            Iterator<Album> aIterator = Photos.albums.iterator();

            while (aIterator.hasNext()) {

                Album album = aIterator.next();

                Iterator<Photo> pIterator = album.getListOfPhotos().iterator();

                while (pIterator.hasNext()) {

                    Photo photo = pIterator.next();
                    Iterator<Tag> tIterator = photo.getTags().iterator();

                    while (tIterator.hasNext()) {
                        Tag tag = tIterator.next();

                        if(type1.equalsIgnoreCase(tag.getType()) && tag.getValue().contains(value1)) {
                            results.add(photo);
                            break;
                        }
                    }
                }
            }
        }

        String type2;
        String value2;

        if (s.length == 2) {
            s[1] = s[1].trim();
            s2 = s[1].split("=");
            if (s2.length != 2) {
                return;
            }
            type2 = s2[0];
            value2 = s2[1];


            if (input.contains(" and ") || input.contains(" AND ")) {
                Iterator<Album> aIterator = Photos.albums.iterator();

                while (aIterator.hasNext()) {

                    Album album = aIterator.next();

                    Iterator<Photo> pIterator = album.getListOfPhotos().iterator();

                    while (pIterator.hasNext()) {

                        Photo photo = pIterator.next();
                        Iterator<Tag> tIterator = photo.getTags().iterator();

                        int i = 0;
                        while (tIterator.hasNext()) {
                            Tag tag = tIterator.next();

                            if((type1.equalsIgnoreCase(tag.getType()) && tag.getValue().contains(value1))
                                    || (type2.equalsIgnoreCase(tag.getType()) && tag.getValue().contains(value2))) {

                                if (i == 1) {
                                    results.add(photo);
                                    break;
                                }
                                i++;

                            }
                        }
                    }
                }

            } else if (input.contains(" or ") || input.contains(" OR ")) {
                Iterator<Album> aIterator = Photos.albums.iterator();

                while (aIterator.hasNext()) {

                    Album album = aIterator.next();

                    Iterator<Photo> pIterator = album.getListOfPhotos().iterator();

                    while (pIterator.hasNext()) {

                        Photo photo = pIterator.next();
                        Iterator<Tag> tIterator = photo.getTags().iterator();

                        while (tIterator.hasNext()) {
                            Tag tag = tIterator.next();

                            if((type1.equalsIgnoreCase(tag.getType()) && tag.getValue().contains(value1))
                                    || (type2.equalsIgnoreCase(tag.getType()) && tag.getValue().contains(value2))) {
                                results.add(photo);
                                break;
                            }

                        }
                    }
                }
            }
        }

        ShowAlbum.photos = results;

        grid = findViewById(R.id.grid);
        GridAdapter image = new GridAdapter(Search.this);
        grid.setAdapter(image);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);




        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.app_bar_search:
                LayoutInflater la = Search.this.getLayoutInflater();
                View vi = la.inflate(R.layout.search_field, null);
                final EditText search_field = vi.findViewById(R.id.edit_text);
                search_field.setHint("type=value [AND/OR type=value]");

                AlertDialog.Builder build =
                        new AlertDialog.Builder(Search.this);
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




}

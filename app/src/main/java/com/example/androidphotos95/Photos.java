package com.example.androidphotos95;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class Photos extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);

        listView = findViewById(R.id.album_list);
        albums = new ArrayList<>();
        ArrayAdapter<Album> adapter =
                new ArrayAdapter<>(this, R.layout.list_item, albums);
        listView.setAdapter(adapter);

        FloatingActionButton add = findViewById(R.id.add_album_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater l = Photos.this.getLayoutInflater();
                View v = l.inflate(R.layout.add_album_field, null);
                final EditText album_field = v.findViewById(R.id.edit_text);

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Photos.this);
                builder.setMessage("Enter album name:")
                        .setTitle("Add Album")
                        .setView(v)
                        .setPositiveButton("ADD", (dialog, id) -> {
                            albums.add(new Album(album_field.getText().toString()));
                            listView.setAdapter(new ArrayAdapter<Album>(Photos.this, R.layout.list_item, albums));})
                        .setNegativeButton("Cancel", (dialog, id) -> {});

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}

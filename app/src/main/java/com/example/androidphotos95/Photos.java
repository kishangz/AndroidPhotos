package com.example.androidphotos95;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;

public class Photos extends AppCompatActivity {

    private ListView listView;
    public static ArrayList<Album> albums = new ArrayList<>();
    public static Album album = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);

        listView = findViewById(R.id.album_list);

        ArrayAdapter<Album> adapter =
                new ArrayAdapter<>(this, R.layout.list_item, albums);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                (parent,view,pos,id) -> {
                    album = albums.get(pos);
                    Intent intent = new Intent(this, ShowAlbum.class);
                    startActivity(intent);
                });

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


                            Iterator<Album> it = albums.iterator();
                            while (it.hasNext()) {
                                if(it.next().getName().equals(album_field.getText().toString())){
                                    AlertDialog.Builder alert =
                                            new AlertDialog.Builder(Photos.this);
                                    alert.setMessage("An album with that name already exists.");
                                    AlertDialog a = alert.create();
                                    a.show();
                                    return;
                                }
                            }

                            albums.add(new Album(album_field.getText().toString()));
                            listView.setAdapter(new ArrayAdapter<Album>(Photos.this, R.layout.list_item, albums));
                        })
                        .setNegativeButton("Cancel", (dialog, id) -> {});

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}

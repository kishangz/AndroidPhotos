package com.example.androidphotos95;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.androidphotos95.Photos.albums;
import static com.example.androidphotos95.ShowAlbum.photos;
import static com.example.androidphotos95.ShowAlbum.selectedPhoto;

public class ShowPhotos extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Tag> adapter;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView v = findViewById(R.id.imageView2);
        v.setImageURI(selectedPhoto.getUri());
        adapter = new ArrayAdapter<Tag>(this, R.layout.list_item, selectedPhoto.getTags());
        listView = (ListView) findViewById(R.id.tagList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener( (parent,view,pos,id) -> {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ShowPhotos.this);
                    builder
                            .setMessage("Delete this tag?")


                            .setPositiveButton("Delete", (dialog, id1) -> {

                                selectedPhoto.getTags().remove(pos);
                                listView.setAdapter(new ArrayAdapter<Tag>(ShowPhotos.this, R.layout.list_item, selectedPhoto.getTags()));
                                try {
                                    File outputFile = new File(getFilesDir(),"save_object.bin");
                                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
                                    oos.writeObject(new ArrayList<Album>(albums));
                                    oos.flush();
                                    oos.close();
                                }catch (java.io.IOException e){


                                }


                            })
                            .setNegativeButton("Cancel", (dialog, id1) -> {});

                    AlertDialog dialog = builder.create();
                    dialog.show();

            return true;

        }


        );

        i = photos.indexOf(selectedPhoto);

        ImageButton left = findViewById(R.id.leftButton);
        left.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i == 0) {

                    i = photos.size() - 1;
                }
                else
                {
                    i = i - 1;

                }

                ImageView v = findViewById(R.id.imageView2);
                selectedPhoto = photos.get(i);
                v.setImageURI(selectedPhoto.getUri());

                listView.setAdapter(new ArrayAdapter<Tag>(ShowPhotos.this, R.layout.list_item, selectedPhoto.getTags()));

            }
        });

        ImageButton right = findViewById(R.id.rightButton);
        right.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i == photos.size() - 1) {

                    i = 0;
                }
                else
                {
                    i = i + 1;

                }

                ImageView v = findViewById(R.id.imageView2);
                selectedPhoto = photos.get(i);
                v.setImageURI(selectedPhoto.getUri());

                listView.setAdapter(new ArrayAdapter<Tag>(ShowPhotos.this, R.layout.list_item, selectedPhoto.getTags()));

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addPhoto: // User pressed add
                addTag();

                break;

        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    private void addTag(){
        LayoutInflater l = ShowPhotos.this.getLayoutInflater();
        View v = l.inflate(R.layout.add_tag_dialog, null);
        final EditText value_field = v.findViewById(R.id.edit_text1);
        final RadioGroup radio = v.findViewById(R.id.types);



        AlertDialog.Builder builder =
                new AlertDialog.Builder(ShowPhotos.this);
        builder
                .setTitle("Add Tag")

                .setView(v)
                .setPositiveButton("ADD", (dialog, id) -> {

                    RadioButton button = radio.findViewById(radio.getCheckedRadioButtonId());
                    String type = button.getText().toString();

                    Iterator<Tag> it = selectedPhoto.getTags().iterator();
                    while (it.hasNext()) {
                        Tag t = it.next();
                        if(t.getValue().equalsIgnoreCase(value_field.getText().toString().toLowerCase()) && t.getType().equalsIgnoreCase(type.toLowerCase())){
                            android.app.AlertDialog.Builder alert =
                                    new android.app.AlertDialog.Builder(ShowPhotos.this);
                            alert.setMessage("This tag already exists.");
                            android.app.AlertDialog a = alert.create();
                            a.show();
                            return;
                        }
                    }

                    selectedPhoto.getTags().add(new Tag(type, value_field.getText().toString()));
                    listView.setAdapter(new ArrayAdapter<Tag>(ShowPhotos.this, R.layout.list_item, selectedPhoto.getTags()));

                    try {
                        File outputFile = new File(getFilesDir(),"save_object.bin");
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
                        oos.writeObject(new ArrayList<Album>(albums));
                        oos.flush();
                        oos.close();
                    }catch (java.io.IOException e){


                    }


                })
                .setNegativeButton("Cancel", (dialog, id) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

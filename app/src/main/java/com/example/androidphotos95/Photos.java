package com.example.androidphotos95;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.androidphotos95.ShowAlbum.selectedPhoto;

public class Photos extends AppCompatActivity {

    private ListView listView;
    public static ArrayList<Album> albums = new ArrayList<>();
    public static Album album;
    public static Photo clipboard;
    public static String search;
    private int i;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);

       File f = new File(getFilesDir(), "save_object.bin");
       if (f.exists()) {

           loadSerializedObject();
       }

        listView = findViewById(R.id.album_list);

        ArrayAdapter<Album> adapter =
                new ArrayAdapter<>(this, R.layout.list_item1, albums);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                (parent,view,pos,id) -> {
                    album = albums.get(pos);
                    Intent intent = new Intent(this, ShowAlbum.class);
                    startActivity(intent);
                });


       registerForContextMenu(listView);


    }

    @Override
    protected void onRestart(){
        super.onRestart();

        loadSerializedObject();

        listView = findViewById(R.id.album_list);

        ArrayAdapter<Album> adapter =
                new ArrayAdapter<>(this, R.layout.list_item1, albums);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                (parent,view,pos,id) -> {
                    album = albums.get(pos);
                    Intent intent = new Intent(this, ShowAlbum.class);
                    startActivity(intent);
                });


        registerForContextMenu(listView);
    }

    public Object loadSerializedObject(){
        try{
            File inputFile = new File(getFilesDir(),"save_object.bin");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));
            albums = (ArrayList)ois.readObject();
            ois.close();

        }catch(java.io.IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Rename");
        menu.add("Delete");

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        i = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
    }

    public boolean onContextItemSelected (MenuItem item) {

        if (item.getTitle().toString().equals("Rename")){
            LayoutInflater l = Photos.this.getLayoutInflater();
            View v = l.inflate(R.layout.add_album_field, null);
            final EditText album_field = v.findViewById(R.id.edit_text);

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(Photos.this);
            builder.setMessage("Enter album name:")
                    .setTitle("Rename Album")
                    .setView(v)
                    .setPositiveButton("Rename", (dialog, id) -> {

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

                        albums.get(i).setAlbumName(album_field.getText().toString());
                        listView.setAdapter(new ArrayAdapter<Album>(Photos.this, R.layout.list_item1, albums));

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

        }   else{
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(Photos.this);
            builder
                    .setMessage("Delete this album?")


                    .setPositiveButton("Delete", (dialog, id1) -> {

                        albums.remove(i);
                        listView.setAdapter(new ArrayAdapter<Album>(Photos.this, R.layout.list_item1, albums));

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

        }



        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);




        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addItem: // User pressed add
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
                            listView.setAdapter(new ArrayAdapter<Album>(Photos.this, R.layout.list_item1, albums));

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



                break;
            case R.id.app_bar_search:
                LayoutInflater la = Photos.this.getLayoutInflater();
                View vi = la.inflate(R.layout.search_field, null);
                final EditText search_field = vi.findViewById(R.id.edit_text);
                search_field.setHint("type=value [AND/OR type=value]");

                AlertDialog.Builder build =
                        new AlertDialog.Builder(Photos.this);
                build
                        .setTitle("Search")
                        .setView(vi)
                        .setPositiveButton("Search", (d, id) -> {
                            search = search_field.getText().toString();
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

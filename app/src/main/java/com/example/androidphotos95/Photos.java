package com.example.androidphotos95;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Photos extends AppCompatActivity {

    //private ListView listView;
    private ListView albumsListView;
    //public static ArrayList<Album> albums = new ArrayList<>();
    //private static ArrayList<Album> albumList = new ArrayList<>();

    public static ArrayList<Album> albumList = new ArrayList<Album>();
    public static Album selectedAlbum = null;
    private ArrayAdapter<Album> albumArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.photos);
        setContentView(R.layout.activity_home);
        //added to de-Serialize Album

        File f = new File(getFilesDir(), "save_object.bin");
        if (f.exists()) {
            System.out.println("it is there");
            loadSerializedObject();
        }

        selectedAlbum = null;
        albumArrayAdapter = new ArrayAdapter<Album>(this, R.layout.list_item, albumList);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        albumsListView.setAdapter(albumArrayAdapter);
        albumsListView.setOnItemClickListener( (p,v,pos,id) -> selectAlbum(pos) );

    }

    private void selectAlbum(Object pos) {
        selectedAlbum = albumList.get((Integer) pos);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("restart");
        System.out.println(albumList.toString());
        loadSerializedObject();
        System.out.println(albumList.toString());
        albumArrayAdapter = new ArrayAdapter<Album>(this, R.layout.list_item, albumList);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        albumsListView.setAdapter(albumArrayAdapter);
        albumsListView.setOnItemClickListener( (p,v,pos,id) -> selectAlbum(pos) );
    }

    public Object loadSerializedObject(){
        try{
            File inputFile = new File(getFilesDir(),"save_object.bin");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));
            albumList = (ArrayList)ois.readObject();
            ois.close();

        }catch(java.io.IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Rename or Delete");
        getMenuInflater().inflate(R.menu.home_menu, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addItem: // User pressed add
                createAlbumDialog();
                break;
            case R.id.searchItem: // User pressed search
                if(albumList.isEmpty()) {
                    Toast.makeText(getBaseContext(), "You have no albums", Toast.LENGTH_LONG).show();
                    break;
                }
                searchAlbums();
                break;
            case R.id.openItem: // User pressed open
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                openAlbum();
                break;

            case R.id.deleteItem: // User pressed delete
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
            }
            deleteAlbumDialog();
            break;
            case R.id.renameItem: // User pressed rename
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                renameAlbumDialog();
                break;
        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    private void openAlbum() {
        Intent openIntent = new Intent(getApplicationContext(), ShowAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("album", selectedAlbum);
        bundle.putSerializable("album_list", albumList);
        openIntent.putExtras(bundle);
        startActivity(openIntent);
    }

    private void createAlbumDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter album name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString().trim();
                if (albumName.equals("")) {
                    Toast.makeText(getBaseContext(), "No name entered", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    createAlbum(albumName);
                } catch (Exception e) {
                    dialog.cancel();
                    Toast.makeText(getBaseContext(), "This album already exists.", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createAlbum (String albumName) throws Exception {
        for(Album a: albumList) {
            if(a.getName().equals(albumName)) {
                throw new Exception();
            }
        }

        Album newAlbum = new Album(albumName);
        albumList.add(newAlbum);
        // array adapter here
        albumArrayAdapter.notifyDataSetChanged();

        //Serialize Album
        try {
            File outputFile = new File(getFilesDir(),"save_object.bin");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            oos.writeObject(new ArrayList<Album>(albumList));
            oos.flush();
            oos.close();
        }catch (java.io.IOException e){
            System.out.println("saved album error");
            System.out.println(e.fillInStackTrace().toString());

        }
    }

    private void searchAlbums() {
        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("album_list", albumList);
        searchIntent.putExtras(bundle);
        startActivity(searchIntent);
    }


    private void deleteAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    deleteAlbum();

                } catch (Exception e) {
                    dialog.cancel();
                    // show toast saying album already exists
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteAlbum() {
        //System.out.println(selectedAlbum.toString());
        albumList.remove(selectedAlbum);
        //added to Serialize the ablum
        try {
            File outputFile = new File(getFilesDir(),"save_object.bin");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            oos.writeObject(new ArrayList<Album>(albumList));
            oos.flush();
            oos.close();
            System.out.println("saved album, album page, HomeActivity.java");
        }catch (java.io.IOException e){
            System.out.println("saved album error");
            System.out.println(e.fillInStackTrace().toString());

        }
        albumArrayAdapter.notifyDataSetChanged();
        selectedAlbum = null;
        albumsListView.setAdapter(albumArrayAdapter);
    }

    private void renameAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new album name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString().trim();
                if(albumName.equals("")) {
                    Toast.makeText(getBaseContext(), "No name entered", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    renameAlbum(albumName);
                } catch (Exception e) {
                    dialog.cancel();
                    Toast.makeText(getBaseContext(), "This album name already exists.", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void renameAlbum(String newName) throws Exception{
        for(Album a: albumList) {
            if(a.getName().equals(newName)) {
                throw new Exception();
            }
        }

        selectedAlbum.setAlbumName(newName);
        albumArrayAdapter.notifyDataSetChanged();
        //added to Serialize the ablum
        try {
            File outputFile = new File(getFilesDir(),"save_object.bin");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            oos.writeObject(new ArrayList<Album>(albumList));
            oos.flush();
            oos.close();
        }catch (java.io.IOException e){
            System.out.println("saved album error");
            System.out.println(e.fillInStackTrace().toString());

        }
    }


    private void noAlbumSelectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No album selected");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos);
        //setContentView(R.layout.activity_home);

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
    }*/

}

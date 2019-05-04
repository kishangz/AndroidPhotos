package com.example.androidphotos95;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Album implements Serializable, Comparable<Album> {

    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();

    }

    public String getName() {
        return this.name;
    }

    public void setAlbumName(String name) {
        this.name = name;

    }

    public ArrayList<Photo> getListOfPhotos(){
        return this.photos;
    }

    public void setPhotoHashMap(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
    }

    public String getAlbumSize() {
        String s = "" + photos.size();
        return s;
    }

    @Override
    public int compareTo(Album otherAlbum) {

        return this.name.compareTo(otherAlbum.getName());
    }

    public String toString() {
        return this.name + " (" + getAlbumSize() + " photos)";
    }
}

package com.example.androidphotos95;

import java.io.Serializable;
import java.util.HashMap;

public class Album implements Serializable, Comparable<Album> {

    private static final long serialVersionUID = 1L;
    private String name;
    private HashMap<String, Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new HashMap<String, Photo>();

    }

    public String getName() {

        return this.name;

    }

    public void setAlbumName(String name) {
        this.name = name;

    }

    public HashMap<String, Photo> getListOfPhotos(){
        return this.photos;
    }

    public void setPhotoHashMap(HashMap<String, Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.put(photo.getPhotoName(), photo);
    }

    public void removePhoto(String photoName) {
        this.photos.remove(photoName);
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
        return this.name;
    }
}

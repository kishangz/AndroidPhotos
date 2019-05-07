package com.example.androidphotos95;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.net.Uri;
import java.util.ArrayList;

public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String photoName;
    private transient Bitmap image;
    private transient Bitmap thumbnail;
    private File file;
    private Uri u;
    private ArrayList<Tag> listOfTags;


    public Photo(String photoName, Uri u) {
        this.listOfTags = new ArrayList<Tag>();
        this.u = u;
    }

    public Photo (Bitmap image,File file) {
        this.image = image;
        this.thumbnail = image;
        this.file = file;
    }

    public void addTag(String type, String value) {
        Tag newTag = new Tag(type, value);
        listOfTags.add(newTag);
    }

    public String getFile(){
        return this.file.toString();
    }

    private int getIndexForTagList(Tag newTag, int i) {

        int start = 0;
        int mid;
        int end = this.listOfTags.size()-1;

        while(end >= start) {

            mid = (start + end)/ 2;

            if(this.listOfTags.get(mid).equals(newTag)) {
                if(i ==1) {
                    return mid;
                }
                return -1;
            }
            else if (this.listOfTags.get(mid).toString().compareToIgnoreCase(newTag.toString()) < 0) {
                start = mid +1;
            }
            else {
                end = mid-1;
            }

        } if (i ==1) {
            return -1;
        }
        return start;
    }

    public void deleteTag(Tag tag) {

        int tagListdex = getIndexForTagList(tag, 1);
        this.listOfTags.remove(tagListdex);

    }

    public Uri getUri() {
        return u;
    }

    public ArrayList<Tag> getTags(){
        return this.listOfTags;
    }

    public String getPhotoName() {
        return photoName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String toString() {

        return this.photoName;
    }

}

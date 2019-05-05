package com.example.androidphotos95;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import static com.example.androidphotos95.ShowAlbum.photos;

public class GridAdapter extends BaseAdapter {

    private Context context;

    public GridAdapter(Context c) {
        context = c;
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i= null;

        if (convertView == null) {
            i = new ImageView(context);
            i.setLayoutParams(new GridView.LayoutParams(540, 540));
            i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            i = (ImageView) convertView;
        }

        i.setImageURI(photos.get(position).getUri());
        return i;

    }


}

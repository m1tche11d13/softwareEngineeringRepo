package com.mitchell.daniel.bricktracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class CustomSetAdapter extends ArrayAdapter<Set>{

    private final Context mContext;
    private final ArrayList<Set> mSetArrayList;

    public CustomSetAdapter(Context context, ArrayList<Set> setArrayList){
        super(context, R.layout.row, setArrayList);

        this.mContext = context;
        this.mSetArrayList = setArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView rowText = (TextView) rowView.findViewById(R.id.row_text);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.row_image);

        rowText.setText(mSetArrayList.get(position).getName());
        String url = mSetArrayList.get(position).getSetUrl();

        Log.e("PIC","SET IMG URL IS " + url);
        Log.e("PIC","SET URL IS " + mSetArrayList.get(position).getSetUrl());

        if(url != null && !url.equals("")){
            new DownloadImagesTask().execute(rowImage, url);
        }
        else{
            rowImage.setImageResource(R.drawable.blank_row);
        }

        return rowView;
    }
}
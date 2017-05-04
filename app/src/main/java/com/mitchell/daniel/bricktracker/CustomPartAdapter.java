package com.mitchell.daniel.bricktracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class CustomPartAdapter extends ArrayAdapter<Part>{

    private final Context mContext;
    private final ArrayList<Part> mPartArrayList;

    public CustomPartAdapter(Context context, ArrayList<Part> partArrayList){
        super(context, R.layout.row, partArrayList);

        this.mContext = context;
        this.mPartArrayList = partArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView rowText = (TextView) rowView.findViewById(R.id.row_text);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.row_image);

        rowText.setText(mPartArrayList.get(position).getName());
        String url = mPartArrayList.get(position).getPartImgUrl();

        if(url != null && !url.equals("")){
            new DownloadImagesTask().execute(rowImage, url);
        }
        else{
            rowImage.setImageResource(R.drawable.blank_row);
        }

        return rowView;
    }
}
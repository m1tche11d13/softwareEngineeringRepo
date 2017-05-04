package com.mitchell.daniel.bricktracker;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class CustomColorAdapter extends ArrayAdapter<Color> {

    private final Context mContext;
    private final ArrayList<Color> mColorArrayList;

    public CustomColorAdapter(Context context, ArrayList<Color> colorArrayList){
        super(context, R.layout.row, colorArrayList);

        this.mContext = context;
        this.mColorArrayList = colorArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);

        TextView rowText = (TextView) rowView.findViewById(R.id.row_text);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.row_image);

        rowText.setText(mColorArrayList.get(position).getName());
        String colorString = "#" + mColorArrayList.get(position).getRgb();

        rowImage.setColorFilter(android.graphics.Color.parseColor(colorString), PorterDuff.Mode.SRC_ATOP);

        return rowView;
    }
}
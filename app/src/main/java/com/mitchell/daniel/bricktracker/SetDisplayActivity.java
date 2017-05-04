package com.mitchell.daniel.bricktracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SetDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_display);

        ImageView imageView = (ImageView) findViewById(R.id.setDisplayImage);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        Bundle bundle = getIntent().getExtras();

        Set set = databaseHandler.getSet(bundle.getString("setId"));

        String setUrl = set.getSetImgUrl();

        if(setUrl != null && !setUrl.equals("")){
            new DownloadImagesTask().execute(imageView, setUrl);
        }
        else{
            imageView.setImageResource(R.drawable.blank_row);
        }
    }
}
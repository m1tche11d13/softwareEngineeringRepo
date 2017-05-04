package com.mitchell.daniel.bricktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        LinearLayout myCollection = (LinearLayout) findViewById(R.id.menu_my_collection);
        myCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent collectionIntent = new Intent(MainMenuActivity.this, MyCollectionActivity.class);
                startActivity(collectionIntent);
            }
        });

        LinearLayout browse = (LinearLayout) findViewById(R.id.menu_browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseIntent = new Intent(MainMenuActivity.this, BrowseActivity.class);
                startActivity(browseIntent);
            }
        });

        LinearLayout news = (LinearLayout) findViewById(R.id.menu_news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newsIntent = new Intent(MainMenuActivity.this, NewsActivity.class);
                startActivity(newsIntent);
            }
        });
    }
}
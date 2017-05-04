package com.mitchell.daniel.bricktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.view.View;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {

    private String currentAdapter;

    private ListView mListView;
    private final DatabaseHandler mDatabaseHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Log.e("BROWSE","Content view set");

        //Retrieves the lists of items from the SQLite database and creates listview adapters
        //for each of them
        ArrayList<Color> colorList = mDatabaseHandler.getAllColors();
        ArrayList<Set> setList = mDatabaseHandler.getAllSets();
        ArrayList<Part> partList = mDatabaseHandler.getAllParts();

        final CustomColorAdapter colorAdapter = new CustomColorAdapter(this, colorList);
        final CustomSetAdapter setAdapter = new CustomSetAdapter(this, setList);
        final CustomPartAdapter partAdapter = new CustomPartAdapter(this, partList);

        //Defines the buttons for switching between lists
        Button colorButton = (Button) findViewById(R.id.color_button);
        Button partButton = (Button) findViewById(R.id.part_button);
        Button setButton = (Button) findViewById(R.id.set_button);

        //Creates the ListView and sets the default view to parts
        mListView = (ListView) findViewById(R.id.browseListView);
        mListView.setAdapter(colorAdapter);
        currentAdapter = "color";

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch(currentAdapter){
                    case "set":
                        Set s = (Set) mListView.getItemAtPosition(position);
                        i = new Intent(BrowseActivity.this, SetDisplayActivity.class);
                        i.putExtra("setId",s.getNumber());
                        startActivity(i);
                        break;
                    case "part":
                        Part p = (Part) mListView.getItemAtPosition(position);
                        i = new Intent(BrowseActivity.this, PartDisplayActivity.class);
                        i.putExtra("partId",p.getNumber());
                        startActivity(i);
                        break;
                    case "color":
                        /*
                        Color c = (Color)mListView.getItemAtPosition(position);
                        i = new Intent(BrowseActivity.this, ColorDisplayActivity.class);
                        i.putExtra("colorId",c.getId());
                        startActivity(i);
                        */
                        break;
                    default:
                        Log.e("OUT","You shouldn't have gotten here...");
                }
            }
        });

        //OnClickListeners for the three buttons. Each just switches the list shown when clicked.
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setAdapter(colorAdapter);
                currentAdapter = "color";
            }
        });
        partButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setAdapter(partAdapter);
                currentAdapter = "part";
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setAdapter(setAdapter);
                currentAdapter = "set";
            }
        });
    }
}
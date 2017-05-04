package com.mitchell.daniel.bricktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PartDisplayActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_display);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        Bundle bundle = getIntent().getExtras();
        Part part = databaseHandler.getPart(bundle.getString("partId"));

        ImageView imageView = (ImageView) findViewById(R.id.partDisplayImage);


        TextView partName = (TextView) findViewById(R.id.text_part_name);
        TextView partCategory = (TextView) findViewById(R.id.text_part_category);
        TextView partInitialYear = (TextView) findViewById(R.id.text_initial_year);
        TextView partFinalYear = (TextView) findViewById(R.id.text_final_year);
        TextView partColorCount = (TextView) findViewById(R.id.colors_count);

        partName.setText(part.getName());
        partCategory.setText(Integer.toString(part.getPartCatId()));
        partInitialYear.setText(Integer.toString(part.getYearFrom()));
        partFinalYear.setText(Integer.toString(part.getYearTo()));
        partColorCount.setText(Integer.toString(part.getColorsList().size()));

        Spinner colorsSpinner = (Spinner) findViewById(R.id.parts_color_spinner);

        ListView setsList = (ListView) findViewById(R.id.partDisplayListView);

        String partUrl = part.getPartImgUrl();

        if(partUrl != null && !partUrl.equals("")){
            new DownloadImagesTask().execute(imageView, partUrl);
        }
        else{
            imageView.setImageResource(R.drawable.blank_row);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //No menu items yet
        return true;
    }
}
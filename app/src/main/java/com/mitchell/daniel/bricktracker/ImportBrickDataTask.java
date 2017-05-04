package com.mitchell.daniel.bricktracker;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class ImportBrickDataTask extends AsyncTask<Void, Void, Void>{

    //REST API code derived from
    //https://code.tutsplus.com/tutorials/android-from-scratch-using-rest-apis--cms-27117

    //JSON code derived from
    //http://stackoverflow.com/questions/15870114/android-jsonreader-cant-iterate-json-array

    final private String REBRICKABLE_API_KEY = "key 6c17c2c742a2c63ccf783df3f9c02a0b";

    private final Context mContext;
    private final DatabaseHandler db;

    ImportBrickDataTask(Context mContext){
        this.mContext = mContext;
        db = new DatabaseHandler(this.mContext);
    }

    private void getSets(){
        try{
            int count = 1;
            double stopCount = 0.0;
            boolean shouldContinue = true;
            while(shouldContinue) {
                Log.e("SET_COUNT", Integer.toString(db.getSetCount()));
                String baseURL = "http://rebrickable.com/api/v3/lego/sets/?page=";

                URL sets_endpoint = new URL(baseURL + Integer.toString(count));
                HttpURLConnection sets_connection = (HttpURLConnection) sets_endpoint.openConnection();

                sets_connection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

                while(sets_connection.getResponseCode() == 429);

                if (sets_connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream response = sets_connection.getInputStream();
                    InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                    JsonReader reader = new JsonReader(responseReader);

                    reader.beginObject();
                    while (reader.hasNext()) {
                        String key = reader.nextName();

                        switch(key){
                            case "count":
                                stopCount = 1.0 + reader.nextInt()/1000;
                                break;
                            case "results":
                                reader.beginArray();
                                while (reader.hasNext()){
                                    Set s = readSet(reader);
                                    Log.e("SET_ADD",s.toString());
                                    db.addSet(s);
                                }
                                reader.endArray();
                                break;
                            default:
                                reader.skipValue();
                        }
                    }
                } else Log.e("RESPONSE CODE", Integer.toString(sets_connection.getResponseCode()));

                if(count >= stopCount) shouldContinue = false;
                ++count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getParts(){
        try{
            int count = 1;
            double stopCount = 0.0;
            boolean shouldContinue = true;
            while(shouldContinue) {
                Log.e("PART_COUNT", Integer.toString(db.getPartCount()));
                String baseURL = "http://rebrickable.com/api/v3/lego/parts/?page=";

                URL rebrickableEndpoint = new URL(baseURL + Integer.toString(count));
                HttpURLConnection myConnection = (HttpURLConnection) rebrickableEndpoint.openConnection();

                myConnection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

                while(myConnection.getResponseCode() == 429);

                if (myConnection.getResponseCode() == 200) {

                    InputStream response = myConnection.getInputStream();
                    InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                    JsonReader reader = new JsonReader(responseReader);

                    reader.beginObject();
                    while (reader.hasNext()) {

                        String key = reader.nextName();

                        switch (key){
                            case "count":
                                stopCount = 1.0 + reader.nextInt()/1000;
                                break;
                            case "results":
                                reader.beginArray();
                                while (reader.hasNext()) db.addPart(readPart(reader));
                                reader.endArray();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                } else Log.e("RESPONSE CODE", Integer.toString(myConnection.getResponseCode()));

                if(count >= stopCount) shouldContinue = false;
                ++count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getColors(){
        try {
            Log.e("COLOR_COUNT", Integer.toString(db.getColorCount()));
            URL colors_url = new URL("http://rebrickable.com/api/v3/lego/colors/");
            HttpURLConnection colors_connection = (HttpURLConnection) colors_url.openConnection();

            colors_connection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

            if(colors_connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                InputStream response = colors_connection.getInputStream();
                InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                JsonReader reader = new JsonReader(responseReader);

                reader.beginObject();
                while(reader.hasNext()){
                    String key = reader.nextName();

                    if(key.equals("results")){
                        reader.beginArray();
                        while(reader.hasNext()) db.addColor(readColor(reader));
                        reader.endArray();
                    }
                    else reader.skipValue();
                }

            }
            else Log.e("RESPONSE CODE", Integer.toString(colors_connection.getResponseCode()));
        } catch (Exception e) {e.printStackTrace();}
    }

    private Part readPart(JsonReader reader) throws Exception {
        int part_cat_id = 0, year_from = 0, year_to = 0;
        String name = "", part_num = "", part_url = "", part_image_url = "";
        ArrayList<Color> colors_list = new ArrayList<>();

        reader.beginObject();
        while(reader.hasNext()){
            String key = reader.nextName();
            switch(key){
                case "part_num":
                    part_num = reader.nextString();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "part_cat_id":
                    part_cat_id = reader.nextInt();
                    break;
                case "part_url":
                    part_url = reader.nextString();
                    break;
                case "part_img_url":
                    try { part_image_url = reader.nextString(); }
                    catch(Exception e) { reader.skipValue(); }
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        try{
            String parts_url = "http://rebrickable.com/api/v3/lego/parts/" + part_num + "/";

            URL parts_endpoint = new URL(parts_url);
            HttpURLConnection parts_connection = (HttpURLConnection) parts_endpoint.openConnection();

            parts_connection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

            while(parts_connection.getResponseCode() == 429);

            if (parts_connection.getResponseCode() == 200) {

                InputStream response = parts_connection.getInputStream();
                InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                JsonReader parts_reader = new JsonReader(responseReader);

                parts_reader.beginObject();
                while (parts_reader.hasNext()) {

                    String key = parts_reader.nextName();
                    switch (key){
                        case "year_from":
                            year_from = parts_reader.nextInt();
                            break;
                        case "year_to":
                            year_to = parts_reader.nextInt();
                            break;
                        default:
                            parts_reader.skipValue();
                            break;
                    }
                }
                parts_reader.endObject();
            } else Log.e("RESPONSE CODE", Integer.toString(parts_connection.getResponseCode()));
        } catch (Exception e) { e.printStackTrace(); }

        try{
            String part_colors_url = "http://rebrickable.com/api/v3/lego/sets/" + part_num + "/parts/";

            URL part_colors_endpoint = new URL(part_colors_url);
            HttpURLConnection part_colors_connection = (HttpURLConnection) part_colors_endpoint.openConnection();

            part_colors_connection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

            while(part_colors_connection.getResponseCode() == 429);

            if (part_colors_connection.getResponseCode() == 200) {

                InputStream response = part_colors_connection.getInputStream();
                InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                JsonReader part_colors_reader = new JsonReader(responseReader);

                part_colors_reader.beginObject();
                while (part_colors_reader.hasNext()) {

                    String key = part_colors_reader.nextName();
                    switch (key){
                        case "results":
                            part_colors_reader.beginArray();
                            while(reader.hasNext()){
                                Color c = readPartColor(part_colors_reader);
                                Log.e("CCCC",c.toString());
                                colors_list.add(c);
                            }
                            part_colors_reader.endArray();
                            break;
                        default:
                            part_colors_reader.skipValue();
                            break;
                    }
                }
            } else Log.e("RESPONSE CODE", Integer.toString(part_colors_connection.getResponseCode()));
        } catch (Exception e) { e.printStackTrace(); }

        return new Part(part_num, name, part_cat_id, part_url, part_image_url, year_from, year_to, colors_list);
    }
    private Color readColor(JsonReader reader) throws Exception {
        int id = 0;
        String name = "", rgb = "";
        boolean is_trans = true;

        reader.beginObject();
        while(reader.hasNext()){
            String key = reader.nextName();
            switch(key){
                case "id":
                    id = reader.nextInt();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "rgb":
                    rgb = reader.nextString();
                    break;
                case "is_trans":
                    is_trans = reader.nextBoolean();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new Color(id, name, rgb, is_trans);
    }
    private Color readPartColor(JsonReader reader) throws Exception {
        int id = 0;
        Color c = new Color();

        reader.beginObject();
        while(reader.hasNext()){
            String key = reader.nextName();
            switch(key){
                case "color_id":
                    id = reader.nextInt();
                    try{
                        URL colors_url = new URL("http://rebrickable.com/api/v3/lego/colors/" + Integer.toString(id));
                        HttpURLConnection colors_connection = (HttpURLConnection) colors_url.openConnection();

                        colors_connection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

                        if(colors_connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                            InputStream response = colors_connection.getInputStream();
                            InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                            JsonReader color_reader = new JsonReader(responseReader);

                            c = readColor(color_reader);
                        }
                        else Log.e("RESPONSE CODE", Integer.toString(colors_connection.getResponseCode()));
                    }
                    catch(Exception e){Log.e("EXCEPTION", e.toString());}
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return c;
    }
    private Set readSet(JsonReader reader) throws Exception{
        int year = 0, theme_id = 0, num_parts = 0;
        String number = "", name = "", set_img_url = "", set_url = "";
        ArrayList<SetPiece> pieces_list = new ArrayList<>();

        reader.beginObject();
        while(reader.hasNext()){
            String key = reader.nextName();
            switch(key){
                case "set_num":
                    number = reader.nextString();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "year":
                    year = reader.nextInt();
                    break;
                case "theme_id":
                    theme_id = reader.nextInt();
                    break;
                case "num_parts":
                    num_parts = reader.nextInt();
                    break;
                case "set_img_url":
                    try { set_img_url = reader.nextString(); }
                    catch(Exception e) { reader.skipValue(); }
                    break;
                case "set_url":
                    set_url = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        try{
            String set_parts_url = "http://rebrickable.com/api/v3/lego/sets/" + number + "/parts/";

            URL set_parts_endpoint = new URL(set_parts_url);
            HttpURLConnection set_parts_connection = (HttpURLConnection) set_parts_endpoint.openConnection();

            set_parts_connection.setRequestProperty("Authorization", REBRICKABLE_API_KEY);

            while(set_parts_connection.getResponseCode() == 429);

            if (set_parts_connection.getResponseCode() == 200) {

                InputStream response = set_parts_connection.getInputStream();
                InputStreamReader responseReader = new InputStreamReader(response, "UTF-8");
                JsonReader set_parts_reader = new JsonReader(responseReader);

                set_parts_reader.beginObject();
                while (set_parts_reader.hasNext()) {

                    String key = set_parts_reader.nextName();
                    switch (key){
                        case "results":
                            set_parts_reader.beginArray();
                            while(reader.hasNext())pieces_list.add(readSetPiece(set_parts_reader));
                            set_parts_reader.endArray();
                            break;
                        default:
                            set_parts_reader.skipValue();
                            break;
                    }
                }
            } else Log.e("RESPONSE CODE", Integer.toString(set_parts_connection.getResponseCode()));
        } catch (Exception e) { e.printStackTrace(); }

        return new Set(number, name, year, theme_id, num_parts, set_img_url, set_url, pieces_list);
    }
    private SetPiece readSetPiece(JsonReader reader) throws Exception {
        String part_number = "";
        int color_id = 0;
        int amount = 0;

        reader.beginObject();
        while(reader.hasNext()){
            String key = reader.nextName();
            switch(key){
                case "part":
                    reader.beginObject();
                    while(reader.hasNext()){
                        String partKey = reader.nextName();
                        switch(partKey){
                            case "part_num":
                                part_number = reader.nextString();
                                break;
                            default:
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                    break;
                case "color":
                    reader.beginObject();
                    while(reader.hasNext()){
                        String colorKey = reader.nextName();
                        switch(colorKey){
                            case "id":
                                color_id = reader.nextInt();
                                break;
                            default:
                                reader.skipValue();
                        }
                    }
                    reader.endObject();
                break;
                case "quantity":
                    amount = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new SetPiece(part_number, color_id, amount);
    }

    @Override
    protected void onPreExecute(){
        Log.e("Pre","Pre-execution");
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params){
        //Log.e("COLOR_COUNT", Integer.toString(db.getColorCount()));
        //Log.e("PART_COUNT", Integer.toString(db.getPartCount()));
        //Log.e("SET_COUNT", Integer.toString(db.getSetCount()));
        //getColors();
        //getSets();
        //getParts();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        Log.e("Post","Post-execution");

        Log.e("COLOR_COUNT", Integer.toString(db.getColorCount()));
        Log.e("PART_COUNT", Integer.toString(db.getPartCount()));
        Log.e("SET_COUNT", Integer.toString(db.getSetCount()));

        Log.e("-->", "Starting mainMenuActivity");

        //Start main menu when this activity is done.
        Intent intent = new Intent(mContext, MainMenuActivity.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }
}

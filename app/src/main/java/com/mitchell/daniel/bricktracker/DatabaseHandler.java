package com.mitchell.daniel.bricktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "brickDatabase";

    private static final String TABLE_SETS = "sets";
    private static final String TABLE_PARTS = "parts";
    private static final String TABLE_COLORS = "colors";

    private static final String KEY_SET_NUMBER = "snumber";
    private static final String KEY_SET_NAME = "sname";
    private static final String KEY_SET_YEAR = "syear";
    private static final String KEY_SET_THEME_ID = "stheme_id";
    private static final String KEY_SET_NUM_PARTS = "snum_parts";
    private static final String KEY_SET_URL = "surl";
    private static final String KEY_SET_IMG_URL = "simg_url";
    private static final String KEY_SET_PARTS_LIST = "sparts_list";

    private static final String KEY_PART_NUMBER = "pnumber";
    private static final String KEY_PART_NAME = "pname";
    private static final String KEY_PART_CAT_ID = "pcat_id";
    private static final String KEY_PART_URL = "purl";
    private static final String KEY_PART_IMG_URL = "pimg_url";
    private static final String KEY_PART_YEAR_FROM = "pyear_from";
    private static final String KEY_PART_YEAR_TO = "pyear_to";
    private static final String KEY_PART_COLORS_LIST = "pcolors_list";

    private static final String KEY_COLOR_ID = "cid";
    private static final String KEY_COLOR_NAME = "cname";
    private static final String KEY_COLOR_RGB = "crgb";
    private static final String KEY_COLOR_IS_TRANS = "cis_trans";

    DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_SETS_TABLE =  "CREATE TABLE " + TABLE_SETS + "(" + KEY_SET_NUMBER + " TEXT PRIMARY KEY," + KEY_SET_NAME + " TEXT," + KEY_SET_YEAR + " INTEGER," + KEY_SET_THEME_ID + " INTEGER," + KEY_SET_NUM_PARTS + " INTEGER," + KEY_SET_URL + " TEXT," + KEY_SET_IMG_URL + " TEXT," + KEY_SET_PARTS_LIST + " TEXT)";
        String CREATE_PARTS_TABLE = "CREATE TABLE " + TABLE_PARTS + "(" + KEY_PART_NUMBER + " TEXT PRIMARY KEY, " + KEY_PART_NAME + " TEXT," + KEY_PART_CAT_ID + " INTEGER," + KEY_PART_URL + " TEXT," + KEY_PART_IMG_URL + " TEXT," + KEY_PART_YEAR_FROM + " INTEGER," + KEY_PART_YEAR_TO + " INTEGER," + KEY_PART_COLORS_LIST + " TEXT)";
        String CREATE_COLORS_TABLE = "CREATE TABLE " + TABLE_COLORS + "(" + KEY_COLOR_ID + " INTEGER PRIMARY KEY," + KEY_COLOR_NAME + " TEXT," + KEY_COLOR_RGB + " TEXT," + KEY_COLOR_IS_TRANS + " INTEGER)";
        db.execSQL(CREATE_SETS_TABLE);
        db.execSQL(CREATE_PARTS_TABLE);
        db.execSQL(CREATE_COLORS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLORS);
        onCreate(db);
    }

    void addSet(Set set){
        Gson gson = new Gson();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        ArrayList<SetPiece> partArrayList = set.getPartsList();
        String json = gson.toJson(partArrayList);

        values.put(KEY_SET_NUMBER, set.getNumber());
        values.put(KEY_SET_NAME, set.getName());
        values.put(KEY_SET_YEAR, set.getYear());
        values.put(KEY_SET_THEME_ID, set.getThemeId());
        values.put(KEY_SET_NUM_PARTS, set.getNumParts());
        values.put(KEY_SET_URL, set.getSetUrl());
        values.put(KEY_SET_IMG_URL, set.getSetImgUrl());
        values.put(KEY_SET_PARTS_LIST, json);

        db.insertWithOnConflict(TABLE_SETS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    void addPart(Part part){
        Gson gson = new Gson();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        ArrayList<Color> colorArrayList = part.getColorsList();
        String json = gson.toJson(colorArrayList);

        values.put(KEY_PART_NUMBER, part.getNumber());
        values.put(KEY_PART_NAME, part.getName());
        values.put(KEY_PART_CAT_ID, part.getPartCatId());
        values.put(KEY_PART_URL, part.getPartUrl());
        values.put(KEY_PART_IMG_URL, part.getPartImgUrl());
        values.put(KEY_PART_YEAR_FROM, part.getYearFrom());
        values.put(KEY_PART_YEAR_TO, part.getYearTo());
        values.put(KEY_PART_COLORS_LIST, json);

        db.insertWithOnConflict(TABLE_PARTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    void addColor(Color color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COLOR_ID, color.getId());
        values.put(KEY_COLOR_NAME, color.getName());
        values.put(KEY_COLOR_RGB, color.getRgb());
        values.put(KEY_COLOR_IS_TRANS, color.getIsTrans());

        db.insertWithOnConflict(TABLE_COLORS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    boolean containsSet(String number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SETS, new String[] {KEY_SET_NUMBER, KEY_SET_NAME, KEY_SET_YEAR, KEY_SET_THEME_ID, KEY_SET_NUM_PARTS, KEY_SET_URL, KEY_SET_IMG_URL, KEY_SET_PARTS_LIST}, KEY_SET_NUMBER + "=?", new String[] { String.valueOf(number) }, null, null, null, null);
        return cursor != null;
    }

    Set getSet(String number){
        Gson gson = new Gson();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SETS, new String[] {KEY_SET_NUMBER, KEY_SET_NAME, KEY_SET_YEAR, KEY_SET_THEME_ID, KEY_SET_NUM_PARTS, KEY_SET_URL, KEY_SET_IMG_URL, KEY_SET_PARTS_LIST}, KEY_SET_NUMBER + "=?", new String[] { String.valueOf(number) }, null, null, null, null);
        if(cursor != null) cursor.moveToFirst();
        else return new Set();

        Type type = new TypeToken<ArrayList<SetPiece>>(){}.getType();
        ArrayList<SetPiece> setPieceArrayList = gson.fromJson(cursor.getString(7), type);

        Set s = new Set(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6), setPieceArrayList);
        cursor.close();
        return s;
    }
    Part getPart(String number){
        Gson gson = new Gson();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTS, new String[] {KEY_PART_NUMBER, KEY_PART_NAME, KEY_PART_CAT_ID, KEY_PART_URL, KEY_PART_IMG_URL, KEY_PART_YEAR_FROM, KEY_PART_YEAR_TO, KEY_PART_COLORS_LIST}, KEY_PART_NUMBER + "=?", new String[] {String.valueOf(number) }, null, null, null, null);
        if(cursor != null) cursor.moveToFirst();
        else return new Part();

        Type type = new TypeToken<ArrayList<SetPiece>>(){}.getType();
        Log.e("Colors",cursor.getString(7));
        ArrayList<Color> colorArrayList = gson.fromJson(cursor.getString(7), type);

        Part p = new Part(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), colorArrayList);
        cursor.close();
        return p;
    }
    public Color getColor(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COLORS, new String[] {KEY_COLOR_ID, KEY_COLOR_NAME, KEY_COLOR_RGB, KEY_COLOR_IS_TRANS}, KEY_COLOR_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null)cursor.moveToFirst();
        Color c = new Color(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Boolean.parseBoolean(cursor.getString(3)));
        cursor.close();
        return c;
    }

    int getSetCount(){
        String countQuery = "SELECT * FROM " + TABLE_SETS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }
    int getPartCount(){
        String countQuery = "SELECT * FROM " + TABLE_PARTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }
    int getColorCount(){
        String countQuery = "SELECT * FROM " + TABLE_COLORS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int i = cursor.getCount();
        cursor.close();
        return i;
    }

    ArrayList<Set> getAllSets(){
        ArrayList<Set> setList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SETS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Gson gson = new Gson();

                Type type = new TypeToken<ArrayList<SetPiece>>(){}.getType();
                ArrayList<SetPiece> setPieceArrayList = gson.fromJson(cursor.getString(7), type);

                setList.add(new Set(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6), setPieceArrayList));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        return setList;
    }
    ArrayList<Part> getAllParts(){
        ArrayList<Part> partList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PARTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Gson gson = new Gson();

                Log.e("Colors",cursor.getString(7));

                Type type = new TypeToken<ArrayList<SetPiece>>(){}.getType();
                ArrayList<Color> colorArrayList = gson.fromJson(cursor.getString(7), type);

                partList.add(new Part(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)), colorArrayList));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        return partList;
    }
    ArrayList<Color> getAllColors(){
        ArrayList<Color> colorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COLORS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Color color = new Color();
                color.setId(Integer.parseInt(cursor.getString(0)));
                color.setName(cursor.getString(1));
                color.setRgb(cursor.getString(2));
                color.setIsTrans(Boolean.parseBoolean(cursor.getString(3)));

                colorList.add(color);
            }
            while(cursor.moveToNext());
        }

        return colorList;
    }
}


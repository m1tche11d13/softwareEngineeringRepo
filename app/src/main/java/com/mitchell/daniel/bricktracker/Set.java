package com.mitchell.daniel.bricktracker;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Set {
    private String number;
    private String name;
    private int year;
    private int theme_id;
    private int num_parts;
    private String set_img_url;
    private String set_url;
    private ArrayList<SetPiece> pieces_list;

    public Set(){

    }

    public Set(String number_, String name_, int year_, int theme_id_, int num_parts_, String set_img_url_, String set_url_, ArrayList<SetPiece> pieces_list_){
        number = number_;
        name = name_;
        year = year_;
        theme_id = theme_id_;
        num_parts = num_parts_;
        set_img_url = set_img_url_;
        set_url = set_url_;
        pieces_list = pieces_list_;
    }

    public String getNumber() { return number; }
    public String getName() {return name; }
    public int getYear() { return year; }
    public int getThemeId() {return theme_id; }
    public int getNumParts() {return num_parts; }
    public String getSetImgUrl() {return set_img_url; }
    public String getSetUrl() {return set_url; }
    public ArrayList<SetPiece> getPartsList(){return pieces_list;}

    public void setNumber(String number_){number = number_;}
    public void setName(String name_) {name = name_;}
    public void setYear(int year_){year = year_;}
    public void setThemeId(int theme_id_){theme_id = theme_id_;}
    public void setNumParts(int num_parts_){num_parts = num_parts_;}
    public void setSetImgUrl(String set_img_url_){set_img_url = set_img_url_;}
    public void setSetUrl(String set_url_){set_url = set_url_;}
    public void setPartsList(ArrayList<SetPiece> pieces_list_){pieces_list = pieces_list_;}

    @Override
    public String toString(){
        String t = "";
        t += "Set number " + number + " is called " + name + " and was produced for the first time in " + Integer.toString(year) + ".\n";
        t += "It contains " + Integer.toString(num_parts) + " parts and is part of theme " + Integer.toString(theme_id) + ", it can be found at " + set_url + ".\n";

        t += "The set contains " + Integer.toString(pieces_list.size()) + " unique pieces.\n";

        for(SetPiece sp : pieces_list){
            t += sp.toString() + "\n";
        }

        return t;
    }

}

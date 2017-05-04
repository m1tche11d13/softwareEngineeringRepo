package com.mitchell.daniel.bricktracker;

import java.util.ArrayList;

public class Part {

    private String number;
    private String name;
    private int part_cat_id;
    private String part_url;
    private String part_img_url;
    private int year_from;
    private int year_to;
    private ArrayList<Color> colors_list;

    public Part(){

    }

    public Part(String number_, String name_, int part_cat_id_, String part_url_, String part_img_url_, int year_from_, int year_to_, ArrayList<Color> colors_list_){
        number = number_;
        name = name_;
        part_cat_id = part_cat_id_;
        part_url = part_url_;
        part_img_url = part_img_url_;
        year_from = year_from_;
        year_to = year_to_;
        colors_list = colors_list_;
    }

    public String getNumber() {return number;}
    public String getName() {return name;}
    public int getPartCatId(){return part_cat_id;}
    public String getPartUrl(){return part_url;}
    public String getPartImgUrl(){return part_img_url;}
    public int getYearFrom(){return year_from;}
    public int getYearTo(){return year_to;}
    public ArrayList<Color> getColorsList(){return colors_list;}

    public void setNumber(String number_){number = number_;}
    public void setName(String name_){name = name_;}
    public void setPartCatId(int part_cat_id_){part_cat_id = part_cat_id_;}
    public void setPartUrl(String part_url_){part_url = part_url_;}
    public void setPartImgUrl(String part_img_url_){part_img_url = part_img_url_;}
    public void setYearFrom(int year_from_){year_from = year_from_;}
    public void setYearTo(int year_to_){year_to = year_to_;}
    public void setColorsList(ArrayList<Color> colors_list_){colors_list = colors_list_;}

    @Override
    public String toString() {
        return name;
    }
}

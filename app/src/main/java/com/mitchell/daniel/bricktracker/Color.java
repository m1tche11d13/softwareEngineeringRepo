package com.mitchell.daniel.bricktracker;

class Color {
    //Private variables
    private int id;
    private String name;
    private String rgb;
    private boolean is_trans;

    //Constructors
    Color(){
    }

    Color (int id_, String name_, String rgb_, Boolean is_trans_){
        id = id_;
        name = name_;
        rgb = rgb_;
        is_trans = is_trans_;
    }

    public int getId(){return id;}
    public String getName(){return name;}
    public String getRgb(){return rgb;}
    public boolean getIsTrans(){return is_trans;}

    public void setId(int id_){id = id_;}
    public void setName(String name_){name = name_;}
    public void setRgb(String rgb_){rgb = rgb_;}
    public void setIsTrans(boolean is_trans_){is_trans = is_trans_;}

    @Override
    public String toString(){
        return "Id: " + Integer.toString(id) + "\nName: " + name + "\nRGB:  " + rgb + "\nTrans: " + Boolean.toString(is_trans);
    }
}

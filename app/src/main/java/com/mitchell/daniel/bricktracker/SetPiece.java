package com.mitchell.daniel.bricktracker;

public class SetPiece {
    private String part_number;
    private int color_id;
    private int amount;

    public SetPiece(){

    }

    public SetPiece(String part_number_, int color_id_, int amount_){
        part_number = part_number_;
        color_id = color_id_;
        amount = amount_;
    }

    public String getPartNumber(){return part_number;}
    public int getColorId(){return color_id;}
    public int getAmount(){return amount;}

    public void setPartNumber(String part_number_){part_number = part_number_;}
    public void setColorId(int color_id_){color_id = color_id_;}
    public void setAmount(int amount_){amount = amount_;}

    @Override
    public String toString() {
        return Integer.toString(amount) + "x    " + part_number;
    }
}

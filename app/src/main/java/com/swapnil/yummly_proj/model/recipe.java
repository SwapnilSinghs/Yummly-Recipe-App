package com.swapnil.yummly_proj.model;

public class recipe {

    private String mtitle;
    private String mimageUrl;
    private int mRating;
    private String mRecipe;

    public recipe(String imageUrl,String text,int rating,String  recipes ){
        mtitle = text;
        mimageUrl = imageUrl;
        mRating = rating;
        mRecipe = recipes;

    }

    public String getImageUrl(){
        return mimageUrl;
    }

    public String getText(){
        return mtitle;
    }

    public int getRating(){ return mRating; }

    public String getRecipe() { return mRecipe; }


}

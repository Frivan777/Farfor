package com.ivanfrankov.android.farfor;


public class CategoryFood {

    private String mName;
    private int mRscImg;
    private int mId;

    public CategoryFood(String name, int rsc, int id) {
        mName = name;
        mRscImg = rsc;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public int getRscImg() {
        return mRscImg;
    }

    public int getId() {
        return mId;
    }
}

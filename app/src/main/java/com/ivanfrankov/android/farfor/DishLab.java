package com.ivanfrankov.android.farfor;


import java.util.ArrayList;
import java.util.UUID;

public class DishLab {
    private static DishLab sDishLab;
    private ArrayList<Dish> mList;

    private DishLab() {
        mList = new ArrayList<Dish>();
    }

    public static DishLab get() {
        if (sDishLab == null) {
            sDishLab = new DishLab();
        }
        return sDishLab;
    }

    public ArrayList<Dish> getDishList() {
        return mList;
    }

    public void setDishList(ArrayList<Dish> list) {
        mList = list;
    }

    public Dish getDish(UUID uuid) {
        for (Dish d : mList) {
            if (d.getUuid().equals(uuid)) {
                return d;
            }
        }
        return null;
    }

    public void getDishList(ArrayList<Dish> list, int id) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getId() == id) {
                list.add(mList.get(i));
            }
        }
    }
}

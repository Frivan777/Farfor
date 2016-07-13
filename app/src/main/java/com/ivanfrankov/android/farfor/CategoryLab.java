package com.ivanfrankov.android.farfor;


import java.util.ArrayList;

public class CategoryLab {

    private ArrayList<CategoryFood> mCategoriesFood;
    private static CategoryLab sLab;

    private CategoryLab() {
        mCategoriesFood = new ArrayList<CategoryFood>();
        mCategoriesFood.add(new CategoryFood("Шашлыки", R.drawable.img1, 24));
        mCategoriesFood.add(new CategoryFood("Добавки", R.drawable.img13, 23));
        mCategoriesFood.add(new CategoryFood("Закуски", R.drawable.img2, 20));
        mCategoriesFood.add(new CategoryFood("Роллы", R.drawable.img3, 18));
        mCategoriesFood.add(new CategoryFood("Десерты", R.drawable.img4, 10));
        mCategoriesFood.add(new CategoryFood("Напитки", R.drawable.img5, 9));
        mCategoriesFood.add(new CategoryFood("Теплое", R.drawable.img6, 8));
        mCategoriesFood.add(new CategoryFood("Салаты", R.drawable.img7, 7));
        mCategoriesFood.add(new CategoryFood("Супы", R.drawable.img8, 6));
        mCategoriesFood.add(new CategoryFood("Суши", R.drawable.img9, 5));
        mCategoriesFood.add(new CategoryFood("Лапша", R.drawable.img10, 3));
        mCategoriesFood.add(new CategoryFood("Сеты", R.drawable.img11, 2));
        mCategoriesFood.add(new CategoryFood("Пицца", R.drawable.img12, 1));
    }

    public static CategoryLab get() {
        if (sLab == null)
            sLab = new CategoryLab();
        return sLab;
    }

    public ArrayList<CategoryFood> getCategories() {
        return mCategoriesFood;
    }

    public CategoryFood getCategoryFood(int id) {
        for (CategoryFood cF : mCategoriesFood) {
            if (cF.getId() == id) {
                return cF;
            }
        }
        return null;
    }
}

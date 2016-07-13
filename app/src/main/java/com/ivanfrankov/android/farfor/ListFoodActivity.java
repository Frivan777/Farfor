package com.ivanfrankov.android.farfor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

public class ListFoodActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        int idCategory = getIntent().getIntExtra(ListFoodFragment.EXTRA_ID, 24);
        return ListFoodFragment.newInstance(idCategory);
    }

}

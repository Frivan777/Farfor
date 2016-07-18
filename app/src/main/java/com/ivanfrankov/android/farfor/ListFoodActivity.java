package com.ivanfrankov.android.farfor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import java.util.UUID;

public class ListFoodActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        int idCategory = getIntent().getIntExtra(ListFoodFragment.EXTRA_ID, 24);
        return ListFoodFragment.newInstance(idCategory);
    }

    public static Intent newIntent(Context packageContext, int idCategory) {
        Intent i = new Intent(packageContext, ListFoodActivity.class);
        i.putExtra(ListFoodFragment.EXTRA_ID, idCategory);

        return  i;
    }

}

package com.ivanfrankov.android.farfor;


import android.support.v4.app.Fragment;

import java.util.UUID;

public class AboutDishActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AboutDishFragment.newInstance(getIntent().getByteArrayExtra(AboutDishFragment.EXTRA_BITMAP),
                (UUID)getIntent().getSerializableExtra(AboutDishFragment.EXTRA_UUID),
                getIntent().getStringExtra(AboutDishFragment.EXTRA_NAME));
    }
}

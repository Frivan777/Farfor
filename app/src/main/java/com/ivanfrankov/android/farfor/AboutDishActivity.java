package com.ivanfrankov.android.farfor;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class AboutDishActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AboutDishFragment.newInstance(getIntent().getByteArrayExtra(AboutDishFragment.EXTRA_BITMAP),
                (UUID)getIntent().getSerializableExtra(AboutDishFragment.EXTRA_UUID),
                getIntent().getStringExtra(AboutDishFragment.EXTRA_NAME));
    }

    public static Intent newIntent(Context packageContext, byte[] bytes, UUID uuid, String name) {
        Intent i = new Intent(packageContext, AboutDishActivity.class);
        i.putExtra(AboutDishFragment.EXTRA_BITMAP, bytes);
        i.putExtra(AboutDishFragment.EXTRA_UUID, uuid);
        i.putExtra(AboutDishFragment.EXTRA_NAME, name);

        return  i;
    }
}

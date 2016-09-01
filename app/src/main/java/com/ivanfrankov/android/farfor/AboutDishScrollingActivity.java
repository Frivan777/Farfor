package com.ivanfrankov.android.farfor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class AboutDishScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(AboutDishFragment.EXTRA_NAME));
        setContentView(R.layout.activity_about_dish_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        byte[] buffer = getIntent().getByteArrayExtra(AboutDishFragment.EXTRA_BITMAP);
        UUID position = (UUID)getIntent().getSerializableExtra(AboutDishFragment.EXTRA_UUID);

        Dish dish = DishLab.get().getDish(position);

        ImageView imageView = (ImageView)findViewById(R.id.dish_dialog_imageView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        imageView.setImageBitmap(bitmap);
        TextView textViewPrice = (TextView)findViewById(R.id.dish_dialog_price_textView);
        textViewPrice.setText(dish.getPrice() + " р");
        TextView textViewDescription = (TextView)findViewById(R.id.dish_dialog_description_textView);
        textViewDescription.setText("Описание:" + dish.getDescription());
        TextView textViewWeight = (TextView)findViewById(R.id.dish_dialog_weight_textView);
        textViewWeight.setText(dish.getWeight());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Покупайте белорусское =)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent newIntent(Context packageContext, byte[] bytes, UUID uuid, String name) {
        Intent i = new Intent(packageContext, AboutDishScrollingActivity.class);
        i.putExtra(AboutDishFragment.EXTRA_BITMAP, bytes);
        i.putExtra(AboutDishFragment.EXTRA_UUID, uuid);
        i.putExtra(AboutDishFragment.EXTRA_NAME, name);

        return  i;
    }
}

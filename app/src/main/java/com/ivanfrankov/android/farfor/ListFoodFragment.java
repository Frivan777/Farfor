package com.ivanfrankov.android.farfor;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ListFoodFragment extends ListFragment {

    public static final String EXTRA_ID = "com.android.ivanfrankov.farfor.id";
    private static final String TAG = "dialog_dish";
    private int mId;
    private ArrayList<Dish> mDishList;
    ImageDownloader<ImageView> mDownloader;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mId = getArguments().getInt(EXTRA_ID);
        getActivity().setTitle(CategoryLab.get().getCategoryFood(mId).getName());
        mDishList = new ArrayList<Dish>();
        DishLab.get().getDishList(mDishList, mId);

        FoodAdapter adapter = new FoodAdapter(mDishList);
        setListAdapter(adapter);



        mDownloader = new ImageDownloader<ImageView>(new Handler());
        mDownloader.setListener(new ImageDownloader.Listener<ImageView>() {
            @Override
            public void ImageDownloaded(ImageView token, Bitmap bitmap) {
                if(isVisible()) token.setImageBitmap(bitmap);
            }
        });

        mDownloader.start();
        mDownloader.getLooper();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(CategoryLab.get().getCategoryFood(mId).getName());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Dish dish = ((FoodAdapter)getListAdapter()).getItem(position);
        showDish(v, dish);
    }

    public static ListFoodFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ID, id);
        ListFoodFragment fragment = new ListFoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void showDish(View v, Dish dish) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ImageView imageView = (ImageView)v.findViewById(R.id.food_list_item_imageView);
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        Intent i = new Intent(getActivity(), AboutDishActivity.class);
        i.putExtra(AboutDishFragment.EXTRA_BITMAP, bos.toByteArray());
        i.putExtra(AboutDishFragment.EXTRA_UUID, dish.getUuid());
        i.putExtra(AboutDishFragment.EXTRA_NAME, dish.getName());
        //AboutDishDialog dialog = AboutDishDialog.newInstance(bos.toByteArray(), uuid);
        //dialog.show(getActivity().getSupportFragmentManager(), TAG);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloader.quit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDownloader.clearQueue();
    }


    private class FoodAdapter extends ArrayAdapter<Dish> {
        public FoodAdapter(ArrayList<Dish> list) {
            super(getActivity(), 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_food, null);
            }

            Dish dish = getItem(position);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.food_list_item_imageView);
            imageView.setImageResource(R.drawable.no_image);
            mDownloader.newMessage(imageView, dish.getUrl());
            TextView textViewName = (TextView)convertView.findViewById(R.id.food_list_item_name_textView);
            textViewName.setText(dish.getName());
            TextView textViewWeight = (TextView)convertView.findViewById(R.id.food_list_item_weight_textView);
            textViewWeight.setText("Вес:" + dish.getWeight());
            TextView textViewPrice = (TextView)convertView.findViewById(R.id.food_list_item_price_textView);
            textViewPrice.setText("Цена:" + dish.getPrice());

            return convertView;
        }
    }
}

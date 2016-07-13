package com.ivanfrankov.android.farfor;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class AboutDishFragment extends Fragment {

    public static final String EXTRA_BITMAP = "bitmap";
    public static final String EXTRA_UUID = "position";
    public static final String EXTRA_NAME = "name";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getArguments().getString(EXTRA_NAME));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_dish, container, false);

        byte[] buffer = getArguments().getByteArray(EXTRA_BITMAP);
        UUID position = (UUID)getArguments().getSerializable(EXTRA_UUID);

        Dish dish = DishLab.get().getDish(position);

        ImageView imageView = (ImageView)v.findViewById(R.id.dish_dialog_imageView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        imageView.setImageBitmap(bitmap);
        TextView textViewName = (TextView)v.findViewById(R.id.dish_dialog_name_textView);
        textViewName.setText(dish.getName());
        TextView textViewPrice = (TextView)v.findViewById(R.id.dish_dialog_price_textView);
        textViewPrice.setText("Цена:" + dish.getPrice());
        TextView textViewDescription = (TextView)v.findViewById(R.id.dish_dialog_description_textView);
        textViewDescription.setText("Описание:" + dish.getDescription());
        TextView textViewWeight = (TextView)v.findViewById(R.id.dish_dialog_weight_textView);
        textViewWeight.setText("Вес:" + dish.getWeight());

        return v;
    }

    public static AboutDishFragment newInstance(byte[] bitmap, UUID uuid, String name) {
        Bundle args = new Bundle();
        args.putByteArray(EXTRA_BITMAP, bitmap);
        args.putSerializable(EXTRA_UUID, uuid);
        args.putString(EXTRA_NAME, name);
        AboutDishFragment fragment = new AboutDishFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

package com.ivanfrankov.android.farfor;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class AboutDishDialog extends DialogFragment {

    public static final String EXTRA_BITMAP = "bitmap";
    public static final String EXTRA_UUID = "position";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        byte[] buffer = getArguments().getByteArray(EXTRA_BITMAP);
        UUID position = (UUID)getArguments().getSerializable(EXTRA_UUID);

        Dish dish = DishLab.get().getDish(position);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_dish, null);

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
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public static AboutDishDialog newInstance(byte[] bitmap, UUID uuid) {
        Bundle args = new Bundle();
        args.putByteArray(EXTRA_BITMAP, bitmap);
        args.putSerializable(EXTRA_UUID, uuid);
        AboutDishDialog dialog = new AboutDishDialog();
        dialog.setArguments(args);
        return dialog;
    }
}

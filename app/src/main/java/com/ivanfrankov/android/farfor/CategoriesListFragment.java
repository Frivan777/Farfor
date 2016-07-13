package com.ivanfrankov.android.farfor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesListFragment extends ListFragment{

    private ArrayList<CategoryFood> mCategories;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategories = CategoryLab.get().getCategories();

        setListAdapter(new CategoryAdapter(mCategories));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        int idCategory = ((CategoryAdapter)getListAdapter()).getItem(position).getId();
        Intent i = new Intent(getActivity(), ListFoodActivity.class);
        i.putExtra(ListFoodFragment.EXTRA_ID, idCategory);
        startActivity(i);
    }

    private class CategoryAdapter extends ArrayAdapter<CategoryFood> {

        public CategoryAdapter(ArrayList<CategoryFood> list) {
            super(getActivity(), 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_category, null);
            }

            CategoryFood category = getItem(position);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.category_list_item_imageView);
            imageView.setImageResource(category.getRscImg());
            TextView textView = (TextView)convertView.findViewById(R.id.category_list_item_textView);
            textView.setText(category.getName());
            return convertView;

        }

    }
}

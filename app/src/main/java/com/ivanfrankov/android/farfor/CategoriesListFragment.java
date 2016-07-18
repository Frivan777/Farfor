package com.ivanfrankov.android.farfor;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoriesListFragment extends Fragment{


    private RecyclerView mCategoryRecyclerView;
    private CategoryAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_list, container, false);

        mCategoryRecyclerView = (RecyclerView) v.findViewById(R.id.category_recycler_view);;
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        List<CategoryFood> categories = CategoryLab.get().getCategories();
        mAdapter = new CategoryAdapter(categories);
        mCategoryRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        TextView mTextView;
        private CategoryFood mCategoryFood;

        public CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView)itemView.findViewById(R.id.category_list_item_imageView);
            mTextView = (TextView)itemView.findViewById(R.id.category_list_item_textView);
        }

        public void bindCategory(CategoryFood category) {
            mCategoryFood = category;
            mImageView.setImageResource(category.getRscImg());
            mTextView.setText(category.getName());
        }

        @Override
        public void onClick(View v) {
            int idCategory = mCategoryFood.getId();

            Intent i = ListFoodActivity.newIntent(getActivity(), idCategory);

            startActivity(i);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<CategoryFood> mCategories;

        public CategoryAdapter(List<CategoryFood> categories) {
            mCategories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_category, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            CategoryFood category = mCategories.get(position);
            holder.bindCategory(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }
    }
}

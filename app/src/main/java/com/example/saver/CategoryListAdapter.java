package com.example.saver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.saver.model.Category;

import java.util.LinkedList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private final LinkedList<Category> categoryList;

    private LayoutInflater inflater;

    public CategoryListAdapter(Context context, LinkedList<Category> categoryList) {
        this.inflater = LayoutInflater.from(context);
        this.categoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.categorylist_item, parent, false);
        return new CategoryViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category current = categoryList.get(position);
        holder.categoryTotal_textView.setText("$" + current.total().toString());
        holder.categoryName_textView.setText(current.getName());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        public final TextView categoryName_textView;
        public final TextView categoryTotal_textView;

        final CategoryListAdapter adapter;

        public  CategoryViewHolder(View itemView, CategoryListAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            categoryName_textView = itemView.findViewById(R.id.categoryName_textView);
            categoryTotal_textView = itemView.findViewById(R.id.categoryTotal_textView);
        }
    }
}

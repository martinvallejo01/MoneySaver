package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saver.model.Category;
import com.example.saver.model.Expense;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final int TEXT_REQUEST = 1;

    private LinkedList<Category> categoryList;
    private RecyclerView category_recyclerView;
    private CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryList = new LinkedList<>();

        createRandomData();
        category_recyclerView = findViewById(R.id.category_recyclerView);
        categoryListAdapter = new CategoryListAdapter(this, categoryList);
        category_recyclerView.setAdapter(categoryListAdapter);
        category_recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply = data.getStringExtra(CategoryActivity.EXTRA_CATEGORY_REPLY);
                Log.d(LOG_TAG, reply);
            }
        }
    }

    public void onAddNewCategoryTap(View view) {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }

    private void createRandomData() {
        categoryList.add(new Category("Food", 30.0));
        categoryList.add(new Category("Studies", 25.0));
        Expense e1 = new Expense("Museo cantoverde", 55.25);
        Expense e2 = new Expense("Viaje a Toronto", 536.89);
        Category category = new Category("Turism", 600.0);
        category.addExpense(e1);
        category.addExpense(e2);
        categoryList.add(category);
    }

    public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
        public static final String EXTRA_CATEGORY = "com.example.saver.extra.CATEGORY";

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
            holder.categoryTotal_textView.setText(String.format("$%s", current.total().toString()));
            holder.categoryName_textView.setText(current.getName());
            holder.categoryBound_textView.setText(String.format("Bound: $%s", current.getBound().toString()));

        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final TextView categoryName_textView, categoryBound_textView, categoryTotal_textView;

            final CategoryListAdapter adapter;

            public  CategoryViewHolder(View itemView, CategoryListAdapter adapter) {
                super(itemView);
                this.adapter = adapter;
                categoryName_textView = itemView.findViewById(R.id.categoryName_textView);
                categoryTotal_textView = itemView.findViewById(R.id.categoryTotal_textView);
                categoryBound_textView = itemView.findViewById(R.id.categoryBound_textView);
                itemView.setOnClickListener(this::onClick);
            }

            @Override
            public void onClick(View v) {
                Category category = categoryList.get(getLayoutPosition());
                Gson gson = new Gson();
                String data = gson.toJson(category, Category.class);
                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                intent.putExtra(EXTRA_CATEGORY, data);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        }
    }

}

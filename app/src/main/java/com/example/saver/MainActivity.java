package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private static final String PREFERENCE_KEY = "com.example.saver.MainActivity.KEY";
    private static final String sharedPrefFile = "com.example.saver.SHARED_PREFS";
    public static final int TEXT_REQUEST = 1;
    public static final int NAME_AND_BOUND_REQUEST = 2;

    private SharedPreferences preferences;

    private Gson gson = new Gson();

    private LinkedList<Category> categoryList;
    private RecyclerView category_recyclerView;
    private CategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // createRandomData();
        category_recyclerView = findViewById(R.id.category_recyclerView);
        categoryListAdapter = new CategoryListAdapter(this);
        category_recyclerView.setAdapter(categoryListAdapter);
        category_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    @Override
    protected void onPause() {
        saveChanges();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TEXT_REQUEST) {
                String reply = data.getStringExtra(CategoryActivity.EXTRA_CATEGORY_REPLY);
                int index = data.getIntExtra(CategoryActivity.EXTRA_CATEGORY_INDEX_REPLY, -1);
                Category updatedCategory = gson.fromJson(reply, Category.class);
                categoryList.remove(index);
                categoryList.add(index, updatedCategory);
                category_recyclerView.getAdapter().notifyItemChanged(index);
            }
            if (requestCode == NAME_AND_BOUND_REQUEST) {
                String name = data.getStringExtra(CreateCategoryActivity.EXTRA_NAME_REPLY);
                Double bound = data.getDoubleExtra(CreateCategoryActivity.EXTRA_BOUND_REPLY, 0.0);
                categoryList.add(new Category(name, bound));
                category_recyclerView.getAdapter().notifyDataSetChanged();
            }
            saveChanges();
        }
    }

    public void onAddNewCategoryTap(View view) {
        Intent intent = new Intent(this, CreateCategoryActivity.class);
        startActivityForResult(intent, NAME_AND_BOUND_REQUEST);
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

    private void saveChanges() {
        String dataToSave = gson.toJson(categoryList.toArray(), Category[].class);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString(PREFERENCE_KEY, dataToSave);
        preferenceEditor.apply();
    }

    private void loadData() {
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String dataToLoad = preferences.getString(PREFERENCE_KEY, null);
        categoryList = new LinkedList<>();
        if (dataToLoad != null) {
            Category[] categories = gson.fromJson(dataToLoad, Category[].class);
            for (Category c : categories)
                categoryList.add(c);
        }
        else
            categoryList = new LinkedList<>();
    }

    public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
        public static final String EXTRA_CATEGORY = "com.example.saver.extra.CATEGORY";
        public static final String EXTRA_CATEGORY_INDEX = "com.example.saver.extra.CATEGORY_INDEX";

        private LayoutInflater inflater;

        public CategoryListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
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
                int index = getLayoutPosition();
                Category category = categoryList.get(index);
                Gson gson = new Gson();
                String data = gson.toJson(category, Category.class);
                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                intent.putExtra(EXTRA_CATEGORY, data);
                intent.putExtra(EXTRA_CATEGORY_INDEX, index);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        }
    }

}

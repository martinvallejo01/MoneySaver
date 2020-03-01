package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.saver.model.Category;
import com.example.saver.model.Expense;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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
}

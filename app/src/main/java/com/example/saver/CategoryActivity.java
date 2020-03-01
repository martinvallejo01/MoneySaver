package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.saver.model.Category;
import com.example.saver.model.Expense;
import com.google.gson.Gson;

import java.util.Calendar;

public class CategoryActivity extends AppCompatActivity {
    public static final String LOG_TAG = CategoryActivity.class.getSimpleName();
    private Category category;

    private ExpenseListAdapter expenseListAdapter;

    private TextView datePicker_textView;
    private EditText description_editText, amount_editText;
    private Button delete_button;
    private RecyclerView expenses_recyclerView;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        loadData();
        setTitle("Category " + category.getName());

        datePicker_textView = findViewById(R.id.datePicker_textView);
        dateSetListener = this::onDaySet;

        expenses_recyclerView = findViewById(R.id.expenses_recyclerView);
        expenseListAdapter = new ExpenseListAdapter(this, category.getExpenseList());
        expenses_recyclerView.setAdapter(expenseListAdapter);
        expenses_recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClearTap(View view) {
        /* TODO */
    }

    public void onDeleteTap(View view) {
        /* TODO */
    }

    public void onSubmitTap(View view) {
        /* TODO */
    }

    @Override
    public void onBackPressed() {
        /* TODO */
        super.onBackPressed();
        finish();
    }

    private void loadData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra(CategoryListAdapter.EXTRA_CATEGORY);
        Log.d(LOG_TAG, data);
        Gson gson = new Gson();
        category = gson.fromJson(data, Category.class);
        Log.d(LOG_TAG, category.toString());
    }

    public void onDatePickerTap(View view) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, android.R.style.Theme_Material_Light_Dialog,
                dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void onDaySet(DatePicker view, int year, int month, int day) {
        String date = year + "/" + (month + 1) + "/" + day;
        datePicker_textView.setText(date);
    }
}

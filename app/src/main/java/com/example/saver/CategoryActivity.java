package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.saver.model.Category;
import com.example.saver.model.Expense;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.Calendar;


public class CategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_REPLY = "com.example.saver.extra.EXTRA_CATEGORY_REPLY";
    public static final String LOG_TAG = CategoryActivity.class.getSimpleName();
    private Gson gson = new Gson();
    private Category category;

    private ExpenseListAdapter expenseListAdapter;

    private TextView datePicker_textView;
    private EditText description_editText, amount_editText;
    private Button delete_button;
    private RecyclerView expenses_recyclerView;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private int indexBeingEdited = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        loadData();
        setTitle("Category " + category.getName());

        description_editText = findViewById(R.id.description_editText);
        amount_editText = findViewById(R.id.amount_editText);
        delete_button = findViewById(R.id.delete_button);

        datePicker_textView = findViewById(R.id.datePicker_textView);
        dateSetListener = this::onDaySet;

        expenses_recyclerView = findViewById(R.id.expenses_recyclerView);
        expenseListAdapter = new ExpenseListAdapter(this);
        expenses_recyclerView.setAdapter(expenseListAdapter);
        expenses_recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClearTap(View view) {
        datePicker_textView.setText(R.string.pick_date);
        description_editText.getText().clear();
        amount_editText.getText().clear();
        indexBeingEdited = -1;
        delete_button.setEnabled(false);
    }

    public void onDeleteTap(View view) {
        String deletedExpenseName = category.getExpenseList().get(indexBeingEdited).getDescription();
        category.getExpenseList().remove(indexBeingEdited);
        expenses_recyclerView.getAdapter().notifyDataSetChanged();
        onClearTap(view);
        Snackbar.make(view, "Bye bye " + deletedExpenseName, Snackbar.LENGTH_LONG).show();
    }

    public void onSubmitTap(View view) {
        String newExpenseDescription = description_editText.getText().toString();
        Double newExpenseAmount = Double.parseDouble(amount_editText.getText().toString());
        String[] date = toString().split("-");
        LocalDate newExpenseDate = LocalDate.now();
        Expense newExpense = new Expense(newExpenseDate, newExpenseDescription, newExpenseAmount);
        if (date.length == 3) {
            newExpenseDate = LocalDate.of(
                    Integer.parseInt(date[0]),
                    Integer.parseInt(date[1] + 1),
                    Integer.parseInt(date[2])
            );
        }
        if (indexBeingEdited == -1) {
            if (!category.addExpense(newExpense)) {
                Snackbar.make(view, "Amount > Limit", Snackbar.LENGTH_LONG).show();
            }
            else {
                onClearTap(view);
                expenses_recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
        else {
            if (!category.replaceExpense(newExpense, indexBeingEdited)) {
                Snackbar.make(view, "Amount > Limit", Snackbar.LENGTH_LONG).show();
            }
            else {
                onClearTap(view);
                expenses_recyclerView.getAdapter().notifyDataSetChanged();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        String reply = gson.toJson(category);
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_CATEGORY_REPLY, reply);
        setResult(RESULT_OK, replyIntent);
        Log.d(LOG_TAG, reply);
        super.onBackPressed();
        finish();
    }

    private void loadData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra(MainActivity.CategoryListAdapter.EXTRA_CATEGORY);
        category = gson.fromJson(data, Category.class);
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
        String date = year + "-" + (month + 1) + "-" + day;
        datePicker_textView.setText(date);
    }

    public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
        private LayoutInflater inflater;

        public ExpenseListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.expenselist_item, parent, false);
            return new ExpenseViewHolder(itemView, this);
        }

        @Override
        public void onBindViewHolder(ExpenseViewHolder holder, int position) {
            Expense current = category.getExpenseList().get(position);
            holder.amount_textView.setText(String.format("$%s", current.getAmount().toString()));
            holder.date_textView.setText(current.getDate().toString());
            holder.description_textView.setText(current.getDescription());
        }

        @Override
        public int getItemCount() {
            return category.getExpenseList().size();
        }

        class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView description_textView, date_textView, amount_textView;
            final ExpenseListAdapter adapter;

            public ExpenseViewHolder(View itemView, ExpenseListAdapter adapter) {
                super(itemView);
                description_textView = itemView.findViewById(R.id.descriptionList_textView);
                date_textView = itemView.findViewById(R.id.dateList_textView);
                amount_textView = itemView.findViewById(R.id.amountList_textView);
                this.adapter = adapter;
                itemView.setOnClickListener(this::onClick);
            }

            @Override
            public void onClick(View v) {
                indexBeingEdited = getLayoutPosition();
                Expense selected = category.getExpenseList().get(indexBeingEdited);
                datePicker_textView.setText(selected.getDate().toString());
                description_editText.setText(selected.getDescription());
                amount_editText.setText(selected.getAmount().toString());
                delete_button.setEnabled(true);
            }
        }
    }
}

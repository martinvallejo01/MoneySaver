package com.example.saver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.saver.model.Expense;

import java.util.LinkedList;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>  {
    private final LinkedList<Expense> expenseList;

    private LayoutInflater inflater;

    public ExpenseListAdapter(Context context, LinkedList<Expense> expenseList) {
        inflater = LayoutInflater.from(context);
        this.expenseList = expenseList;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.expenselist_item, parent, false);
        return new ExpenseViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense current = expenseList.get(position);
        holder.amount_textView.setText(String.format("$%s", current.getAmount().toString()));
        holder.date_textView.setText(current.getDate().toString());
        holder.description_textView.setText(current.getDescription());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView description_textView, date_textView, amount_textView;
        final ExpenseListAdapter adapter;

        public ExpenseViewHolder(View itemView, ExpenseListAdapter adapter) {
            super(itemView);
            description_textView = itemView.findViewById(R.id.descriptionList_textView);
            date_textView = itemView.findViewById(R.id.dateList_textView);
            amount_textView = itemView.findViewById(R.id.amountList_textView);
            this.adapter = adapter;
        }
    }
}

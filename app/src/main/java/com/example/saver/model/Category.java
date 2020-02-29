package com.example.saver.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

public class Category {
    private String name;
    private Double bound;
    private LinkedList<Expense> expenseList;

    public Category(String name, Double bound, LinkedList<Expense> expenseList) {
        this.name = name;
        this.bound = bound;
        this.expenseList = expenseList;
        Collections.sort(this.expenseList);
    }

    public Category(String name, Double bound) {
        this.name = name;
        this.bound = bound;
        expenseList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBound() {
        return bound;
    }

    public void setBound(Double bound) {
        this.bound = bound;
    }

    public LinkedList<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(LinkedList<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) &&
                Objects.equals(bound, category.bound) &&
                Objects.equals(expenseList, category.expenseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, bound, expenseList);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", bound=" + bound +
                ", expenseList=" + expenseList +
                '}';
    }

    Boolean addExpense(Expense e) {
        if (e.getAmount() > bound) {
            return false;
        }
        expenseList.add(e);
        Collections.sort(expenseList);
        return true;
    }
}

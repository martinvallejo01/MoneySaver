package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateCategoryActivity extends AppCompatActivity {
    public final static String
            EXTRA_NAME_REPLY = "com.example.saver.NAME_REPLY",
            EXTRA_BOUND_REPLY = "com.example.saver.BOUND_REPLY",
            EXTRA_DELETED_REPLY = "com.example.saver.DELETED_REPLY",
            EXTRA_isDELETABLE_REPLY = "com.example.saver.isDELETABLE_REPLY",
            EXTRA_INDEX_REPLY = "com.example.saver.INDEX_REPLY";


    EditText name_editText, bound_editText;
    Boolean isDeletable;
    String categoryName;
    Double categoryBound;
    int indexBeingEdited;
    Intent replyIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        name_editText = findViewById(R.id.categoryName_editText);
        bound_editText = findViewById(R.id.categoryBound_editText);
        Intent intent = getIntent();
        indexBeingEdited = intent.getIntExtra(MainActivity.CategoryListAdapter.EXTRA_CATEGORY_INDEX, -1);
        if (indexBeingEdited != -1) {
            categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
            categoryBound = intent.getDoubleExtra(MainActivity.EXTRA_CATEGORY_BOUND, -1);
            name_editText.setText(categoryName);
            bound_editText.setText(categoryBound.toString());
            isDeletable = true;
        } else {
            isDeletable = false;
        }
        replyIntent.putExtra(EXTRA_isDELETABLE_REPLY, isDeletable);
        replyIntent.putExtra(EXTRA_INDEX_REPLY, indexBeingEdited);
    }

    public void onDeleteButton(View view) {
        replyIntent.putExtra(EXTRA_DELETED_REPLY, true);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void onSubmitButtonTapped(View view) {
        String replyName = name_editText.getText().toString();
        Double replyBound = Double.parseDouble(bound_editText.getText().toString());
        replyIntent.putExtra(EXTRA_NAME_REPLY, replyName);
        replyIntent.putExtra(EXTRA_BOUND_REPLY, replyBound);
        replyIntent.putExtra(EXTRA_DELETED_REPLY, false);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}

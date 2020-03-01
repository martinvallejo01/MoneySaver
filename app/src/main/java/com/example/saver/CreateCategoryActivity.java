package com.example.saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateCategoryActivity extends AppCompatActivity {
    public final static String
            EXTRA_NAME_REPLY = "com.example.saver.NAME_REPLY",
            EXTRA_BOUND_REPLY = "com.example.saver.BOUND_REPLY";
    EditText name_editText, bound_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        name_editText = findViewById(R.id.categoryName_editText);
        bound_editText = findViewById(R.id.categoryBound_editText);
    }

    public void onSubmitButtonTapped(View view) {
        String replyName = name_editText.getText().toString();
        Double replyBound = Double.parseDouble(bound_editText.getText().toString());
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_NAME_REPLY, replyName);
        replyIntent.putExtra(EXTRA_BOUND_REPLY, replyBound);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}

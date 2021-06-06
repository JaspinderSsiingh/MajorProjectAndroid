package com.n.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {

    String category;
    TextView backTV;
    TextView addTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        category = getIntent().getStringExtra("selected_category");
        init();
    }

    public void init() {
        backTV = findViewById(R.id.backTV);
        addTV = findViewById(R.id.addTV);
        backTV.setOnClickListener(this);
        addTV.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backTV:
                finish();
                break;
            case R.id.addTV:
                startActivity(new Intent(this,AddNotesActivity.class));
                break;
        }
    }
}
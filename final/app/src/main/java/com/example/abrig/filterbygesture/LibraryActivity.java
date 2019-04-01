package com.example.abrig.filterbygesture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ImageButton dog = findViewById(R.id.imageButton2_dog);
        ImageButton canoe = findViewById(R.id.imageButton_canoe);
        ImageButton london = findViewById(R.id.imageButton_london);
        ImageButton ski = findViewById(R.id.imageButton_ski_resort);
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(LibraryActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startTakePicIntent(View view) {
        Intent intent = new Intent(LibraryActivity.this, TakePicActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFilterIntent(View view) {
        Intent intent = new Intent(LibraryActivity.this, FilterActivity.class);
        startActivityForResult(intent, 1);
    }
}

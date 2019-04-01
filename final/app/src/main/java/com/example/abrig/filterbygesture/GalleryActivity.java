package com.example.abrig.filterbygesture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(GalleryActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startTakePicIntent(View view) {
        Intent intent = new Intent(GalleryActivity.this, TakePicActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFilterIntent(View view) {
        Intent intent = new Intent(GalleryActivity.this, FilterActivity.class);
        startActivityForResult(intent, 1);
    }
}

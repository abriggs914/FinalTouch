package com.example.abrig.filterbygesture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    private TextView gestureText;
//    private GestureDetector myGestureDectector;
//    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        gestureText = (TextView) findViewById(R.id.tvGesture);
//        AndroidGestureDectector androidGestureDectector = new AndroidGestureDectector();
//        myGestureDectector = new GestureDetector(MainActivity.this, androidGestureDectector);
    }

    public void startTakePicIntent(View view) {
        Intent intent = new Intent(MainActivity.this,TakePicActivity.class);
        startActivityForResult(intent,1);
    }

    public void startFilterIntent(View view) {
        Intent intent = new Intent(MainActivity.this,FilterActivity.class);
        startActivityForResult(intent,1);
    }

    public void startLibraryIntent(View view) {
        Intent intent = new Intent(MainActivity.this,LibraryActivity.class);
        startActivityForResult(intent,1);
    }
//
//    class AndroidGestureDectector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
//            gestureText.setText("onSingleTapConfirmed");
//            updateNumTouches();
//            Log.d("Gesture ", "onSingleTapConfirmed");
//            return false;
//        }
//
//        @Override
//        public boolean onDoubleTap(MotionEvent motionEvent) {
//            gestureText.setText("onDoubleTap");
//            updateNumTouches();
//            Log.d("Gesture ", "onDoubleTap");
//            return false;
//        }
//
//        @Override
//        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
//            gestureText.setText("onDoubleTapEvent");
//            updateNumTouches();
//            Log.d("Gesture ", "onDoubleTapEvent");
//            return false;
//        }
//
//        @Override
//        public boolean onDown(MotionEvent motionEvent) {
//            gestureText.setText("onDown");
//            count--;
//            updateNumTouches();
//            Log.d("Gesture ", "onDown");
//            return false;
//        }
//
//        @Override
//        public void onShowPress(MotionEvent motionEvent) {
//            String line = "onShowPress";
//            count--;
//            updateNumTouches();
//            Toast.makeText(MainActivity.this, line, Toast.LENGTH_LONG).show();
//            gestureText.setText(line);
//            Log.d("Gesture ", "onShowPress");
//
//        }
//
//        @Override
//        public boolean onSingleTapUp(MotionEvent motionEvent) {
//            gestureText.setText("onSingleTapUp");
//            updateNumTouches();
//            count++;
//            Log.d("Gesture ", "onSingleTapUp");
//            return false;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            gestureText.setText("onScroll");
//            String line = "";
//            count++;
//            updateNumTouches();
//            line = "";
//            if (motionEvent.getX() < motionEvent1.getX()) {
//                line = "GESTURE: Left to Right Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
//                Toast.makeText(MainActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
//            }
//            if (motionEvent.getX() > motionEvent1.getX()) {
//                line = "GESTURE: Right to Left Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
//                Toast.makeText(MainActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
//            }
//            if (motionEvent.getY() < motionEvent1.getY()) {
//                line = "GESTURE: Up to Down Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
//                Toast.makeText(MainActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
//            }
//            if (motionEvent.getY() > motionEvent1.getY()) {
//                line = "GESTURE: Down to Up Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
//                Toast.makeText(MainActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
//            }
//            return false;
//        }
//
//        @Override
//        public void onLongPress(MotionEvent motionEvent) {
//            gestureText.setText("onLongPress");
//            count++;
//            updateNumTouches();
//            Log.d("Gesture ", "onLongPress");
//
//        }
//
//        @Override
//        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            gestureText.setText("onFling");
//            count++;
//            updateNumTouches();
//            if (motionEvent.getX() < motionEvent1.getX()) {
//                Log.d("Gesture ", "Left to Right Fling: " + motionEvent.getX() + " - " + motionEvent1.getX());
//                Log.d("Speed ", String.valueOf(v) + " pixels/second");
//            }
//            if (motionEvent.getX() > motionEvent1.getX()) {
//                Log.d("Gesture ", "Right to Left Fling: " + motionEvent.getX() + " - " + motionEvent1.getX());
//                Log.d("Speed ", String.valueOf(v) + " pixels/second");
//            }
//            if (motionEvent.getY() < motionEvent1.getY()) {
//                Log.d("Gesture ", "Up to Down Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
//                Log.d("Speed ", String.valueOf(v1) + " pixels/second");
//            }
//            if (motionEvent.getY() > motionEvent1.getY()) {
//                Log.d("Gesture ", "Down to Up Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
//                Log.d("Speed ", String.valueOf(v1) + " pixels/second");
//            }
//            return false;
//        }
//    }
//
//    public void updateNumTouches(){
//        TextView fs = findViewById(R.id.fling_stats);
//        String line = "";
//        line += "Count: " + count;
//        fs.setText(line);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
////        count++;
//        myGestureDectector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }
}
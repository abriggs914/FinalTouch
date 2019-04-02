package com.example.abrig.filterbygesture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

import java.util.Random;

public class LibraryActivity extends AppCompatActivity {

    static{
        System.loadLibrary("NativeImageProcessor");
    }

    private TextView gestureText;
    private GestureDetector myGestureDectector;
    private ImageView selectedImage;
    private Bitmap selectedImageBitmap;
    private Bitmap startOverImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        selectedImage = findViewById(R.id.selectedImage);
        LibraryActivity.AndroidGestureDectector androidGestureDectector = new LibraryActivity.AndroidGestureDectector();
        myGestureDectector = new GestureDetector(LibraryActivity.this, androidGestureDectector);
        gestureText = (TextView) findViewById(R.id.tvGesture);

        ImageButton dog = findViewById(R.id.imageButton2_dog);
        ImageButton canoe = findViewById(R.id.imageButton_canoe);
        ImageButton london = findViewById(R.id.imageButton_london);
        ImageButton ski = findViewById(R.id.imageButton_ski_resort);

        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.dog), 640, 640, false);
                selectedImage.setImageBitmap(pic);
                selectedImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
                startOverImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
            }
        });
        canoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.canoe_lake), 640, 640, false);
                selectedImage.setImageBitmap(pic);
                selectedImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
                startOverImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
            }
        });
        london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.london_skyline), 640, 640, false);
                selectedImage.setImageBitmap(pic);
                selectedImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
                startOverImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
            }
        });
        ski.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ski_resort), 640, 640, false);
                selectedImage.setImageBitmap(pic);
                selectedImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
                startOverImageBitmap = pic.copy(Bitmap.Config.ARGB_8888, true);
            }
        });
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

    class AndroidGestureDectector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        public Filter myFilter = new Filter();

        public void processImage(){
            Toast.makeText(LibraryActivity.this, "Processing Image", Toast.LENGTH_SHORT).show();
            Bitmap currPic = myFilter.processFilter(selectedImageBitmap);
            myFilter.clearSubFilters();
            selectedImage.setImageBitmap(currPic);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Log.d("Gesture ", "onSingleTapConfirmed");
            Random rnd = new Random();
            int dep = rnd.nextInt(100);
            float r = rnd.nextFloat();
            float b = rnd.nextFloat();
            float g = rnd.nextFloat();
            myFilter.addSubFilter(new ColorOverlaySubFilter(dep, r, g, b));
            processImage();
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.d("Gesture ", "onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            Log.d("Gesture ", "onDoubleTapEvent");
            gestureText.setText("Applying: Vignette filter");
            myFilter.addSubFilter(new VignetteSubFilter(LibraryActivity.this, 100));
            processImage();
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.d("Gesture ", "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.d("Gesture ", "onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.d("Gesture ", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            if (motionEvent.getX() < motionEvent1.getX()) {
                String line = "GESTURE: Left to Right Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Log.d("onScroll ", line);
            }
            if (motionEvent.getX() > motionEvent1.getX()) {
                String line = "GESTURE: Right to Left Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Log.d("onScroll ", line);
            }
            if (motionEvent.getY() < motionEvent1.getY()) {
                String line = "GESTURE: Up to Down Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Log.d("onScroll ", line);
            }
            if (motionEvent.getY() > motionEvent1.getY()) {
                String line = "GESTURE: Down to Up Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Log.d("onScroll ", line);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.d("Gesture ", "onLongPress");
            //Bitmap currPic = myFilter.processFilter(selectedImageBitmap);
            Toast.makeText(LibraryActivity.this, "RESETTING IMAGE", Toast.LENGTH_LONG).show();
            selectedImageBitmap = startOverImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
            selectedImage.setImageBitmap(startOverImageBitmap);

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            gestureText.setText("onFling");
            float diffX = Math.abs(motionEvent1.getX() - motionEvent.getX());
            float diffY = Math.abs(motionEvent1.getY() - motionEvent.getY());
            if(diffX > diffY) { // R -> L || L -> R
                if (motionEvent.getX() < motionEvent1.getX()) {
                    Log.d("Gesture ", "Left to Right Fling: " + motionEvent.getX() + " - " + motionEvent1.getX());
                    //                tv.setText("Left to Right Fling: og - 1: " + motionEvent.getX() + " - " + motionEvent1.getX());
                    Log.d("Speed ", String.valueOf(v) + " pixels/second");
                    gestureText.setText("Applying: StarLit filter 30");

                    Point[] rgbKnots;
                    rgbKnots = new Point[8];
                    rgbKnots[0] = new Point(0, 0);
                    rgbKnots[1] = new Point(34, 6);
                    rgbKnots[2] = new Point(69, 23);
                    rgbKnots[3] = new Point(100, 58);
                    rgbKnots[4] = new Point(150, 154);
                    rgbKnots[5] = new Point(176, 196);
                    rgbKnots[6] = new Point(207, 233);
                    rgbKnots[7] = new Point(255, 255);
                    myFilter.addSubFilter(new ToneCurveSubFilter(rgbKnots, null, null, null));
                    processImage();
                }
                if (motionEvent.getX() > motionEvent1.getX()) {
                    Log.d("Gesture ", "Right to Left Fling: " + motionEvent.getX() + " - " + motionEvent1.getX());
                    //                tv.setText("Right to Left Fling: og - 1: " + motionEvent.getX() + " - " + motionEvent1.getX());
                    Log.d("Speed ", String.valueOf(v) + " pixels/second");
                    gestureText.setText("Applying: BlueMess filter");

                    Point[] redKnots;
                    redKnots = new Point[8];
                    redKnots[0] = new Point(0, 0);
                    redKnots[1] = new Point(86, 34);
                    redKnots[2] = new Point(117, 41);
                    redKnots[3] = new Point(146, 80);
                    redKnots[4] = new Point(170, 151);
                    redKnots[5] = new Point(200, 214);
                    redKnots[6] = new Point(225, 242);
                    redKnots[7] = new Point(255, 255);

                    myFilter.addSubFilter(new ToneCurveSubFilter(null, redKnots, null, null));
                    myFilter.addSubFilter(new BrightnessSubFilter(30));
                    myFilter.addSubFilter(new ContrastSubFilter(1f));
                    processImage();
                }
            }
            if(diffY > diffX) { // U -> D || D -> U
                if (motionEvent.getY() < motionEvent1.getY()) {
                    Log.d("Gesture ", "Up to Down Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
                    //                tv.setText("Up to Down Fling: og - 1: " + motionEvent.getY() + " - " + motionEvent1.getY());
                    Log.d("Speed ", String.valueOf(v1) + " pixels/second");
                    gestureText.setText("Applying: AweStruckVibe filter");
                    Point[] rgbKnots;
                    Point[] redKnots;
                    Point[] greenKnots;
                    Point[] blueKnots;

                    rgbKnots = new Point[5];
                    rgbKnots[0] = new Point(0, 0);
                    rgbKnots[1] = new Point(80, 43);
                    rgbKnots[2] = new Point(149, 102);
                    rgbKnots[3] = new Point(201, 173);
                    rgbKnots[4] = new Point(255, 255);

                    redKnots = new Point[5];
                    redKnots[0] = new Point(0, 0);
                    redKnots[1] = new Point(125, 147);
                    redKnots[2] = new Point(177, 199);
                    redKnots[3] = new Point(213, 228);
                    redKnots[4] = new Point(255, 255);


                    greenKnots = new Point[6];
                    greenKnots[0] = new Point(0, 0);
                    greenKnots[1] = new Point(57, 76);
                    greenKnots[2] = new Point(103, 130);
                    greenKnots[3] = new Point(167, 192);
                    greenKnots[4] = new Point(211, 229);
                    greenKnots[5] = new Point(255, 255);


                    blueKnots = new Point[7];
                    blueKnots[0] = new Point(0, 0);
                    blueKnots[1] = new Point(38, 62);
                    blueKnots[2] = new Point(75, 112);
                    blueKnots[3] = new Point(116, 158);
                    blueKnots[4] = new Point(171, 204);
                    blueKnots[5] = new Point(212, 233);
                    blueKnots[6] = new Point(255, 255);
                    myFilter.addSubFilter(new ToneCurveSubFilter(rgbKnots, redKnots, greenKnots, blueKnots));

                    processImage();
                }
                if (motionEvent.getY() > motionEvent1.getY()) {
                    Log.d("Gesture ", "Down to Up Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
                    //                tv.setText("Down to Up Fling: og - 1: " + motionEvent.getY() + " - " + motionEvent1.getY());
                    Log.d("Speed ", String.valueOf(v1) + " pixels/second");
                    gestureText.setText("Applying: NightWhisper filter");
                    Point[] rgbKnots;
                    Point[] redKnots;
                    Point[] greenKnots;
                    Point[] blueKnots;

                    rgbKnots = new Point[3];
                    rgbKnots[0] = new Point(0, 0);
                    rgbKnots[1] = new Point(174, 109);
                    rgbKnots[2] = new Point(255, 255);

                    redKnots = new Point[4];
                    redKnots[0] = new Point(0, 0);
                    redKnots[1] = new Point(70, 114);
                    redKnots[2] = new Point(157, 145);
                    redKnots[3] = new Point(255, 255);

                    greenKnots = new Point[3];
                    greenKnots[0] = new Point(0, 0);
                    greenKnots[1] = new Point(109, 138);
                    greenKnots[2] = new Point(255, 255);

                    blueKnots = new Point[3];
                    blueKnots[0] = new Point(0, 0);
                    blueKnots[1] = new Point(113, 152);
                    blueKnots[2] = new Point(255, 255);

                    myFilter.addSubFilter(new ToneCurveSubFilter(rgbKnots, redKnots, greenKnots, blueKnots));
                    processImage();
                }
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(selectedImageBitmap != null) {
            myGestureDectector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}

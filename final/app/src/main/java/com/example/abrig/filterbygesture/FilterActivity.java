package com.example.abrig.filterbygesture;

import android.Manifest;
import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.geometry.Point;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

public class FilterActivity extends AppCompatActivity {

    static{
        System.loadLibrary("NativeImageProcessor");
    }

    private TextView gestureText;
    private GestureDetector myGestureDectector;
    private int count = 0;

    private ImageView selectedImage;
    private Bitmap selectedImageBitmap;
    private Bitmap startOverImageBitmap;
    private String lst = "";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        gestureText = (TextView) findViewById(R.id.tvGesture);
        AndroidGestureDectector androidGestureDectector = new AndroidGestureDectector();
        myGestureDectector = new GestureDetector(FilterActivity.this, androidGestureDectector);
        selectedImage = findViewById(R.id.displayedImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            adjustPicture(targetUri, bitmap, false);
        }
    }

    public void adjustPicture(Uri targetUri, Bitmap pic, boolean isPreset){
        assert targetUri != null;
        Bitmap bitmap = pic;
        try {
            if(!isPreset) {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                Cursor cur = getContentResolver().query(targetUri, orientationColumn, null, null, null);
                int orientation = -1;
                if (cur != null && cur.moveToFirst()) {
                    orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                }
                while (orientation >= 0) {
                    Matrix matrix = new Matrix();
                    if (orientation > 0) {
                        matrix.postRotate(90);
                    }
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    orientation -= 90;
                }
            }
            selectedImage.setImageBitmap(bitmap);
            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            selectedImageBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            startOverImageBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
        catch (FileNotFoundException e) {}
        catch (ArithmeticException e){}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public void startTakePicIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, TakePicActivity.class);
        startActivityForResult(intent, 0);
    }

    public void startGalleryIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, GalleryActivity.class);
        startActivityForResult(intent, 0);
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    public void selectPictureFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public void startLibraryIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, LibraryActivity.class);
        startActivityForResult(intent, 1);
    }

    public void saveEditedPicture(View view) {
        if(selectedImageBitmap == null){
            Toast.makeText(FilterActivity.this, "No picture selected yet", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap finalBitmap = selectedImageBitmap;
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();

        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
// Tell the media scanner about the new file so that it is
// immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        Toast.makeText(FilterActivity.this, "Picture saved successfully",Toast.LENGTH_LONG).show();
    }

    public void startEditingOver(View view) {
        if(startOverImageBitmap != null){
            selectedImageBitmap = startOverImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
            selectedImage.setImageBitmap(selectedImageBitmap);
        }
        else{
            Toast.makeText(FilterActivity.this, "Select a picture first.", Toast.LENGTH_LONG).show();
        }
    }

    class AndroidGestureDectector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        public Filter myFilter = new Filter();

        public void processImage(){
            Toast.makeText(FilterActivity.this, "Processing Image", Toast.LENGTH_SHORT).show();
            Bitmap currPic = myFilter.processFilter(selectedImageBitmap);
            myFilter.clearSubFilters();
            selectedImage.setImageBitmap(currPic);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
//            gestureText.setText("onSingleTapConfirmed");
//            lst += "onSingleTapConfirmed,  ";
            Log.d("Gesture ", "onSingleTapConfirmed");
            Random rnd = new Random();
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
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
//            gestureText.setText("onDoubleTap");
//            lst += "onDoubleTap,  ";
            Log.d("Gesture ", "onDoubleTap");
//            myFilter.addSubFilter(new BrightnessSubFilter(30));
//            myFilter.addSubFilter(new ContrastSubFilter(1.1f));
//            selectedImageBitmap = myFilter.processFilter(selectedImageBitmap);
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
//            gestureText.setText("onDoubleTapEvent");
//            lst += "onDoubleTapEvent,  ";
            Log.d("Gesture ", "onDoubleTapEvent");
            myFilter.addSubFilter(new VignetteSubFilter(FilterActivity.this, 100));
            processImage();
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
//            gestureText.setText("onDown");
//            lst += "onDown,  ";
            Log.d("Gesture ", "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
//            String line = "onShowPress";
//            lst += line + ",  ";
//            Toast.makeText(FilterActivity.this, line, Toast.LENGTH_LONG).show();
//            gestureText.setText(line);
            Log.d("Gesture ", "onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
//            gestureText.setText("onSingleTapUp");
//            lst += "onSingleTapUp,  ";
            Log.d("Gesture ", "onSingleTapUp");
//            myFilter.addSubFilter(new ColorOverlaySubFilter(100, .2f, .2f, .0f));
//            selectedImageBitmap = myFilter.processFilter(selectedImageBitmap);
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//            gestureText.setText("onScroll");
//            String line = "";
////            count++;
//            updateNumTouches();
//            line = "";
            if (motionEvent.getX() < motionEvent1.getX()) {
//                String line = "GESTURE: Left to Right Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
////                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
            }
            if (motionEvent.getX() > motionEvent1.getX()) {
//                String line = "GESTURE: Right to Left Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
////                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
            }
            if (motionEvent.getY() < motionEvent1.getY()) {
//                String line = "GESTURE: Up to Down Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
////                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
            }
            if (motionEvent.getY() > motionEvent1.getY()) {
//                String line = "GESTURE: Down to Up Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
////                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
//                Log.d("onScroll ", line);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
//            gestureText.setText("onLongPress");
//            lst += "onLongPress,  ";
            Log.d("Gesture ", "onLongPress");
            //Bitmap currPic = myFilter.processFilter(selectedImageBitmap);
            Toast.makeText(FilterActivity.this, "RESETTING IMAGE", Toast.LENGTH_LONG).show();
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
//                    myFilter.addSubFilter(new BrightnessSubFilter(30));
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
//                    myFilter.addSubFilter(new ContrastSubFilter(1.2f));

                    processImage();
                }
                if (motionEvent.getY() > motionEvent1.getY()) {
                    Log.d("Gesture ", "Down to Up Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
                    //                tv.setText("Down to Up Fling: og - 1: " + motionEvent.getY() + " - " + motionEvent1.getY());
                    Log.d("Speed ", String.valueOf(v1) + " pixels/second");
                    gestureText.setText("Applying: NightWhisper filter");
//                    myFilter.addSubFilter(new SaturationSubFilter(1.3f));
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
//        count++;
        lst = "";
        if(selectedImageBitmap != null) {
//            Runnable runnable = new Runnable() {
//
////                public MotionEvent event = event;
//
//                @Override
//                public void run() {
//
//
//                }
//            };
//            Thread mythread = new Thread(runnable);
//            mythread.start();
            myGestureDectector.onTouchEvent(event);
        }
//        else{
//            Toast.makeText(FilterActivity.this, "Select a picture first", Toast.LENGTH_LONG).show();
//        }
        return super.onTouchEvent(event);
    }
}
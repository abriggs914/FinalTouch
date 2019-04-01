package com.example.abrig.filterbygesture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TakePicActivity extends AppCompatActivity {

    public ImageButton takePictureButton;
    public ImageView targetImage;
    public Bitmap imageBitmap;
    public Uri contentUri;
    public String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);
        targetImage = findViewById(R.id.takenimage);
        takePictureButton = findViewById(R.id.take_picture_button);

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        contentUri = Uri.fromFile(getOutputMediaFile());
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createPhotoFile();
            if(photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                contentUri = FileProvider.getUriForFile(TakePicActivity.this, "com.example.abrig.filterbygesture", photoFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                startActivityForResult(intent, 100);
            }
        }
    }

    public File createPhotoFile(){
        String name = "finaTouch_";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name + timeStamp, ".jpeg", storageDir);
        }
        catch (Exception e){
            Log.i("createPhotoFile", "File creation failed");
        }
        return image;
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
//                Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG).show();
                Log.i("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.i("CameraDemo", "File created successfully");
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
//                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);//(Bitmap) data.getExtras().get("data");
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            contentUri = Uri.fromFile(getOutputMediaFile());
            Toast.makeText(TakePicActivity.this, "Captured picture successfully",Toast.LENGTH_LONG).show();
//                bitmap = adjustPictureOrientation(getImageUri(TakePicActivity.this, bitmap), bitmap, false);
                targetImage.setImageBitmap(bitmap);
                imageBitmap = bitmap;
            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //showDialog("External storage", context,
                    //        Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff.
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    public void startLibraryIntent(View view) {
        Intent intent = new Intent(TakePicActivity.this, LibraryActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(TakePicActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFilterIntent(View view) {
        Intent intent = new Intent(TakePicActivity.this, FilterActivity.class);
        startActivityForResult(intent, 1);
    }

    public void saveTakenPic(View view) {
        if(imageBitmap == null){
            Toast.makeText(TakePicActivity.this, "Take a picture!", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap finalBitmap = imageBitmap;
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
        Toast.makeText(TakePicActivity.this, "Picture saved successfully",Toast.LENGTH_LONG).show();
    }

    public Bitmap adjustPictureOrientation(Uri targetUri, Bitmap pic, boolean isPreset){
        assert targetUri != null;
        Bitmap bitmap = pic;
//        try {
            if(!isPreset) {
                try {
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                    Cursor cur = getContentResolver().query(targetUri, orientationColumn, null, null, null);
                    int orientation = -1;
                    if (cur != null && cur.moveToFirst()) {
                        orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                    }
//                    orientation = 0;
                    Log.i("adjustPicOrientation", "otientation: " + orientation);
    //                tv.setText("Rotate: " + orientation);
                    while (orientation >= 0) {
                        Log.i("adjustPicOrientation", "otientation in while: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation >= 0) {
                            matrix.postRotate(90);
                        }
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        orientation -= 90;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//        }
//        catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
////                e.printStackTrace();
//
//        }
        return bitmap;
    }
}

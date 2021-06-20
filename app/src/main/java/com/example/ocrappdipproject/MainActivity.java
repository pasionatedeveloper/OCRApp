package com.example.ocrappdipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_REQUEST=200;
    private static final int STORAGE_PERMISSION_REQUEST=400;
    private static final int GALLERY_PERMISSION_REQUEST=600;
    private static final int IMAGE_PICK_CAMERA_CODE=800;


    EditText editText;
    ImageView imageView;

    String cameraPermission[];
    String storagePermission[];

    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText=findViewById(R.id.resultEditText);
        imageView=findViewById(R.id.imageView);


        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.addImage:
                showImageImportDialog();
                break;
            case R.id.settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showImageImportDialog() {

        String[] items = {"Camera","Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){

                    case 0:
                        if (!checkCameraPermission()){
                               requestCameraPermissions();
                        }
                        else{
                            pickCamera();
                        }
                        break;
                    case 1:
                        if (!checkStoragePermissions()){
                            requestStoragePermission();
                        }
                        else {
                            pickGallery();
                        }
                        break;
                }
            }
        });
        dialog.create();
        dialog.show();
    }


    private void pickGallery(){

    }


    private void pickCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image TO Text");
        imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }


    private boolean checkStoragePermissions(){

        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.this,storagePermission,STORAGE_PERMISSION_REQUEST);
    }

    private void requestCameraPermissions(){
        ActivityCompat.requestPermissions(MainActivity.this,cameraPermission,CAMERA_PERMISSION_REQUEST);
    }


    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result&&result1;
    }
}
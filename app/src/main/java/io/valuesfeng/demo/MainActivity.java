package io.valuesfeng.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import io.valuesfeng.picker.MimeType;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.PicassoEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

public class MainActivity extends FragmentActivity {
    public static final int REQUEST_CODE_CHOOSE = 10;
    private static final int CAMERA_PERMISSION = 23;
    private List<Uri> mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                Log.i("picture", u.getPath());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (requestCode == CAMERA_PERMISSION && permissionGranted)
            startImagePickerIntent();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onClickButton(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION);
        }

        startImagePickerIntent();
    }

    public void startImagePickerIntent() {
        Picker.from(this)
                .count(3)
                .enableCamera(true)
                .startCamera(true)
                .setEngine(new PicassoEngine())
                //.setEngine(new GlideEngine())
                //.setEngine(new ImageLoaderEngine())
                //.setEngine(new CustomEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    };

    static class CustomEngine implements LoadEngine {
        @Override
        public void displayImage(String path, ImageView imageView) {
            Log.i("picture", path);
        }

        @Override
        public void displayCameraItem(ImageView imageView) {

        }

        @Override
        public void scrolling(GridView view) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public CustomEngine() {

        }

        protected CustomEngine(Parcel in) {
        }

        public static final Creator<CustomEngine> CREATOR = new Creator<CustomEngine>() {
            public CustomEngine createFromParcel(Parcel source) {
                return new CustomEngine(source);
            }

            public CustomEngine[] newArray(int size) {
                return new CustomEngine[size];
            }
        };
    }
}

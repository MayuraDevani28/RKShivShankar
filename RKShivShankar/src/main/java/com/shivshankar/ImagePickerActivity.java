package com.shivshankar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.shivshankar.cropimage.CropImageActivity;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.ExceptionHandler;
import com.shivshankar.utills.InternalStorageContentProvider;
import com.shivshankar.utills.commonVariables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.shivshankar.utills.commonVariables.REQUEST_CODE_GALLERY;

@SuppressLint("NewApi")
public class ImagePickerActivity extends Activity {
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    File mFileTemp;
    String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    boolean isBrand = true;

    public static void copyStream(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            isStoragePermissionGranted();

            setContentView(R.layout.dialog_gall_cams);
            isBrand = getIntent().getBooleanExtra(commonVariables.KEY_IS_BRAND, true);

            setTitle(null);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lp);
            Button bGallery1 = (Button) findViewById(R.id.bgallary1);
            Button bCamera1 = (Button) findViewById(R.id.bcamera1);
            bGallery1.setOnClickListener(v -> {
                try {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
                    overridePendingTransition(0, 0);
                } catch (Exception e) {

                }
            });
            bCamera1.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri mImageCaptureUri = null;
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        mImageCaptureUri = Uri.fromFile(mFileTemp);
                    } else {

                        mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    intent.putExtra("return-data", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, commonVariables.REQUEST_CODE_TAKE_PICTURE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            });

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
            } else {
                mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, R.anim.slide_bottom);
//            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_bottom);
        }
    }
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_bottom);
//    }

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.hold, R.anim.slide_bottom);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case commonVariables.REQUEST_CODE_GALLERY:

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case commonVariables.REQUEST_CODE_TAKE_PICTURE:
                    startCropImage();
                    break;
                case commonVariables.REQUEST_CODE_CROP_IMAGE:
                    String path = data.getStringExtra("image-path");
                    if (path == null) {
                        return;
                    }
                    Bitmap bitmap1 = BitmapFactory.decodeFile(mFileTemp.getPath());
                    if (bitmap1 != null) {

                        if (isBrand) {
                            Intent intent = new Intent(ImagePickerActivity.this, AddBrandActivitySeller.class);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                            editor.putString("image", saveThis);
                            editor.apply();
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    }
                    break;
            }
        } else
            onBackPressed();
    }

    private void startCropImage() {
        Intent intent = new Intent(ImagePickerActivity.this, CropImageActivity.class);
        intent.putExtra("image-path", mFileTemp.getPath());
        intent.putExtra(CropImageActivity.SCALE, true);
        intent.putExtra(CropImageActivity.ASPECT_X, 0);
        intent.putExtra(CropImageActivity.ASPECT_Y, 0);
        startActivityForResult(intent, commonVariables.REQUEST_CODE_CROP_IMAGE);
    }

}

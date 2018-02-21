package com.kaerenabo.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.kaerenabo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class ImagePicker {

    private Activity activity;
    public static final int REQUEST_IMAGE_CAMERA = 2001;
    public static final int REQUEST_IMAGE_GALLERY = 2002;
    public static final int REQUEST_VIDEO_CAMERA = 2003;
    public static final int REQUEST_VIDEO_GALLERY = 2004;
    public static final int REQUEST_WRITE_STORAGE = 101;
    public ImagePickerCallback imagePickerCallback;
    private String folderName;
    private String fileName;
    private Uri mCapturedImageURI;
    private boolean toCompress;
    public ImagePicker(){

    }

    public ImagePicker(Activity activity, ImagePickerCallback imagePickerCallback){
        this.activity = activity;
        this.imagePickerCallback = imagePickerCallback;
    }

    public void showMediaPickerDialog(String fileName, String folderName, final Activity act){

        if(fileName==null)
            fileName =  System.currentTimeMillis() + ".jpg";
        final String fName = fileName;
        this.fileName = fileName;

        if(folderName == null)
            folderName = "ImagePicker";
        this.folderName = folderName;

        final CharSequence[] items = { "Take Photo", "Photo Gallery","Take Video", "Video Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Media");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0) {
                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fName);
                    mCapturedImageURI = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    act.startActivityForResult(intentPicture, REQUEST_IMAGE_CAMERA);

                } else if (item==1) {
                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    act.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            REQUEST_IMAGE_GALLERY);
                }
                else if(item ==2){
                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }

                    // create new Intentwith with Standard Intent action that can be
                    // sent to have the camera application capture an video and return it.
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    // create a file to save the video
                    mCapturedImageURI = getOutputMediaFileUri();

                    // set the image file name
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                    // set the video image quality to high
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);

                    // start the Video Capture Intent
                    act.startActivityForResult(intent, REQUEST_VIDEO_CAMERA);
                }
                else if(item == 3){
                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("video/*");
                    act.startActivityForResult(i, REQUEST_VIDEO_GALLERY);
                }
                else if (item==4) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void showImagePickerDialog(String fileName, String folderName, final Activity act, boolean toCompress){

        this.toCompress = toCompress;
        if(fileName==null)
            fileName =  System.currentTimeMillis() + ".jpg";
        final String fName = fileName;
        this.fileName = fileName;

        if(folderName == null)
            folderName = "ImagePicker";
        this.folderName = folderName;

        final CharSequence[] items = { act.getString(R.string.label_camera), act.getString(R.string.label_photo_album), act.getString(R.string.label_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0) {
                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fName);
                    mCapturedImageURI = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    act.startActivityForResult(intentPicture, REQUEST_IMAGE_CAMERA);

                } else if (item==1) {
                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    act.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            REQUEST_IMAGE_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void showImagePickerDialog(String fileName, String folderName, final Fragment fragment, boolean toCompress){

        this.toCompress = toCompress;
        if(fileName==null)
            fileName =  System.currentTimeMillis() + ".jpg";
        final String fName = fileName;
        this.fileName = fileName;

        if(folderName == null)
            folderName = "ImagePicker";
        this.folderName = folderName;

        final CharSequence[] items = { fragment.getString(R.string.label_camera), fragment.getString(R.string.label_photo_album), fragment.getString(R.string.label_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0) {

                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(fragment.getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fName);
                    mCapturedImageURI = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    fragment.startActivityForResult(intentPicture, REQUEST_IMAGE_CAMERA);

                } else if (item==1) {

                    boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(fragment.getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                        return;
                    }

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    fragment.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            REQUEST_IMAGE_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case ImagePicker.REQUEST_IMAGE_GALLERY:
                    new ImageCompressionAsyncTask(true, activity, this).execute(data.getDataString());
                    break;
                case ImagePicker.REQUEST_IMAGE_CAMERA:
                    new ImageCompressionAsyncTask(false, activity, this).execute(getRealPathFromURI(mCapturedImageURI));
                    break;

            }
        }

    }


    public String compressImage(String imageUri) {

        if(imageUri == null)
            return null;
        else if(imageUri.equals(""))
            return null;
        String filePath = getRealPathFromStringURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        /*float maxHeight = activity.getResources().getDimension(R.dimen.upload_img_height);
        float maxWidth = activity.getResources().getDimension(R.dimen.upload_img_width);*/
        float imgRatio = 1;
        if(actualHeight!=0)
            imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16*1024];

        try{
            bmp = BitmapFactory.decodeFile(filePath,options);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();

        }
        try{
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        }
        catch(OutOfMemoryError exception){
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float)options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }

    public String getRealPathFromStringURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), folderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"+ fileName);
        return uriSting;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public Bitmap getBitampFromFile(String filePath){
        if(filePath!=null)
            return BitmapFactory.decodeFile(filePath);
        else
            return null;
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(){

        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(){


        File file = new File(Environment.getExternalStorageDirectory().getPath(), folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(file.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");


        return mediaFile;
    }

    public Uri getCaptureMediaUrl(){
        return mCapturedImageURI;
    }
}

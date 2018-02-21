package com.kaerenabo.imagepicker;

import android.app.Activity;
import android.os.AsyncTask;

public class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;
        private Activity activity;
        private ImagePicker imagePicker;
        public ImageCompressionAsyncTask(boolean fromGallery, Activity activity,
                                         ImagePicker imagePicker){
            this.fromGallery = fromGallery;
            this.activity = activity;
            this.imagePicker = imagePicker;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = imagePicker.compressImage(params[0]);
            return filePath;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
                if (fromGallery) {
                    imagePicker.imagePickerCallback.onImagePickerResult(result, ImagePicker.REQUEST_IMAGE_GALLERY);
                } else {
                    imagePicker.imagePickerCallback.onImagePickerResult(result, ImagePicker.REQUEST_IMAGE_CAMERA);
                }
            }

    }
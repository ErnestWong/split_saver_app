package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.noname.splitsaver.Login.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * displays splash screen as buffer to move OCR traineddata files
 *
 * @author E Wong
 */
public class SplashActivity extends Activity {

    public static final String SPLASH_ACTIVITY_TAG = "splash_activity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new OCRInitAsync().execute();
    }

    /**
     * async task required for app preparation (copying OCR files)
     *
     * @author E Wong
     */
    private class OCRInitAsync extends AsyncTask<Void, Void, Void> {

        private static final String TRAINED_DATA_DIRECTORY = "tessdata/";
        private static final String TRAINED_DATA_FILENAME = "eng.traineddata";
        private final String DATA_PATH = Environment
                .getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getPackageName() + "/Files/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // copies tess OCR traineddata file from assets to external storage
        @Override
        protected Void doInBackground(Void... arg) {
            copyTessFileToStorage();
            return null;
        }

        // method called after doInBackground
        @Override
        protected void onPostExecute(Void ready) {
            super.onPostExecute(ready);
            // start main activity
            LoginActivity.startActivity(getApplicationContext());
            finish();
        }

        /**
         * copies traineddata file from assets folder to external storage
         * (destination is DATA_PATH)
         **/
        private void copyTessFileToStorage() {
            try {
                // initializes file and parent directory of file
                File dir = new File(DATA_PATH + TRAINED_DATA_DIRECTORY);
                File file = new File(DATA_PATH + TRAINED_DATA_DIRECTORY
                        + TRAINED_DATA_FILENAME);

                // checks if file already exists
                if (!file.exists()) {
                    // copies file in assets folder to stream
                    InputStream in = mContext.getAssets().open(
                            TRAINED_DATA_DIRECTORY + TRAINED_DATA_FILENAME);

                    // create parent directories
                    if (dir.mkdirs()) {
                        Log.d(SPLASH_ACTIVITY_TAG, "succeeded create directory: " + dir.toString());
                    } else {
                        Log.d(SPLASH_ACTIVITY_TAG, "succeeded create directory: " + dir.toString());
                    }

                    // set outputstream to the destination in external storage
                    // copies inputstream to outputstream
                    byte[] buffer = new byte[1024];
                    FileOutputStream out = new FileOutputStream(file);

                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }

                    out.close();
                    in.close();
                    Log.d("file copied", " tess success");
                }

            } catch (IOException e) {
                Log.d("file error TessOCR", e.toString());
                e.printStackTrace();
            }
        }
    }
}

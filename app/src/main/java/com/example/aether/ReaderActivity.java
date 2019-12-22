package com.example.aether;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.aether.api.RetrofitClient;
import com.example.aether.model.Course;
import com.example.aether.model.Session;
import com.example.aether.model.Slide;
import com.example.aether.model.Slides;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReaderActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    private static final String TAG = "PDFReadingPage";
    private CameraSource cameraSource;
    private TextView mStatus;
    private ImageView mCheckEyes;
    int currentPageNumber = 1;
    Slide slide;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

         slide = (Slide) getIntent().getSerializableExtra("slide");
         course = (Course) getIntent().getSerializableExtra("course");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Toast.makeText(this, "Grant Permission and restart app", Toast.LENGTH_SHORT).show();
        } else {
            createCameraSource();
        }

        mStatus = findViewById(R.id.tv_situation);
        PDFView mPdfView = findViewById(R.id.pdf_reader);
        mCheckEyes = findViewById(R.id.img_check);

        File pdf = loadDocument(course, slide);


        mPdfView.fromFile(pdf)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(ReaderActivity.this, "Re-open this slide", Toast.LENGTH_SHORT).show();
                    }
                })
                .onPageChange(this)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .load();

    }

    public void download(Course course, Slide slide) {
        String fileName = course.getCourseId()+"-"+slide.getTitle()+".pdf";

        new DownloadFile().execute(slide.getUrl(), fileName);
        Toast.makeText(ReaderActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();

    }

    private File loadDocument(Course course, Slide slide) {
        File filePath = Environment.getExternalStorageDirectory();
        File directory = new File (filePath.getAbsolutePath()+"/aether/");
        String fileName = course.getCourseId()+"-"+slide.getTitle()+".pdf";

        if (directory.exists()) {
            File check = new File(directory, fileName);
            if (check.exists()) {
                return check;
            } else {
                download(course, slide);
            }
        } else {
            if (directory.mkdir()) {
                download(course, slide);
                Toast.makeText(this, "Directory Created", Toast.LENGTH_SHORT).show();
            }
        }

        return new File (directory, fileName);
    }

    private void downloadFile (String fileURL, File directory) {
        final int KILOBYTE = 1024;
        try {
            URL url = new URL(fileURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[KILOBYTE];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }

            fileOutputStream.close();

//            Log.v(TAG, "downloadFile() completed ");

        } catch (IOException  e) {
//            Log.e(TAG, "downloadFile() error" + e.getStackTrace());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Log.v(TAG, "doInBackground() Method invoked ");

            String fileUrl = slide.getUrl();
            String fileName = course.getCourseId()+"-"+slide.getTitle()+".pdf";

            File filePath = Environment.getExternalStorageDirectory();
            File directory = new File (filePath.getAbsolutePath()+"/aether/");


            File pdfFile = new File(directory, fileName);

            try {
                if (pdfFile.createNewFile()) {
                    downloadFile(fileUrl, pdfFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
//                Log.e(TAG, "doInBackground() error" + e.getMessage());
            }


            return null;
        }
    }


    // ................................ GOOGLE API .................................. //

    //This class will use google vision api to detect eyes
    private class EyesTracker extends Tracker<Face> {

        private final float THRESHOLD = 0.75f;

        String startTime = null;
        String endTime = null;
        int flag = -99;


        private EyesTracker() {

        }

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {
            if (face.getIsLeftEyeOpenProbability() > THRESHOLD || face.getIsRightEyeOpenProbability() > THRESHOLD) {
                if(startTime == null && flag == -99) {
                    startTime = getCurrentTimeStamp();
                    Log.i(TAG, "Start Time: "+startTime);
                    flag = 0;

                }
                Log.i(TAG, "onUpdate: Eyes Detected");


                showStatus(0);  // "Eyes Detected and open"
            }
            else {
                showStatus(1);  // "Eyes Detected and closed"
            }
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections) {
            super.onMissing(detections);
            if(startTime != null){
                endTime = getCurrentTimeStamp();
                Log.i(TAG, "End Time: "+endTime);
                importReadingSession(currentPageNumber, startTime, endTime);
                flag = -99;
                startTime = null;
                endTime = null;

            }
            showStatus(2);  // "Face Not Detected yet!"
        }

        @Override
        public void onDone() {
            super.onDone();
        }
    }

    private class FaceTrackerFactory implements MultiProcessor.Factory<Face> {

        private FaceTrackerFactory() {

        }

        @Override
        public Tracker<Face> create(Face face) {
            return new EyesTracker();
        }
    }

    public void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerFactory()).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraSource.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraSource != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                cameraSource.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource!=null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource!=null) {
            cameraSource.release();
        }
    }


    // ................................ GOOGLE API .................................. //


    // ................................ PDF Page number and data .................................. //

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        currentPageNumber = page;
        mStatus.setText(String.valueOf(currentPageNumber+" / "+pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    // ................................ PDF Page number and data .................................. //


    // ................................ Statistics .................................. //

    public void showStatus(final int situation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (situation == 0) {
                    // "Eyes Detected and open"
                    mCheckEyes.setImageResource(R.drawable.ic_eye_looking);

                } else if (situation == 1){
                    // "Eyes Detected and closed"
                    mCheckEyes.setImageResource(R.drawable.ic_close_eye);
                } else if (situation == 2) {
                    // "Face Not Detected yet!"
                    mCheckEyes.setImageResource(R.drawable.ic_eye_not_found);

                }

            }
        });
    }


    private void importReadingSession(int page, String startTime, String endTime) {
        // call the api and sent it to the server

        Call<Session> call = RetrofitClient.getInstance()
                .getRetroApi()
                .reading(new Session(slide.getId(), FirebaseAuth.getInstance().getUid(),currentPageNumber, startTime, endTime));

        call.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(@NonNull Call<Session> call, @NonNull Response<Session> response) {

                if(response.body() != null) {
                    Toast.makeText(ReaderActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Session> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : ", Toast.LENGTH_LONG).show();

            }
        });
    }

    private String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}

package zeev.fraiman.cameraandgalery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String currentPhotoPath;
    private String currentVideoPath;

    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> takeVideoLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    private DatabaseHelper dbHelper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private enum PendingAction { NONE, PHOTO, VIDEO }
    private PendingAction pendingAction = PendingAction.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);
        Button btnRecordVideo = findViewById(R.id.btnRecordVideo);
        Button btnViewGallery = findViewById(R.id.btnViewGallery);
        Button btnViewInfo = findViewById(R.id.btnViewInfo);

        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        if (pendingAction == PendingAction.PHOTO) {
                            dispatchTakePictureIntent();
                        } else if (pendingAction == PendingAction.VIDEO) {
                            dispatchTakeVideoIntent();
                        }
                    } else {
                        Toast.makeText(this, R.string.camera_permission_required, Toast.LENGTH_SHORT).show();
                    }
                    pendingAction = PendingAction.NONE;
                });

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && currentPhotoPath != null) {
                        Toast.makeText(this, getString(R.string.photo_saved, currentPhotoPath), Toast.LENGTH_SHORT).show();
                        saveFileInfoToDatabase(currentPhotoPath, "image");
                    }
                });

        takeVideoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && currentVideoPath != null) {
                        Toast.makeText(this, getString(R.string.video_saved, currentVideoPath), Toast.LENGTH_SHORT).show();
                        saveFileInfoToDatabase(currentVideoPath, "video");
                    }
                });

        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndExecute(PendingAction.PHOTO));
        btnRecordVideo.setOnClickListener(v -> checkCameraPermissionAndExecute(PendingAction.VIDEO));
        btnViewGallery.setOnClickListener(v -> openGallery());
        btnViewInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewInfoAboutMyFiles.class);
            startActivity(intent);
        });
    }

    private void checkCameraPermissionAndExecute(PendingAction action) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (action == PendingAction.PHOTO) {
                dispatchTakePictureIntent();
            } else if (action == PendingAction.VIDEO) {
                dispatchTakeVideoIntent();
            }
        } else {
            pendingAction = action;
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e(TAG, "Error creating image file", ex);
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "zeev.fraiman.cameraandgalery.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Log.e(TAG, "Failed to create image file");
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File videoFile = null;
        try {
            videoFile = createVideoFile();
        } catch (IOException ex) {
            Log.e(TAG, "Error creating video file", ex);
        }
        if (videoFile != null) {
            Uri videoURI = FileProvider.getUriForFile(this,
                    "zeev.fraiman.cameraandgalery.fileprovider",
                    videoFile);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
            takeVideoLauncher.launch(takeVideoIntent);
        } else {
            Log.e(TAG, "Failed to create video file");
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                videoFileName,
                ".mp4",
                storageDir
        );

        currentVideoPath = video.getAbsolutePath();
        return video;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW);
            fallbackIntent.setType("*/*");
            fallbackIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            try {
                startActivity(fallbackIntent);
            } catch (Exception ex) {
                Toast.makeText(this, "No gallery app found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveFileInfoToDatabase(String filePath, String type) {
        executorService.execute(() -> {
            File file = new File(filePath);
            String fileName = file.getName();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String uri = Uri.fromFile(file).toString();

            dbHelper.insertFileInfo(fileName, date, time, uri, type);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
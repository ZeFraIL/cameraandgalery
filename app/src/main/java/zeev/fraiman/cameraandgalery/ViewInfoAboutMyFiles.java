package zeev.fraiman.cameraandgalery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewInfoAboutMyFiles extends AppCompatActivity {

    private FileAdapter fileAdapter;
    private final List<FileInfo> fileInfoList = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_info_about_my_files);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(fileInfoList);
        recyclerView.setAdapter(fileAdapter);

        loadFileInfoFromDatabase();
    }

    private void loadFileInfoFromDatabase() {
        executorService.execute(() -> {
            List<FileInfo> tempList = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int nameIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_OF_FILE);
                    int dateIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_OF_FILE);
                    int timeIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_OF_FILE);
                    int uriIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_URI_OF_FILE);
                    int typeIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_FILE);

                    do {
                        String name = nameIdx != -1 ? cursor.getString(nameIdx) : "";
                        String date = dateIdx != -1 ? cursor.getString(dateIdx) : "";
                        String time = timeIdx != -1 ? cursor.getString(timeIdx) : "";
                        String uri = uriIdx != -1 ? cursor.getString(uriIdx) : "";
                        String type = typeIdx != -1 ? cursor.getString(typeIdx) : "";

                        tempList.add(new FileInfo(name, date, time, uri, type));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }

            mainHandler.post(() -> {
                fileInfoList.clear();
                fileInfoList.addAll(tempList);
                fileAdapter.notifyDataSetChanged();
            });
        });
    }

    private class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

        private final List<FileInfo> fileInfoList;

        public FileAdapter(List<FileInfo> fileInfoList) {
            this.fileInfoList = fileInfoList;
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_info, parent, false);
            return new FileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            FileInfo fileInfo = fileInfoList.get(position);
            holder.fileName.setText(fileInfo.getName());

            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(fileInfo.getUri()))
                    .override(130, 130)
                    .placeholder("image".equals(fileInfo.getType()) ? R.drawable.ic_photo : R.drawable.ic_video)
                    .error("image".equals(fileInfo.getType()) ? R.drawable.ic_photo : R.drawable.ic_video)
                    .into(holder.fileImage);

            holder.itemView.setOnClickListener(v -> showFileInfoDialog(fileInfo));
        }

        @Override
        public int getItemCount() {
            return fileInfoList.size();
        }

        public class FileViewHolder extends RecyclerView.ViewHolder {
            ImageView fileImage;
            TextView fileName;

            public FileViewHolder(@NonNull View itemView) {
                super(itemView);
                fileImage = itemView.findViewById(R.id.fileImage);
                fileName = itemView.findViewById(R.id.fileName);
            }
        }
    }

    private void showFileInfoDialog(FileInfo fileInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_title);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, padding);

        VideoView videoView = null;

        if ("image".equals(fileInfo.getType())) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) (200 * getResources().getDisplayMetrics().density)
            );
            imageView.setLayoutParams(lp);
            Glide.with(this).load(Uri.parse(fileInfo.getUri())).into(imageView);
            container.addView(imageView);
        } else if ("video".equals(fileInfo.getType())) {
            videoView = new VideoView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) (200 * getResources().getDisplayMetrics().density)
            );
            videoView.setLayoutParams(lp);
            videoView.setVideoURI(Uri.parse(fileInfo.getUri()));

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            container.addView(videoView);
            videoView.start();
        }

        TextView fileInfoText = new TextView(this);
        fileInfoText.setPadding(0, padding, 0, 0);
        String details = getString(R.string.file_name_format, fileInfo.getName()) + "\n" +
                getString(R.string.date_format, fileInfo.getDate()) + "\n" +
                getString(R.string.time_format, fileInfo.getTime()) + "\n" +
                getString(R.string.uri_format, fileInfo.getUri()) + "\n" +
                getString(R.string.type_format, fileInfo.getType());
        fileInfoText.setText(details);
        container.addView(fileInfoText);

        builder.setView(container);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        VideoView finalVideoView = videoView;
        dialog.setOnDismissListener(d -> {
            if (finalVideoView != null) {
                finalVideoView.stopPlayback();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
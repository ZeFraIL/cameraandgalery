package zeev.fraiman.cameraandgalery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ViewInfoAboutMyFiles extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<FileInfo> fileInfoList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info_about_my_files);

        dbHelper = new DatabaseHelper(this);
        fileInfoList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(fileInfoList);
        recyclerView.setAdapter(fileAdapter);

        loadFileInfoFromDatabase();
    }

    private void loadFileInfoFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_OF_FILE));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_OF_FILE));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_OF_FILE));
                String uri = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_URI_OF_FILE));
                String type = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TYPE_OF_FILE));

                fileInfoList.add(new FileInfo(name, date, time, uri, type));
            } while (cursor.moveToNext());
            cursor.close();
        }

        fileAdapter.notifyDataSetChanged();
    }

    private class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

        private List<FileInfo> fileInfoList;

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

            if (fileInfo.getType().equals("image")) {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(fileInfo.getUri()))
                        .override(130, 130)
                        .into(holder.fileImage);
            } else {
                // Placeholder for video thumbnail
                holder.fileImage.setImageResource(R.drawable.ic_video);
            }

            holder.itemView.setOnClickListener(v -> {
                showFileInfoDialog(fileInfo);
            });
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
        builder.setTitle("Information about file");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        VideoView videoView=null;

        if (fileInfo.getType().equals("image")) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            imageView.setImageURI(Uri.parse(fileInfo.getUri()));
            container.addView(imageView);
        } else if (fileInfo.getType().equals("video")) {
            videoView = new VideoView(this);
            videoView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            videoView.setVideoURI(Uri.parse(fileInfo.getUri()));
            videoView.start();
            container.addView(videoView);
        }

        // Добавляем информацию о файле
        TextView fileInfoText = new TextView(this);
        fileInfoText.setText(
                "File name: " + fileInfo.getName() + "\n" +
                        "Date: " + fileInfo.getDate() + "\n" +
                        "Time: " + fileInfo.getTime() + "\n" +
                        "URI: " + fileInfo.getUri() + "\n" +
                        "Type: " + fileInfo.getType()
        );
        container.addView(fileInfoText);

        builder.setView(container);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        VideoView finalVideoView = videoView;
        dialog.setOnDismissListener(d -> {
            if (fileInfo.getType().equals("video")) {
                finalVideoView.stopPlayback();
            }
        });
    }
}
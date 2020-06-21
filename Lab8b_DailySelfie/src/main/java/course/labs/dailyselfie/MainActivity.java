package course.labs.dailyselfie;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import course.labs.dailyselfie.adapter.PhotoItemsRecyclerAdapter;
import course.labs.dailyselfie.model.PhotoItem;
import course.labs.dailyselfie.view.PhotoViewerFragment;

public class MainActivity extends AppCompatActivity implements PhotoItemsRecyclerAdapter.OnPhotoItemListener {
    private static final String TAG = "MainActivity";

    public static final String CHANNEL_ID = "selfie_notification";
    public static final long INTERVAL_TEN_SECONDS = 10 * 1000;
    public static final long INTERVAL_ONE_MINUTE = 60 * 1000;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Context context = MainActivity.this;

    private List<PhotoItem> photoItems = new ArrayList<>();
    private String currentPhotoPath;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        recyclerView = findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadSavedImageFiles();

        mAdapter = new PhotoItemsRecyclerAdapter(MainActivity.this, photoItems, this);
        recyclerView.setAdapter(mAdapter);

        createNotificationChannel(this);
        initAlarm();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.toolbar_text);
        setSupportActionBar(toolbar);
    }

    private void initAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INTERVAL_TEN_SECONDS,
                INTERVAL_ONE_MINUTE, alarmIntent);
    }

    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "selfie_notification_channel";
            String description = "selfie_notification_channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dispatchTakePictureIntent();
        return true;
    }

    private void dispatchTakePictureIntent() {
        Log.d(TAG, "Taking picture.");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "dispatchTakePictureIntent: " + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "course.labs.dailyselfie.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "Picture captured.");

            Bundle extras = intent.getExtras();
            Log.d(TAG, "Number of extras: " + extras.size());
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Drawable imageDrawable = new BitmapDrawable(getResources(), imageBitmap);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            PhotoItem photoItem = new PhotoItem(imageDrawable, timeStamp);
            photoItems.add(photoItem);

            mAdapter = new PhotoItemsRecyclerAdapter(MainActivity.this, photoItems, this);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onPhotoItemClick(int position) {
        Log.d(TAG, "onPhotoItemClick: Item clicked.");
        //open dialog
        PhotoItem photoItemThatWasClicked = photoItems.get(position);
        DialogFragment dialogFragment = new PhotoViewerFragment(photoItemThatWasClicked);
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName + "_",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void loadSavedImageFiles() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = storageDir.listFiles();

        if (files.length > 0) {
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                PhotoItem photoItem = createPhotoItemFromFilepath(filePath);
                photoItems.add(photoItem);
            }
        }
    }

    public PhotoItem createPhotoItemFromFilepath(String filepath) {
        String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
        filename = filename.substring(0, filename.lastIndexOf("."));
        filename = filename.substring(0, filename.lastIndexOf("_"));

        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        return new PhotoItem(drawable, filename);
    }
}

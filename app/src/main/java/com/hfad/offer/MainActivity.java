package com.hfad.offer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_PERMISSIONS = 1;

    private RecyclerView recyclerView;
    //rename this variable, because you use syntax in project without m prefix
    private ArrayList<Photo> mPhoto = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initViews();
        tryLoadPhotos();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.photosRV);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePhotos() {
        PhotosAdapter photosAdapter = (PhotosAdapter) recyclerView.getAdapter();
        if (photosAdapter == null) {
            photosAdapter = new PhotosAdapter(this::onPhotoClicked);
            int spanCount = 3;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 5;
            }

            recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
            recyclerView.setAdapter(photosAdapter);
        }

        photosAdapter.updatePhotos(mPhoto);
    }

    private void onPhotoClicked(Photo photo, int position) {
        //TODO: Add call of ApiService

        PhotosAdapter adapter = (PhotosAdapter) recyclerView.getAdapter();
        if (adapter != null) {
//            adapter.resetPhotoProgress(position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tryLoadPhotos();
    }

    private void tryLoadPhotos() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);
        } else {
            LoaderManager.getInstance(this).initLoader(0, null, this);
        }
    }

    private Photo convertUriToPhoto(String uri) {
        Photo photo = new Photo();
        photo.setImage(uri);
        return photo;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        return new CursorLoader(this, uri, projection, null,
                null, sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        int columnUriIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
        data.moveToFirst();
        do {
            Photo photo = convertUriToPhoto(data.getString(columnUriIndex));
            mPhoto.add(photo);
        } while (data.moveToNext());

        updatePhotos();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }
}



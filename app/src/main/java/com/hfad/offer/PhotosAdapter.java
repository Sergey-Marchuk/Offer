package com.hfad.offer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private int loadingPhotoPosition = -1;
    private List<Photo> photos;
    private PhotoCallback photoCallback;

    public PhotosAdapter(PhotoCallback photoCallback) {
        this.photoCallback = photoCallback;
    }

    public void updatePhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public void resetPhotoProgress(int position) {
        loadingPhotoPosition = -1;
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosAdapter.ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.bindPhoto(photo);
    }

    private Photo getPhotoByPosition(int position) {
        return photos.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final ProgressBar photoPB;


        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            photoPB = itemView.findViewById(R.id.photoPB);
            itemView.setOnClickListener(this::onPhotoClicked);
        }

        private void onPhotoClicked(View view) {
            photoPB.setVisibility(View.VISIBLE);
            int position = getAdapterPosition();
            loadingPhotoPosition = position;
            notifyItemChanged(position);
            photoCallback.onPhotoClicked(getPhotoByPosition(position), position);
        }

        private void bindPhoto(Photo photo) {
            File file = new File(photo.getImage());
            photoPB.setVisibility(getAdapterPosition() == loadingPhotoPosition ? View.VISIBLE : View.GONE);
            Picasso.get().load(file)
                    .resize(0, 600)
                    .into(imageView);
        }
    }

    public interface PhotoCallback {
        void onPhotoClicked(Photo photo, int position);
    }
}



package com.DatingApp.tinder101.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DatingApp.tinder101.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileImagesAdapter
    extends RecyclerView.Adapter<ProfileImagesAdapter.ImageViewHolder> {
  private List<String> imageUrls;

  public ProfileImagesAdapter(List<String> imageUrls) {
    this.imageUrls = imageUrls;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_images, parent, false);
    return new ImageViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    Picasso.get().load(imageUrls.get(position)).into(holder.profileImage);
  }

  @Override
  public int getItemCount() {
    if (imageUrls != null) {
      return imageUrls.size();
    }
    return 0;
  }

  public final class ImageViewHolder extends RecyclerView.ViewHolder {
    private ImageView profileImage;

    public ImageViewHolder(@NonNull View itemView) {
      super(itemView);
      profileImage = itemView.findViewById(R.id.profileImageHolder);
    }
  }
}

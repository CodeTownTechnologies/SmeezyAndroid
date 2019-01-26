package com.app.smeezy.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.smeezy.R;
import com.app.smeezy.activity.EditPostActivity;
import com.app.smeezy.activity.PostActivity;
import com.app.smeezy.interfacess.EditPostImageClickListener;
import com.app.smeezy.interfacess.PostImageClickListener;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class EditPostImageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_URL = 1;
    private static final int TYPE_URI = 2;

    private Context mContext;
    private ArrayList<String> imageUrlList;
    private ArrayList<Uri> imageUriList;
    private EditPostImageClickListener postImageClickListener;

    public EditPostImageAdapter(Context context, ArrayList<String> imageUrlList, ArrayList<Uri> imageUriList,
                                EditPostImageClickListener postImageClickListener) {

        this.mContext = context;
        this.imageUrlList = imageUrlList;
        this.imageUriList = imageUriList;
        this.postImageClickListener = postImageClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_URL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_image_item, parent, false);
            return new UrlViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_image_item, parent, false);
            return new UriViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof UrlViewHolder) {

            UrlViewHolder viewHolder = (UrlViewHolder) holder;

            if (position < imageUrlList.size()) {

                String imageUrl = imageUrlList.get(position);

                Glide.with(mContext)
                        .load(imageUrl + StaticData.THUMB_300)
                        .asBitmap()
                        .into(viewHolder.img_post);

                viewHolder.img_cross.setVisibility(View.VISIBLE);
                viewHolder.img_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postImageClickListener.onImageUrlRemoveClickListener(position);
                    }
                });
            }

        } else if (holder instanceof UriViewHolder) {

            final UriViewHolder viewHolder = (UriViewHolder) holder;

            final int listPosition = position - imageUrlList.size();

            if (position == imageUriList.size() + imageUrlList.size()) {

                viewHolder.img_cross.setVisibility(View.GONE);
                viewHolder.img_post.setImageResource(R.drawable.add_image);
                viewHolder.img_post.setScaleType(ImageView.ScaleType.FIT_CENTER);
                viewHolder.img_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageUriList.size() + imageUrlList.size() < 4) {
                            viewHolder.img_post.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    postImageClickListener.onAddImageListener(position);
                                }
                            });
                        } else {
                            ((EditPostActivity) mContext).showAlert(mContext.getString(R.string.app_name),
                                    mContext.getString(R.string.max_four_images_allowed));
                        }
                    }
                });

            } else {

                Uri imageUri = imageUriList.get(listPosition);
                viewHolder.img_post.setImageURI(imageUri);
                viewHolder.img_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                viewHolder.img_cross.setVisibility(View.VISIBLE);
                viewHolder.img_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postImageClickListener.onImageUriRemoveClickListener(listPosition);
                    }
                });

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position < imageUrlList.size()) {
            return TYPE_URL;
        } else {
            return TYPE_URI;
        }
    }

    @Override
    public int getItemCount() {
        if (imageUriList.isEmpty() && imageUrlList.isEmpty()) {
            return imageUriList.size() + imageUrlList.size();
        } else {
            return imageUriList.size() + imageUrlList.size() + 1;
        }
    }

    public class UrlViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_post, img_cross;

        public UrlViewHolder(View itemView) {
            super(itemView);

            img_post = (ImageView) itemView.findViewById(R.id.img_post_image_item);
            img_cross = (ImageView) itemView.findViewById(R.id.img_post_image_item_cross);
        }
    }

    public class UriViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_post, img_cross;

        public UriViewHolder(View itemView) {
            super(itemView);

            img_post = (ImageView) itemView.findViewById(R.id.img_post_image_item);
            img_cross = (ImageView) itemView.findViewById(R.id.img_post_image_item_cross);
        }
    }
}

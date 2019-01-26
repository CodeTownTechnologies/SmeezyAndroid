package com.app.smeezy.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.smeezy.R;
import com.app.smeezy.activity.PostActivity;
import com.app.smeezy.interfacess.PostImageClickListener;

import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class PostImageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Uri> imageUriList;
    private PostImageClickListener postImageClickListener;

    public PostImageAdapter(Context context, ArrayList<Uri> imageUriList, PostImageClickListener postImageClickListener) {

        this.mContext = context;
        this.imageUriList = imageUriList;
        this.postImageClickListener = postImageClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_image_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder viewHolder = (ViewHolder) holder;

            if (position < imageUriList.size()) {

                Uri imageUri = imageUriList.get(position);
                viewHolder.img_post.setImageURI(imageUri);
                viewHolder.img_cross.setVisibility(View.VISIBLE);

                viewHolder.img_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postImageClickListener.onImageRemoveClickListener(position);
                    }
                });
            } else if (position == imageUriList.size()) {
                viewHolder.img_cross.setVisibility(View.GONE);
                viewHolder.img_post.setImageResource(R.drawable.add_image);
                viewHolder.img_post.setScaleType(ImageView.ScaleType.FIT_CENTER);
                viewHolder.img_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageUriList.size() < 4) {
                            postImageClickListener.onAddImageListener(position);
                        }else {
                            ((PostActivity) mContext).showAlert(mContext.getString(R.string.app_name),
                                    mContext.getString(R.string.max_four_images_allowed));
                        }
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        if (imageUriList.isEmpty()) {
            return imageUriList.size();
        } else {
            return imageUriList.size() + 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_post, img_cross;

        public ViewHolder(View itemView) {
            super(itemView);

            img_post = (ImageView) itemView.findViewById(R.id.img_post_image_item);
            img_cross = (ImageView) itemView.findViewById(R.id.img_post_image_item_cross);
        }
    }
}

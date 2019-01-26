package com.app.smeezy.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smeezy.R;
import com.app.smeezy.activity.VideoActivity;
import com.app.smeezy.interfacess.HomeFeedListener;
import com.app.smeezy.responsemodels.FeedItem;
import com.app.smeezy.responsemodels.MenuType;
import com.app.smeezy.responsemodels.StuffFeedItem;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Rahul on 03-01-2018.
 */

public class HomeAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_VIDEO_LINK = 3;
    private static final int TYPE_VIDEO = 4;
    private static final int TYPE_LINK = 5;
    private static final int TYPE_LOADING = 6;

    private Context mContext;
    private ArrayList<FeedItem> feedList;
    private HomeFeedListener feedListener;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading = true;
    private boolean noMoreFeedItems;
    private String postPrivacy;

    public HomeAdapter(Context context, RecyclerView recyclerView, ArrayList<FeedItem> feedList,
                       final HomeFeedListener feedListener, String postPrivacy) {

        this.mContext = context;
        this.feedList = feedList;
        this.feedListener = feedListener;
        this.postPrivacy = postPrivacy;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!noMoreFeedItems && !isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                    isLoading = true;
                    if (feedListener != null) {
                        feedListener.onBottomReached();
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loading_item, parent, false);
            return new LoadingHolder(view);
        } else if (viewType == TYPE_IMAGE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_image_item, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == TYPE_VIDEO_LINK) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_video_link_item, parent, false);
            return new VideoLinkViewHolder(view);
        } else if (viewType == TYPE_VIDEO) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_video_item, parent, false);
            return new VideoViewHolder(view);
        } else if (viewType == TYPE_LINK) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_link_item, parent, false);
            return new LinkViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_text_item, parent, false);
            return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ImageViewHolder) {

            final ImageViewHolder imageHolder = (ImageViewHolder) holder;
            final FeedItem feedItem = feedList.get(position);

            imageHolder.tv_name.setText(feedItem.getName());
            imageHolder.tv_date.setText(feedItem.getAddedOn());
            imageHolder.tv_post.setText(feedItem.getDescription());

            if (feedItem.getIsLikes().equals("1")) {
                imageHolder.img_like.setActivated(true);
            } else {
                imageHolder.img_like.setActivated(false);
            }

            if (feedItem.getTotalLikes() == 0) {
                imageHolder.tv_like.setText(mContext.getString(R.string.like));
            } else if (feedItem.getTotalLikes() == 1) {
                imageHolder.tv_like.setText(String.format("%d %s", 1, mContext.getString(R.string.like)));
            } else if (feedItem.getTotalLikes() > 1) {
                imageHolder.tv_like.setText(String.format("%d %s", feedItem.getTotalLikes(), mContext.getString(R.string.likes)));
            }

            if (feedItem.getTotalComments() == 0) {
                imageHolder.tv_comment.setText(mContext.getString(R.string.comment));
            } else if (feedItem.getTotalComments() == 1) {
                imageHolder.tv_comment.setText(String.format("%d %s", 1, mContext.getString(R.string.comment)));
            } else if (feedItem.getTotalComments() > 1) {
                imageHolder.tv_comment.setText(String.format("%d %s", feedItem.getTotalComments(), mContext.getString(R.string.comments)));
            }


            Glide.with(mContext)
                    .load(feedItem.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(imageHolder.img_user);

            HomeListImageViewPagerAdapter viewPagerAdapter = new HomeListImageViewPagerAdapter(mContext, feedItem.getBaseUrl(),
                    feedItem.getImage());
            imageHolder.viewpager.setOffscreenPageLimit(feedItem.getImage().size());
            imageHolder.viewpager.setAdapter(viewPagerAdapter);
            imageHolder.tableLayout.setupWithViewPager(imageHolder.viewpager);
            if (feedItem.getImage().size() == 1){
                imageHolder.tableLayout.setVisibility(View.GONE);
            }

            imageHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedListener.onProfileClick(position, feedItem);
                }
            });

            imageHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedListener.onProfileClick(position, feedItem);


                }
            });

            imageHolder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onLike(position, feedItem.getActivityId(), feedItem.getId());
                }
            });

            imageHolder.img_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            imageHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onComment(position, feedItem);
                }
            });

            if (PreferenceUtils.getId(mContext).equals(feedItem.getUserId())) {
                imageHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu menu = new PopupMenu(mContext, view);
                        menu.getMenuInflater().inflate(R.menu.edit_delete_post_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                if (menuItem.getItemId() == R.id.edit_post) {

                                    feedListener.onEditPost(position, feedItem);

                                } else if (menuItem.getItemId() == R.id.delete_post) {

                                    feedListener.onDeletePost(position, feedItem.getId());

                                }

                                return false;
                            }
                        });

                        menu.show();
                    }
                });
            } else {

                imageHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);

                        final ArrayList<MenuType> menuList = getMenuList(feedItem);

                        listPopupWindow.setWidth(400);

                        FeedOptionAdapter feedOptionAdapter = new FeedOptionAdapter(mContext,
                                menuList);

                        listPopupWindow.setAdapter(feedOptionAdapter);
                        listPopupWindow.setAnchorView(imageHolder.img_more);


                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                                if (listPopupWindow.isShowing()){
                                    listPopupWindow.dismiss();
                                }
                                onOptionClick((int)id, position, feedItem);

                            }
                        });

                        listPopupWindow.show();
                    }
                });

            }


        } else if (holder instanceof VideoViewHolder) {

            final VideoViewHolder videoHolder = (VideoViewHolder) holder;
            final FeedItem feedItem = feedList.get(position);

            videoHolder.tv_name.setText(feedItem.getName());
            videoHolder.tv_date.setText(feedItem.getAddedOn());
            videoHolder.tv_post.setText(feedItem.getDescription());

            if (feedItem.getIsLikes().equals("1")) {
                videoHolder.img_like.setActivated(true);
            } else {
                videoHolder.img_like.setActivated(false);
            }

            if (feedItem.getTotalLikes() == 0) {
                videoHolder.tv_like.setText(mContext.getString(R.string.like));
            } else if (feedItem.getTotalLikes() == 1) {
                videoHolder.tv_like.setText(String.format("%d %s", 1, mContext.getString(R.string.like)));
            } else if (feedItem.getTotalLikes() > 1) {
                videoHolder.tv_like.setText(String.format("%d %s", feedItem.getTotalLikes(), mContext.getString(R.string.likes)));
            }


            if (feedItem.getTotalComments() == 0) {
                videoHolder.tv_comment.setText(mContext.getString(R.string.comment));
            } else if (feedItem.getTotalComments() == 1) {
                videoHolder.tv_comment.setText(String.format("%d %s", 1, mContext.getString(R.string.comment)));
            } else if (feedItem.getTotalComments() > 1) {
                videoHolder.tv_comment.setText(String.format("%d %s", feedItem.getTotalComments(), mContext.getString(R.string.comments)));
            }

            Glide.with(mContext)
                    .load(feedItem.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(videoHolder.img_user);


            videoHolder.img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, VideoActivity.class);
                    intent.putExtra("videoUrl", feedItem.getMedia());
                    mContext.startActivity(intent);
                }
            });

            videoHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onProfileClick(position, feedItem);
                }
            });

            videoHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onProfileClick(position, feedItem);
                }
            });

            videoHolder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onLike(position, feedItem.getActivityId(), feedItem.getId());
                }
            });

            videoHolder.img_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            videoHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onComment(position, feedItem);
                }
            });


            if (PreferenceUtils.getId(mContext).equals(feedItem.getUserId())) {
                videoHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu menu = new PopupMenu(mContext, view);
                        menu.getMenuInflater().inflate(R.menu.edit_delete_post_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                if (menuItem.getItemId() == R.id.edit_post) {

                                    feedListener.onEditPost(position, feedItem);

                                } else if (menuItem.getItemId() == R.id.delete_post) {

                                    feedListener.onDeletePost(position, feedItem.getId());

                                }

                                return false;
                            }
                        });

                        menu.show();
                    }
                });
            } else {

                videoHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);

                        final ArrayList<MenuType> menuList = getMenuList(feedItem);

                        listPopupWindow.setWidth(400);

                        FeedOptionAdapter feedOptionAdapter = new FeedOptionAdapter(mContext,
                                menuList);

                        listPopupWindow.setAdapter(feedOptionAdapter);
                        listPopupWindow.setAnchorView(videoHolder.img_more);


                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                                if (listPopupWindow.isShowing()){
                                    listPopupWindow.dismiss();
                                }
                                onOptionClick((int)id, position, feedItem);

                            }
                        });

                        listPopupWindow.show();
                    }
                });

            }


        } else if (holder instanceof VideoLinkViewHolder) {
            final VideoLinkViewHolder videoLinkHolder = (VideoLinkViewHolder) holder;
            final FeedItem feedItem = feedList.get(position);

            videoLinkHolder.tv_name.setText(feedItem.getName());
            videoLinkHolder.tv_date.setText(feedItem.getAddedOn());
            videoLinkHolder.tv_post.setText(feedItem.getDescription());

            if (feedItem.getIsLikes().equals("1")) {
                videoLinkHolder.img_like.setActivated(true);
            } else {
                videoLinkHolder.img_like.setActivated(false);
            }

            if (feedItem.getTotalLikes() == 0) {
                videoLinkHolder.tv_like.setText(mContext.getString(R.string.like));
            } else if (feedItem.getTotalLikes() == 1) {
                videoLinkHolder.tv_like.setText(String.format("%d %s", 1, mContext.getString(R.string.like)));
            } else if (feedItem.getTotalLikes() > 1) {
                videoLinkHolder.tv_like.setText(String.format("%d %s", feedItem.getTotalLikes(), mContext.getString(R.string.likes)));
            }

            if (feedItem.getTotalComments() == 0) {
                videoLinkHolder.tv_comment.setText(mContext.getString(R.string.comment));
            } else if (feedItem.getTotalComments() == 1) {
                videoLinkHolder.tv_comment.setText(String.format("%d %s", 1, mContext.getString(R.string.comment)));
            } else if (feedItem.getTotalComments() > 1) {
                videoLinkHolder.tv_comment.setText(String.format("%d %s", feedItem.getTotalComments(), mContext.getString(R.string.comments)));
            }


            Glide.with(mContext)
                    .load(feedItem.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(videoLinkHolder.img_user);

            Glide.with(mContext)
                    .load(feedItem.getInfo().getImage() + StaticData.THUMB_300)
                    .asBitmap()
                    .into(videoLinkHolder.img_content);

            videoLinkHolder.img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(feedItem.getMedia()));
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }
            });

            videoLinkHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedListener.onProfileClick(position, feedItem);
                }
            });

            videoLinkHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedListener.onProfileClick(position, feedItem);
                }
            });


            videoLinkHolder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onLike(position, feedItem.getActivityId(), feedItem.getId());
                }
            });

            videoLinkHolder.img_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            videoLinkHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onComment(position, feedItem);
                }
            });


            if (PreferenceUtils.getId(mContext).equals(feedItem.getUserId())) {
                videoLinkHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu menu = new PopupMenu(mContext, view);
                        menu.getMenuInflater().inflate(R.menu.edit_delete_post_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                if (menuItem.getItemId() == R.id.edit_post) {

                                    feedListener.onEditPost(position, feedItem);

                                } else if (menuItem.getItemId() == R.id.delete_post) {

                                    feedListener.onDeletePost(position, feedItem.getId());

                                }

                                return false;
                            }
                        });

                        menu.show();
                    }
                });
            } else {

                videoLinkHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);

                        final ArrayList<MenuType> menuList = getMenuList(feedItem);

                        listPopupWindow.setWidth(400);

                        FeedOptionAdapter feedOptionAdapter = new FeedOptionAdapter(mContext,
                                menuList);

                        listPopupWindow.setAdapter(feedOptionAdapter);
                        listPopupWindow.setAnchorView(videoLinkHolder.img_more);


                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                                if (listPopupWindow.isShowing()){
                                    listPopupWindow.dismiss();
                                }
                                onOptionClick((int)id, position, feedItem);

                            }
                        });

                        listPopupWindow.show();
                    }
                });

            }


        } else if (holder instanceof LinkViewHolder) {
            final LinkViewHolder linkHolder = (LinkViewHolder) holder;
            final FeedItem feedItem = feedList.get(position);

            linkHolder.tv_name.setText(feedItem.getName());
            linkHolder.tv_date.setText(feedItem.getAddedOn());
            linkHolder.tv_post.setText(feedItem.getDescription());

            linkHolder.tv_url.setText(feedItem.getMedia());
            linkHolder.tv_url_desc.setText(feedItem.getInfo().getDescription());

            if (feedItem.getIsLikes().equals("1")) {
                linkHolder.img_like.setActivated(true);
            } else {
                linkHolder.img_like.setActivated(false);
            }

            if (feedItem.getTotalLikes() == 0) {
                linkHolder.tv_like.setText(mContext.getString(R.string.like));
            } else if (feedItem.getTotalLikes() == 1) {
                linkHolder.tv_like.setText(String.format("%d %s", 1, mContext.getString(R.string.like)));
            } else if (feedItem.getTotalLikes() > 1) {
                linkHolder.tv_like.setText(String.format("%d %s", feedItem.getTotalLikes(), mContext.getString(R.string.likes)));
            }

            if (feedItem.getTotalComments() == 0) {
                linkHolder.tv_comment.setText(mContext.getString(R.string.comment));
            } else if (feedItem.getTotalComments() == 1) {
                linkHolder.tv_comment.setText(String.format("%d %s", 1, mContext.getString(R.string.comment)));
            } else if (feedItem.getTotalComments() > 1) {
                linkHolder.tv_comment.setText(String.format("%d %s", feedItem.getTotalComments(), mContext.getString(R.string.comments)));
            }

            Glide.with(mContext)
                    .load(feedItem.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(linkHolder.img_user);

            linkHolder.ll_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(feedItem.getMedia()));
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.no_app_available),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            linkHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    feedListener.onProfileClick(position, feedItem);
                }
            });

            linkHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onProfileClick(position, feedItem);
                }
            });


            linkHolder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onLike(position, feedItem.getActivityId(), feedItem.getId());
                }
            });

            linkHolder.img_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            linkHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onComment(position, feedItem);
                }
            });


            if (PreferenceUtils.getId(mContext).equals(feedItem.getUserId())) {
                linkHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu menu = new PopupMenu(mContext, view);
                        menu.getMenuInflater().inflate(R.menu.edit_delete_post_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                if (menuItem.getItemId() == R.id.edit_post) {

                                    feedListener.onEditPost(position, feedItem);

                                } else if (menuItem.getItemId() == R.id.delete_post) {

                                    feedListener.onDeletePost(position, feedItem.getId());

                                }

                                return false;
                            }
                        });

                        menu.show();
                    }
                });
            } else {

                linkHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);

                        final ArrayList<MenuType> menuList = getMenuList(feedItem);

                        listPopupWindow.setWidth(400);

                        FeedOptionAdapter feedOptionAdapter = new FeedOptionAdapter(mContext,
                                menuList);

                        listPopupWindow.setAdapter(feedOptionAdapter);
                        listPopupWindow.setAnchorView(linkHolder.img_more);


                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                                if (listPopupWindow.isShowing()){
                                    listPopupWindow.dismiss();
                                }
                                onOptionClick((int)id, position, feedItem);

                            }
                        });

                        listPopupWindow.show();
                    }
                });

            }


        } else if (holder instanceof TextViewHolder) {
            final TextViewHolder textHolder = (TextViewHolder) holder;
            final FeedItem feedItem = feedList.get(position);

            textHolder.tv_name.setText(feedItem.getName());
            textHolder.tv_date.setText(feedItem.getAddedOn());
            textHolder.tv_post.setText(feedItem.getDescription());

            if (feedItem.getIsLikes().equals("1")) {
                textHolder.img_like.setActivated(true);
            } else {
                textHolder.img_like.setActivated(false);
            }

            if (feedItem.getTotalLikes() == 0) {
                textHolder.tv_like.setText(mContext.getString(R.string.like));
            } else if (feedItem.getTotalLikes() == 1) {
                textHolder.tv_like.setText(String.format("%d %s", 1, mContext.getString(R.string.like)));
            } else if (feedItem.getTotalLikes() > 1) {
                textHolder.tv_like.setText(String.format("%d %s", feedItem.getTotalLikes(), mContext.getString(R.string.likes)));
            }

            if (feedItem.getTotalComments() == 0) {
                textHolder.tv_comment.setText(mContext.getString(R.string.comment));
            } else if (feedItem.getTotalComments() == 1) {
                textHolder.tv_comment.setText(String.format("%d %s", 1, mContext.getString(R.string.comment)));
            } else if (feedItem.getTotalComments() > 1) {
                textHolder.tv_comment.setText(String.format("%d %s", feedItem.getTotalComments(), mContext.getString(R.string.comments)));
            }

            Glide.with(mContext)
                    .load(feedItem.getProfileImage() + StaticData.THUMB_100)
                    .asBitmap()
                    .placeholder(R.drawable.no_user_blue)
                    .into(textHolder.img_user);

            textHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onProfileClick(position, feedItem);
                }
            });

            textHolder.img_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onProfileClick(position, feedItem);
                }
            });


            textHolder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onLike(position, feedItem.getActivityId(), feedItem.getId());
                }
            });

            textHolder.img_dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            textHolder.img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedListener.onComment(position, feedItem);
                }
            });

            if (PreferenceUtils.getId(mContext).equals(feedItem.getUserId())) {
                textHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu menu = new PopupMenu(mContext, view);
                        menu.getMenuInflater().inflate(R.menu.edit_delete_post_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                if (menuItem.getItemId() == R.id.edit_post) {

                                    feedListener.onEditPost(position, feedItem);

                                } else if (menuItem.getItemId() == R.id.delete_post) {

                                    feedListener.onDeletePost(position, feedItem.getId());

                                }

                                return false;
                            }
                        });

                        menu.show();
                    }
                });
            } else {

                textHolder.img_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);

                        final ArrayList<MenuType> menuList = getMenuList(feedItem);

                        listPopupWindow.setWidth(400);

                        FeedOptionAdapter feedOptionAdapter = new FeedOptionAdapter(mContext,
                                menuList);

                        listPopupWindow.setAdapter(feedOptionAdapter);
                        listPopupWindow.setAnchorView(textHolder.img_more);


                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                                if (listPopupWindow.isShowing()){
                                    listPopupWindow.dismiss();
                                }
                                onOptionClick((int)id, position, feedItem);

                            }
                        });

                        listPopupWindow.show();
                    }
                });

            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (feedList.size() == position && isLoading) {
            return TYPE_LOADING;
        } else {

            FeedItem feedItem = feedList.get(position);

            if (feedItem.getType().equals("text")) {
                return TYPE_TEXT;
            } else if (feedItem.getType().equals("image")) {
                return TYPE_IMAGE;
            } else if (feedItem.getType().equals("video") && feedItem.getSubType().equals("link")) {
                return TYPE_VIDEO_LINK;
            } else if (feedItem.getType().equals("video") && feedItem.getSubType().equals("upload")) {
                return TYPE_VIDEO;
            } else if (feedItem.getType().equals("link")) {
                return TYPE_LINK;
            }
        }

        return TYPE_TEXT;
    }

    @Override
    public int getItemCount() {

        if (isLoading && !noMoreFeedItems) {
            return feedList.size() + 1;
        }

        return feedList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_date, tv_post;
        public ImageView img_user, img_like, img_dislike, img_comment, img_report, img_more;
        public TextView tv_like, tv_dislike, tv_comment, tv_report;
        public ViewPager viewpager;
        public TabLayout tableLayout;

        public ImageViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_home_image_item_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_home_image_item_date);
            img_user = (ImageView) itemView.findViewById(R.id.img_home_image_item_user);
            tv_post = (TextView) itemView.findViewById(R.id.tv_home_image_item_post);
            viewpager = (ViewPager) itemView.findViewById(R.id.home_image_item_viewpager);
            tableLayout = (TabLayout) itemView.findViewById(R.id.home_image_item_tab);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            img_report = (ImageView) itemView.findViewById(R.id.img_report);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_dislike = (TextView) itemView.findViewById(R.id.tv_dislike);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_report = (TextView) itemView.findViewById(R.id.tv_report);

        }
    }

    public class VideoLinkViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_date, tv_post;
        public ImageView img_user, img_content, img_like, img_dislike, img_comment, img_report, img_play, img_more;
        public TextView tv_like, tv_dislike, tv_comment, tv_report;

        public VideoLinkViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_home_video_link_item_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_home_video_link_item_date);
            img_user = (ImageView) itemView.findViewById(R.id.img_home_video_link_item_user);
            tv_post = (TextView) itemView.findViewById(R.id.tv_home_video_link_item_post);
            img_content = (ImageView) itemView.findViewById(R.id.img_home_video_link_item);
            img_play = (ImageView) itemView.findViewById(R.id.img_home_video_link_play);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            img_report = (ImageView) itemView.findViewById(R.id.img_report);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_dislike = (TextView) itemView.findViewById(R.id.tv_dislike);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_report = (TextView) itemView.findViewById(R.id.tv_report);

        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_date, tv_post;
        public ImageView img_user, img_like, img_dislike, img_comment, img_report, img_more,
                img_play, img_content;
        public TextView tv_like, tv_dislike, tv_comment, tv_report;

        public VideoViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_home_video_item_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_home_video_item_date);
            img_user = (ImageView) itemView.findViewById(R.id.img_home_video_item_user);
            tv_post = (TextView) itemView.findViewById(R.id.tv_home_video_item_post);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            img_report = (ImageView) itemView.findViewById(R.id.img_report);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_dislike = (TextView) itemView.findViewById(R.id.tv_dislike);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_report = (TextView) itemView.findViewById(R.id.tv_report);
            img_play = (ImageView) itemView.findViewById(R.id.img_home_video_play);
            img_content = (ImageView) itemView.findViewById(R.id.img_home_video_item);

        }
    }

    public class LinkViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_date, tv_post, tv_url_desc, tv_url;
        public ImageView img_user, img_like, img_dislike, img_comment, img_report, img_link, img_more;
        public TextView tv_like, tv_dislike, tv_comment, tv_report;
        public LinearLayout ll_link;

        public LinkViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_home_link_item_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_home_link_item_date);
            img_user = (ImageView) itemView.findViewById(R.id.img_home_link_item_user);
            ll_link = (LinearLayout) itemView.findViewById(R.id.ll_home_link_item);
            tv_post = (TextView) itemView.findViewById(R.id.tv_home_link_item_post);
            tv_url_desc = (TextView) itemView.findViewById(R.id.tv_home_link_item_url_desc);
            tv_url = (TextView) itemView.findViewById(R.id.tv_home_link_item_url);
            img_link = (ImageView) itemView.findViewById(R.id.img_home_link_item_image);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            img_report = (ImageView) itemView.findViewById(R.id.img_report);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_dislike = (TextView) itemView.findViewById(R.id.tv_dislike);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_report = (TextView) itemView.findViewById(R.id.tv_report);

        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_date, tv_post;
        public ImageView img_user, img_like, img_dislike, img_comment, img_report, img_more;
        public TextView tv_like, tv_dislike, tv_comment, tv_report;

        public TextViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_home_text_item_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_home_text_item_date);
            img_user = (ImageView) itemView.findViewById(R.id.img_home_text_item_user);
            tv_post = (TextView) itemView.findViewById(R.id.tv_home_text_item_post);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            img_report = (ImageView) itemView.findViewById(R.id.img_report);
            img_more = (ImageView) itemView.findViewById(R.id.img_more);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_dislike = (TextView) itemView.findViewById(R.id.tv_dislike);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_report = (TextView) itemView.findViewById(R.id.tv_report);

        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void stopLoader() {
        noMoreFeedItems = true;
    }

    private void onOptionClick(int itemId, int position, FeedItem feedItem) {

        switch (itemId) {

            case StaticData.UNFOLLOW_USER:

                feedListener.onUnfollowUser(position, feedItem);

                break;

            case StaticData.FOLLOW_POST:

                feedListener.onFollowPost(position, feedItem);

                break;

            case StaticData.UNFOLLOW_POST:

                feedListener.onUnfollowPost(position, feedItem);

                break;

            case StaticData.REPORT:

                feedListener.onReportPost(position, feedItem);

                break;

        }

    }

    private ArrayList<MenuType> getMenuList(FeedItem feedItem) {

        ArrayList<MenuType> tempList = new ArrayList<>();

        if (feedItem.getIsFollowPost().equals("1")) {
            tempList.add(new MenuType(StaticData.UNFOLLOW_POST,
                    mContext.getString(R.string.unfollow_post),
                    mContext.getString(R.string.unfollow_post_desc),
                    R.drawable.unfollow_post));
        } else {

            tempList.add(new MenuType(StaticData.FOLLOW_POST,
                    mContext.getString(R.string.follow_post),
                    mContext.getString(R.string.follow_post_desc),
                    R.drawable.follow_post));
        }


        if (!feedItem.getUserType().equals("admin")) {
            tempList.add(new MenuType(StaticData.REPORT,
                    mContext.getString(R.string.give_feedback),
                    mContext.getString(R.string.give_feedback_desc),
                    R.drawable.report_flag));

            tempList.add(new MenuType(StaticData.UNFOLLOW_USER,
                    mContext.getString(R.string.unfollow_user),
                    mContext.getString(R.string.unfollow_user_desc),
                    R.drawable.unfollow_user));
        }



        return tempList;
    }

}

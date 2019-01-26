package com.app.smeezy.utills;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smeezy.BuildConfig;
import com.app.smeezy.R;
import com.app.smeezy.dialogs.CustomActionDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WINDOW_SERVICE;
import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by GAURAV on 9/14/2017.
 */

public class Utills {

    public static void toastshow(Context context) {

        String connection_alert = "No internet connection found <font color='#40a4df'>Please Try Again</font>";
        Toast.makeText(context, Html.fromHtml(context.getResources().getString(R.string.no_internet_connection)),
                Toast.LENGTH_SHORT).show();

    }

    public static void noInternetConnection(final Context context) {

        Bundle bundle = new Bundle();
        bundle.putString("title", context.getResources().getString(R.string.app_name));
        bundle.putString("message", context.getResources().getString(R.string.no_internet_connection));
        bundle.putString("action3", context.getResources().getString(R.string.ok));

        final CustomActionDialog actionDialogFragment = new CustomActionDialog();
        actionDialogFragment.setArguments(bundle);
        actionDialogFragment.setIActionSelectionListener(new CustomActionDialog.IActionSelectionListener() {
            @Override
            public void onActionSelected(int actionType) {

            }
        });
        actionDialogFragment.setCancelable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                actionDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "normalAlert");
            }
        });

    }

    public static String moneyConvert(String text) {

        if (text == null || text.equals("null") || text.isEmpty()) {
            return "";
        }

        text = decimalConvert(text);

        //text = String.valueOf((long) Double.parseDouble(text));

        String result = "";
        try {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "in"));
            symbols.setPatternSeparator(',');
            symbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
           /* String[] strings = text.split("\\.");

            if (strings.length == 1){
                result = decimalFormat.format(Long.valueOf(strings[0]));
            }else {
                result = String.format("%s.%s", decimalFormat.format(Long.valueOf(strings[0])), strings[1]);
            }*/

            result = decimalFormat.format(Double.valueOf(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("$%s", result);
    }

    public static String decimalConvert(String s) {

        if (s == null || s.equals("null") || s.isEmpty()) {
            return "";
        }

        String result;

        double d = Double.parseDouble(s);
        s = String.format(new Locale("en", "in"), "%.2f", d);
        d = Double.parseDouble(s);

        long iPart = (long) d;
        double fPart = d - iPart;

        if (fPart > 0) {
            result = String.format(new Locale("en", "in"), "%.2f", d);
        } else {
            result = String.format(new Locale("en", "in"), "%d", iPart);
        }

        return result;

    }


    public static int getDiffYears(Calendar a, Calendar b) {

        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static void show_alert_for_dial_no(final Context context, final String mobileNo) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_app_dialog);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action3.setVisibility(View.GONE);
        txt_action1.setVisibility(View.VISIBLE);
        txt_action2.setVisibility(View.VISIBLE);
        txt_action1.setText(context.getResources().getString(R.string.yes));
        txt_action2.setText(context.getResources().getString(R.string.no));
        txt_title.setText(context.getResources().getString(R.string.app_name));
        txt_message.setText(context.getResources().getString(R.string.do_you_want_to_dial_no));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(String.format("%s%s", "tel:", mobileNo)));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
        });

        txt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

    }

    public static void show_alert_for_mail(final Context context, final String email) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_app_dialog);

        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.txt_message);
        TextView txt_action1 = (TextView) dialog.findViewById(R.id.txt_action1);
        TextView txt_action2 = (TextView) dialog.findViewById(R.id.txt_action2);
        TextView txt_action3 = (TextView) dialog.findViewById(R.id.txt_action3);
        txt_action3.setVisibility(View.GONE);
        txt_action1.setVisibility(View.VISIBLE);
        txt_action2.setVisibility(View.VISIBLE);
        txt_action1.setText(context.getResources().getString(R.string.yes));
        txt_action2.setText(context.getResources().getString(R.string.no));
        txt_title.setText(context.getResources().getString(R.string.app_name));
        txt_message.setText(context.getResources().getString(R.string.do_you_want_to_mail));

        dialog.show();


        txt_action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
        });

        txt_action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String getDateInMDYFormat(String date) {

        //date = "1993-11-13";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = null;

        try {
            inputDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Date oldFormatDate;
        String formattedDate = null;
        //try {
        //oldFormatDate = sdf.parse(date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = sdf2.format(inputDate);
        /*}catch (ParseException e){
            e.printStackTrace();
        }*/

        return formattedDate;

    }

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static String getDateInYMDFormat(String date) {

        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        //Date oldFormatDate;
        String formattedDate = null;
        //try {
        //oldFormatDate = sdf.parse(date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf2.format(date);
        /*}catch (ParseException e){
            e.printStackTrace();
        }*/

        return formattedDate;

    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return Math.round(dm.widthPixels / dm.density);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void hideSoftKeyboard(Activity activity) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }


    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void showAlert(final Context context, String title, String message) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("action3", context.getResources().getString(R.string.ok));

        final CustomActionDialog actionDialogFragment = new CustomActionDialog();
        actionDialogFragment.setArguments(bundle);
        actionDialogFragment.setIActionSelectionListener(new CustomActionDialog.IActionSelectionListener() {
            @Override
            public void onActionSelected(int actionType) {

            }
        });
        actionDialogFragment.setCancelable(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                actionDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "normalAlert");
            }
        });
    }

    public static Uri getOutputMediaFileUri() {
        File file = getOutputMediaFile();
        if (file != null)
            return Uri.fromFile(file);
        else
            return null;
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), StaticData.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                if (BuildConfig.DEBUG)
                    Log.d(StaticData.IMAGE_DIRECTORY_NAME, "Oops! Failed create " + StaticData.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    public static Uri getOutputMediaVideoFileUri() {
        File file = getOutputMediaVideoFile();
        if (file != null)
            return Uri.fromFile(file);
        else
            return null;
    }

    public static File getOutputMediaVideoFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), StaticData.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                if (BuildConfig.DEBUG)
                    Log.d(StaticData.IMAGE_DIRECTORY_NAME, "Oops! Failed create " + StaticData.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (GalleryUtils.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (GalleryUtils.isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        final Uri contentUri =
                                ContentUris.withAppendedId(
                                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return GalleryUtils.getDataColumn(context, contentUri, null, null);
                    } catch (Exception e) {
                        // Do nothing, try something else
                    }
                }

                /*final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return GalleryUtils.getDataColumn(context, contentUri, null, null);*/
            }
            // MediaProvider
            else if (GalleryUtils.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return GalleryUtils.getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return GalleryUtils.getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(
                    "com.app.smeezy",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent newLinkedInIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.linkedin.android", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("linkedin://" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }


}

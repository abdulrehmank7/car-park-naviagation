package com.arkapp.carparknaviagation.utility;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This is a utility class consisting of Commonly used methods in entire project.
 */
public class ViewUtils {
    private static long LAST_CLICK_TIME;

    public static void printLog(String msg) {
        Log.e("Logzzz", msg);
    }

    public static void toast(@NotNull Context context, @NotNull String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toastShort(@NotNull Context context, @NotNull String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showSnack(@NotNull View parent, @NotNull String msg) {
        Snackbar.make(parent, msg, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(@NotNull View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hide(@NotNull View view) {
        view.setVisibility(View.GONE);
    }

    public static void disableTouch(@NotNull Window window) {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void enableTouch(@NotNull Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void setTransparentEdges(@NotNull Window window) {
        window.getDecorView().setBackgroundResource(android.R.color.transparent);
    }

    public static void setFullWidth(@NotNull Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public static void showAlertDialog(@NotNull Context context, @NotNull String title,
                                       @NotNull String message, @Nullable String positiveTxt,
                                       @NotNull String negativeTxt,
                                       @Nullable OnClickListener positiveListener) {
        MaterialAlertDialogBuilder dialogBuilder =
                (new MaterialAlertDialogBuilder(context))
                        .setTitle(title)
                        .setMessage(message)
                        .setNegativeButton(negativeTxt, (dialog, which) -> dialog.dismiss());

        dialogBuilder.setPositiveButton(positiveTxt, positiveListener);
        dialogBuilder.show();
    }

    @Nullable
    public static Drawable getDrawableRes(@NotNull Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    public static void initVerticalAdapter(@NotNull RecyclerView recyclerView,
                                           @NotNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter,
                                           boolean hasFixedSize) {
        LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setHasFixedSize(hasFixedSize);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setLayoutManager(llm);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @NotNull
    public static String getFormattedDate(@NotNull Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static boolean isDoubleClicked(long minimumClickTimeInMilli) {
        boolean isClicked;
        if (getCurrentTimestamp() - LAST_CLICK_TIME < minimumClickTimeInMilli) {
            isClicked = true;
        } else {
            LAST_CLICK_TIME = getCurrentTimestamp();
            isClicked = false;
        }

        return isClicked;
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static void initGridAdapter(@NotNull RecyclerView recyclerView,
                                       @NotNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter,
                                       boolean hasFixedSize, int gridSize) {
        GridLayoutManager gll = new GridLayoutManager(recyclerView.getContext(), gridSize);
        recyclerView.setHasFixedSize(hasFixedSize);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setLayoutManager(gll);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    public static void hideKeyboard(@NotNull Context context, View focusedView) {

        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}

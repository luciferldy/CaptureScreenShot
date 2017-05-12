package io.weeknd.capturescreenshot.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Lucifer on 2017/5/12.
 */

public class ScreenShotUtils {

    private static final String LOG_TAG = ScreenShotUtils.class.getSimpleName();

    /**
     * 进行截图
     * @param activity
     * @return
     */
    public static Bitmap captureScreenShot(Activity activity) {
        Bitmap bitmap = null;
        View view = activity.getWindow().getDecorView();
        // 设置是否可以进行缓存
        view.setDrawingCacheEnabled(true);
        //
        view.buildDrawingCache();
        // 返回这个缓存视图
        bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        Log.d(LOG_TAG, "statusBarHeight is " + statusBarHeight);

        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 根据坐标点和需要的宽高创建 bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
        return bitmap;
    }

    public static boolean savePic(Bitmap bitmap, String dirName, String fileName) {
        FileOutputStream fos = null;
        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                if (dir.mkdirs())
                    Log.d(LOG_TAG, "mkdirs successfully.");
            }

            File file = new File(dir, fileName);
            if (file.exists()) {
                if (file.delete())
                    Log.d(LOG_TAG, "delete existed file successfully.");
            } else {
                Log.d(LOG_TAG, "file doesn\'t exists.");
            }
            if (file.createNewFile())
                Log.d(LOG_TAG, "create file successfully.");
            fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

package com.example.dllo.tomatotodo.statistics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by dllo on 16/7/25.
 */
public class BitmapUtils {
    //保存自定义View形成的bitmap
    public static void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                Log.d("MainActivity", "sdRoot:" + sdRoot);
                File file = new File(sdRoot, "test.PNG");
                fos = new FileOutputStream(file);
                Log.d("MainActivity", "fos:" + fos);
            } else {
                throw new Exception("创建文件失败!");

            }

            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            Log.d("MainActivity", "cachebmp:" + cachebmp);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();
    }
    //将自定义View转化为Bitmap
    private static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        Log.d("MainActivity", "v:" + v.getWidth());
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }



}

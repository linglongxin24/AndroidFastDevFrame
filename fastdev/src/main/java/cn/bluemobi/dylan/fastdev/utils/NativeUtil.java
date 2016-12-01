/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.bluemobi.dylan.fastdev.utils;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * 压缩图片工具类
 */
public class NativeUtil {
    private static int DEFAULT_QUALITY = 95;
    private static int outWidth = 720;
    private static int outHeight = 1080;

    public static void compressBitmap(Bitmap bit, String fileName,
                                      boolean optimize) {
        compressBitmap(bit, DEFAULT_QUALITY, fileName, optimize);

    }

    public static void compressBitmap(Bitmap bit, int quality, String fileName,
                                      boolean optimize) {
        Log.d("native", "compress of native");

        // if (bit.getConfig() != Config.ARGB_8888) {
        Bitmap result = null;
        result = Bitmap.createBitmap(bit.getWidth() / 3, bit.getHeight() / 3,
                Config.ARGB_8888);// 缩小3倍
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());// original
        rect = new Rect(0, 0, bit.getWidth() / 3, bit.getHeight() / 3);// 缩小3倍
        canvas.drawBitmap(bit, null, rect, null);
        saveBitmap(result, quality, fileName, optimize);
        result.recycle();
        // } else {
        // saveBitmap(bit, quality, fileName, optimize);
        // }

    }

    public static void compressBitmap(String srcPath, String outPath, int quality) {
        Log.d("native", "compress of native");

        // if (bit.getConfig() != Config.ARGB_8888) {
        BitmapFactory.Options options = getBitmapOptions(srcPath);
        //计算压缩比
        int scanSize = caculateInSampleSize(options, outWidth, outHeight);
        options.inSampleSize = scanSize;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        options.inJustDecodeBounds = false;
        Bitmap outBitmap = BitmapFactory.decodeFile(srcPath, options);
        //旋转图片
        outBitmap = rotateBitmap(outBitmap, getDegress(srcPath));
        saveBitmap(outBitmap, quality, outPath, true);
        if (outBitmap != null && !outBitmap.isRecycled()) {
            outBitmap.recycle();
            System.gc();
        }
    }

    private static void saveBitmap(Bitmap bit, int quality, String fileName,
                                   boolean optimize) {

        compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality,
                fileName.getBytes(), optimize);

    }

    private static native String compressBitmap(Bitmap bit, int w, int h,
                                                int quality, byte[] fileNameBytes, boolean optimize);

    static {
        System.loadLibrary("jpegbither");
        System.loadLibrary("bitherjni");

    }

    /**
     * 计算图片缩放比
     * caculate the bitmap sampleSize
     *
     * @param options
     * @return
     */
    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩资源图片，并返回图片对象
     *
     * @param res   {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resID, options);
    }

    /**
     * 获取图片旋转角度
     * get the orientation of the bitmap {@link ExifInterface}
     *
     * @param path
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * rotate the bitmap
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 得到指定路径图片的options
     *
     * @param srcPath
     * @return Options {@link BitmapFactory.Options}
     */
    public final static BitmapFactory.Options getBitmapOptions(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }
}

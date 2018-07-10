package cn.bluemobi.dylan.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author TangWei 2013-10-24上午10:38:01
 */
public class Tools {

    /**
     * 取得日期是某年的第几周
     */
    @SuppressLint("SimpleDateFormat")
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
        return week_of_year;
    }

    /**
     * 压缩图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }

    public static void Log(String s) {
        if (s == null) {
            s = "传进来的是null";
        }

        Log.i("logs", s);
    }

    public static String MapGetString(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            if (map.get(key) != null && !"".equals(map.get(key))) {
                return ((String) map.get(key)).trim();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String ListToString(@SuppressWarnings("rawtypes") List list) {
        String s = Pattern
                .compile("\\b([\\w\\W])\\b")
                .matcher(
                        list.toString().substring(1,
                                list.toString().length() - 1)).replaceAll("$1")
                .replaceAll(",", " ");
        return s;

    }

    public static Spanned setErrorTextBlack(String s) {
        return Html.fromHtml("<font color=#000>" + s + "</font>");

    }

    public static void Toast(Context context, String s) {
        // if (context == null)
        // context = ShiQiangApplication.getInstance().getApplicationContext();
        if (s != null) {
            Toast.makeText(context, s,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void Toast(Context context, int res) {
        if (context != null) {
            Toast(context, context.getString(res));
        }
    }

    // 得到versionName
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;

    }

    public static String millisToString(long millis) {
        boolean negative = millis < 0;
        millis = Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat
                .getInstance(Locale.US);
        format.applyPattern("00");
        if (millis > 0) {
            time = (negative ? "-" : "")
                    + (hours == 0 ? 00 : hours < 10 ? "0" + hours : hours)
                    + ":" + (min == 0 ? 00 : min < 10 ? "0" + min : min) + ":"
                    + (sec == 0 ? 00 : sec < 10 ? "0" + sec : sec);
        } else {
            time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

    // 得到versionName
    public static int getVerCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;

    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("")
                    || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        return null == s || s.equals("") || s.equalsIgnoreCase("null");

    }
/**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull2(String s) {
        return null == s || s.equals("");

    }

    /**
     * 判断 一个集合的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (null == list.get(i) || Tools.isNull(list.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(TextView... vv) {
        for (int i = 0; i < vv.length; i++) {
            if (null == vv[i] || Tools.isNull(Tools.getText(vv[i]))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param v
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(TextView v) {
        return null == v || Tools.isNull(Tools.getText(v));

    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param v
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(EditText v) {
        return null == v || Tools.isNull(Tools.getText(v));

    }

    /**
     * 判断两个字段是否一样
     *
     * @author Michael.Zhang 2013-08-02 13:32:51
     */
    public static boolean judgeStringEquals(String s0, String s1) {
        return s0 != null && s0.equals(s1);
    }

    /**
     * 将dp类型的尺寸转换成px类型的尺寸
     *
     * @param size
     * @param context
     * @return
     */
    public static int DPtoPX(float size, Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return (int) ( size * metrics.density + 0.5);
    }

    /**
     * 屏幕宽高
     *
     * @param context
     * @return 0:width，1:height
     * @author TangWei 2013-11-5上午10:27:54
     */
    public static int[] ScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /**
     * double 整理
     *
     * @return
     */
    public static Double roundDouble(double val, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = ((0 == val) ? new BigDecimal("0.0") : new BigDecimal(
                Double.toString(val)));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmptyList(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmptyList(List... list) {
        for (int i = 0; i < list.length; i++) {
            if (isEmptyList(list[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(Object[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(Object[]... list) {
        for (int i = 0; i < list.length; i++) {
            if (isEmptyList(list[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断sd卡是否存在
     *
     * @return
     * @author Michael.Zhang 2013-07-04 11:30:54
     */
    public static boolean judgeSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断 http 链接
     *
     * @param url
     * @return
     * @author Michael.Zhang
     */
    public static boolean isUrl(String url) {
        return url != null && url.startsWith("http://");
    }

    /**
     * 获取保存到View的Tag中的字符串
     *
     * @param v
     * @return
     */
    public static String getTagString(View v) {
        try {
            return v.getTag().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取文本控件上显示的文字
     *
     * @param tv
     * @return
     * @author TangWei 2013-9-29下午2:40:52
     */
    public static String getText(TextView tv) {
        if (tv != null)
            return tv.getText().toString().trim();
        return "";
    }

    /**
     * 获取文本控件上显示的文字
     *
     * @param tv
     * @return
     * @author TangWei 2013-9-29下午2:40:52
     */
    public static String getText(EditText tv) {
        if (tv != null)
            return tv.getText().toString().trim();
        return "";
    }

    /**
     * 隐藏键盘
     *
     * @author TangWei 2013-9-13下午7:51:32
     */
    public static void hideKeyboard(Activity activity) {
        ((InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // public static void playSound(int raw, Context context) {
    // SoundPool sp;
    // sp = new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
    // int task = sp.load(context, raw, 1);
    // sp.play(task, 1, 1, 0, 0, 1);
    // }

    /**
     * 显示纯汉字的星期名称
     *
     * @param i 星期：1,2,3,4,5,6,7
     * @return
     * @author TangWei 2013-10-25上午11:31:51
     */
    public static String changeWeekToHanzi(int i) {
        switch (i) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    /**
     * 验证身份证号码
     *
     * @param idCard
     * @return
     * @author TangWei
     */
    public static boolean validateIdCard(String idCard) {
        if (isNull(idCard))
            return false;
        String pattern = "^[0-9]{17}[0-9|xX]{1}$";
        return idCard.matches(pattern);
    }

    /**
     * 验证推荐人编码
     *
     * @param code
     * @return
     * @author TangWei
     */
    public static boolean validateCode(String code) {
        if (isNull(code))
            return false;
        String pattern = "^\\d{6}$";
        return code.matches(pattern);
    }  /**
     * 验证推荐人编码
     *
     * @param lelephone
     * @return  区号-号码 -------------------
    String regex1 = "\\(?(010|021|022|023|024|025|026|027|028|029|852)?\\)?-?\\d{8}";//3位区号,8位号码
    String regex2 = "\\(?(0[3-9][0-9]{2})?\\)?-?\\d{7,8}";//4位区号
    String regex3 = "(\\(?(010|021|022|023|024|025|026|027|028|029|852)?\\)?-?\\d{8})|(\\(?(0[3-9][0-9]{2})?\\)?-?\\d{7,8})";
     * @author TangWei
     */
    public static boolean validateTelePhone(String lelephone) {
        if (isNull(lelephone))
            return false;
        String pattern = "^(\\d{11})$|^(\\d{3,5}[-]?\\d{6,8})$";
        return lelephone.matches(pattern);
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     * @author TangWei
     */
    public static boolean validatePhone(String phone) {
        if (isNull(phone))
            return false;
        String pattern = "^1[3,4,5,6,7,8]+\\d{9}$";
        return phone.matches(pattern);
    }

    /**
     * 验证微信号
     *
     * @param wx
     * @return
     * @author TangWei
     */
    public static boolean validateWX(String wx) {
        if (isNull(wx))
            return false;
        String pattern = "^[a-zA-Z\\d_]+$";
        return wx.matches(pattern);
    }

    /**
     * 验证中文名
     *
     * @param name
     * @return
     * @author TangWei
     */
    public static boolean validateName(String name) {
        if (isNull(name))
            return false;
        String pattern = "[\\u4E00-\\u9FA5]{2,4}";
        return name.matches(pattern);
    }

    public static boolean isContainsChinese(String str) {
        if (isNull(str))
            return false;
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    public static boolean validatePassWord(String password) {
        if (isContainsChinese(password)) {
            return false;
        } else {
            return password.trim().length() >= 6 && password.trim().length() <= 16;
        }

    }

    /**
     * 简单的验证一下银行卡号
     *
     * @param bankCard 信用卡是16位，其他的是13-19位
     * @return
     */
    public static boolean validateBankCard(String bankCard) {
        if (isNull(bankCard))
            return false;
        String pattern = "^\\d{13,19}$";
        return bankCard.matches(pattern);
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     * @author TangWei 2013-12-13下午2:33:16
     */
    public static boolean validateEmail(String email) {
        if (isNull(email))
            return false;
        String pattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        return email.matches(pattern);
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    //    public static boolean validateArtBeianAnName(String email) {
//        String str = "/\\s+|^c:\\\\con\\\\con|[%,\\*\\\"\\s\\<\\>\\&]|\\xA1\\xA1|\\xAC\\xA3|^Guest|^\\xD3\\xCE\\xBF\\xCD|\\xB9\\x43\\xAB\\xC8/is";
//        Pattern p = Pattern.compile(str);
//        Matcher m = p.matcher(email);
//
//        return m.matches();
//    }
    public static boolean validateArtBeianAnEmail(String email) {
        String str = "/^([a-z0-9\\-_.+]+)@([a-z0-9\\-]+[.][a-z0-9\\-.]+)$/";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public static String trimString(String str) {
        if (!Tools.isNull(str)) {
            return str.trim();
        }
        return "";
    }

    public static int StringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float StringToFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0.00f;
        }
    }

    public static String formatString(Object obj) {
        try {
            if (!Tools.isNull(obj.toString())) {
                return obj.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化money，当返回数据为空时，返回0.00
     *
     * @param obj
     * @return
     * @author TangWei 2013-11-23上午11:42:33
     */
    public static String formatMoney(Object obj) {
        String money = formatString(obj);
        if (money.length() == 0) {
            money = "0.00";
        }
        return money;
    }

    /**
     * 计算某天后的多少天
     *
     * @param startDay
     * @param days
     * @return
     */
    public static Date calculateDate(Date startDay, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDay);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return cal.getTime();
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数
     *
     * @param date   日期毫秒数
     * @param format 格式化样式 示例：yyyy-MM-dd HH:mm:ss
     * @return
     * @author TangWei 2013-11-29上午11:31:49
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Object date, String format) {
        try {
            return new SimpleDateFormat(format).format(new Date(Long
                    .parseLong(formatString(date)) * 1000));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12 11:12:13
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    public static String formatTime(Object date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12 11:12:13
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatAddTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

        try {
            return sdf.format((sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    public static String formatDate(Object date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String getFormatedYear(long longTime) {
        return DateFormat.format("yy-MM-dd HH:mm", longTime * 1000).toString();
    }
    public static String getFormatedNotYear(long longTime) {
        return DateFormat.format("MM-dd", longTime * 1000).toString();
    }

    public static String getFormatedDate(long longTime) {
        return DateFormat.format("MM月dd日", longTime * 1000).toString();
    }

    public static String getOrderDate(long longTime) {
        return DateFormat.format("yyyyMMddHHmmss", longTime).toString();
    }

    public static String getFormatedTime(long longTime) {
        return DateFormat.format("kk:mm", longTime * 1000).toString();
    }

    public static String getFormatedMiao(long longTime) {
        return DateFormat.format("kk:mm:ss", longTime * 1000).toString();
    }

    /**
     * 获取屏幕像素尺寸
     *
     * @return 数组：0-宽，1-高
     * @author TangWei 2013-10-31下午1:08:22
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    public static String formatMoney(String money) {
        if (money.contains(".")) {
            String[] ss = money.split("\\.");
            if (ss[1].length() == 0) {
                return ss[0] + ".00";
            } else if (ss[1].length() == 1) {
                return ss[0] +"."+ ss[1] + "0";
            } else if (ss[1].length() == 2) {
                return money;
            } else {
                return money;
            }
        } else {
            return money + ".00";

        }
    }

    /**
     * 设置圆角的图片
     *
     * @param bitmap 图片
     * @param pixels 角度
     * @return
     * @author TangWei 2013-12-10下午4:43:33
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        try {
            if (bitmap != null) {
                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                        bitmap.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final int color = 0xff424242;
                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                        bitmap.getHeight());
                final RectF rectF = new RectF(rect);
                final float roundPx = pixels;

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);

                return output;
            }
        } catch (Exception e) {
        }

        return bitmap;
    }

    /**
     * 将图片转换为圆形的
     *
     * @param bitmap
     * @return
     * @author TangWei 2013-12-10下午4:45:47
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap = cutSquareBitmap(bitmap);
            return toRoundCorner(bitmap, bitmap.getWidth() / 2);
        }
        return bitmap;
    }

    /**
     * 把图片切成正方形的
     *
     * @param bitmap
     * @return
     * @author TangWei 2013-12-10下午5:16:18
     */
    public static Bitmap cutSquareBitmap(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                Bitmap result;
                int w = bitmap.getWidth();// 输入长方形宽
                int h = bitmap.getHeight();// 输入长方形高
                int nw;// 输出正方形宽
                if (w > h) {
                    // 宽大于高
                    nw = h;
                    result = Bitmap.createBitmap(bitmap, (w - nw) / 2, 0, nw,
                            nw);
                } else {
                    // 高大于宽
                    nw = w;
                    result = Bitmap.createBitmap(bitmap, 0, (h - nw) / 2, nw,
                            nw);
                }
                return result;
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 获取在GridView中一行中一张正方形图片的尺寸大小
     *
     * @param context 上下文，用于计算屏幕的宽度
     * @param offset  界面上左右两边的偏移量，dp值
     * @param spac    水平方向，图片之间的间距，dp值
     * @param count   一行中图片的个数
     * @return
     * @author TangWei 2013-12-12下午1:15:49
     */
    public static int getImageSize(Context context, int offset, int spac,
                                   int count) {
        int width = getScreenSize(context)[0] - Tools.DPtoPX(offset, context)
                - (Tools.DPtoPX(spac, context) * (count - 1));
        return width / count;
    }

    /**
     * 获取一个圆弧上等分点的坐标列表
     *
     * @param radius      半径
     * @param count       等分点个数
     * @param start_angle 开始角度
     * @param end_angle   结束角度
     * @return
     * @author TangWei 2013-12-16下午5:06:31
     */
    public static ArrayList<String[]> getDividePoints(double radius, int count,
                                                      double start_angle, double end_angle) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        double sub_angle = (start_angle - end_angle) / ((double) (count - 1));
        for (int i = 0; i < count; i++) {
            double angle = (start_angle - sub_angle * i) * Math.PI / 180;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            list.add(new String[]{x + "", y + ""});
        }
        return list;
    }

    /**
     * 判断字符串是邮箱还是手机号码
     *
     * @param str
     * @return 1-手机号码，2-邮箱，如果都不是则返回0
     * @author TangWei 2013-12-19下午1:59:16
     */
    public static int validatePhoneOrEmail(String str) {
        if (validatePhone(str))
            return 1;
        if (validateEmail(str))
            return 2;
        return 0;
    }

    /**
     * 播放动画
     * @param layout
     * @param img
     * @param drawableBefore
     * @param drawableClick
     * @param isClicked
     */
    public static void startAnimation(final View layout, ImageView img,
                                      int drawableBefore, int drawableClick, boolean isClicked) {
        if (isClicked) {
            img.setBackgroundResource(drawableClick);
        } else {
            img.setBackgroundResource(drawableBefore);
        }

        // 播放动画
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation1.setStartOffset(0);
        scaleAnimation1.setDuration(50);
        scaleAnimation2.setStartOffset(50);
        scaleAnimation2.setDuration(50);
        animationSet.addAnimation(scaleAnimation1);
        animationSet.addAnimation(scaleAnimation2);
        animationSet.setFillAfter(true);
        img.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encode(bitmapBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * SD卡是否存在
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建文件夹
     */
    public static void makeDir(String path) {
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdir();
        }
    }

    /**
     * 根据Uri返回文件路径
     *
     * @param mUri
     * @return String
     * @author gdpancheng@gmail.com 2013-3-18 上午10:17:55
     */
    public static String getFilePath(ContentResolver mContentResolver, Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(mContentResolver, mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 将100以内的阿拉伯数字转换成中文汉字（15变成十五）
     *
     * @param round 最大值50
     * @return 大于99的，返回“”
     */
    public static String getHanZi1(int round) {
        if (round > 99 || round == 0) {
            return "";
        }
        int ge = round % 10;
        int shi = (round - ge) / 10;
        String value = "";
        if (shi != 0) {
            if (shi == 1) {
                value = "十";
            } else {
                value = getHanZi2(shi) + "十";
            }

        }
        value = value + getHanZi2(ge);
        return value;
    }

    /**
     * 将0-9 转换为 汉字（ _一二三四五六七八九）
     *
     * @param round
     * @return
     */
    public static String getHanZi2(int round) {
        String[] value = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        return value[round];
    }

    /**
     * 将content: 开通的系统uri转换成绝对路径
     *
     * @param mContentResolver
     * @param mUri
     * @return
     * @throws FileNotFoundException
     */
    public static String getFilePathByUri(ContentResolver mContentResolver,
                                          Uri mUri) throws FileNotFoundException {

        String imgPath;
        Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
        cursor.moveToFirst();
        imgPath = cursor.getString(1); // 图片文件路径
        cursor.close();
        return imgPath;
    }

    /**
     * 去除字符串中的 ":"
     *
     * @param str
     * @return
     */
    public static String deleteColon(String str) {
        if (str == null) {
            return null;
        } else {
            return str.replace(":", "");
        }
    }

    /**
     * 将 1800 加个":",变成 18:00
     *
     * @param str
     * @return
     */
    public static String addColon(String str) {
        if (str == null || str.length() != 4) {
            return null;
        }
        return str.substring(0, 2) + ":" + str.substring(2, 4);
    }

    /**
     * 获取map中的值
     * @param map map
     * @param key map的key
     * @return map的值
     */
    public static String getValue(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0) {
            return "";
        } else if (isNull(key)) {
            return "";
        } else if (map.containsKey(key)) {
            Object data = map.get(key);
            if (data instanceof String) {
                if (isNull2((String) map.get(key))) {
                    return "";
                } else {
                    return map.get(key).toString();
                }
            } else {
                return String.valueOf(map.get(key));
            }

        } else {
            return "";
        }
    }

    static Toast toast;

    public static void show(Context context, CharSequence message, int duration) {
        if (null == toast) {
            toast = Toast.makeText(context, message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
    //强制显示或者关闭系统键盘
    public static void KeyBoard(final EditText txtSearchKey, final String status)
    {

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run()
            {
                InputMethodManager m = (InputMethodManager)
                        txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(status.equals("open"))
                {
                    m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                }
                else
                {
                    m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        }, 300);
    }
    //显示虚拟键盘
    public static void showKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }
    /**
     * 验证用户只能输入小数点后后两位并且第一位输入.的时候前面自动补0；
     *
     * @param editText
     */
    public void setPricePoint(final EditText editText, final int pointCount) {//
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editText.getText().toString();
                if (s.contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > pointCount) {
                        s = s.substring(0,
                                s.indexOf(".") + pointCount+1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

        });

    }

}

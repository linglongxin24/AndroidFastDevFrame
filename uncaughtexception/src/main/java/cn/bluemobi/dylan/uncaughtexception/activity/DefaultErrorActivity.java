package cn.bluemobi.dylan.uncaughtexception.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cat.ereza.customactivityoncrash.R;
import cn.bluemobi.dylan.uncaughtexception.CustomActivityOnCrash;
import cn.bluemobi.dylan.uncaughtexception.email.ReportByEmail;


public final class DefaultErrorActivity extends Activity {
    private View CustomView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customactivityoncrash_default_error_activity);
        Button restartButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_restart_button);

        final Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());

        if (restartActivityClass != null) {
            restartButton.setText(R.string.customactivityoncrash_error_activity_restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(DefaultErrorActivity.this, restartActivityClass);
                    CustomActivityOnCrash.restartApplicationWithIntent(DefaultErrorActivity.this, intent);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(DefaultErrorActivity.this);
                }
            });
        }

        Button moreInfoButton = (Button) findViewById(R.id.customactivityoncrash_error_activity_more_info_button);

        if (CustomActivityOnCrash.isShowErrorDetailsFromIntent(getIntent())) {
            moreInfoButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    AlertDialog dialog = new Builder(DefaultErrorActivity.this).
                            setTitle(R.string.customactivityoncrash_error_activity_error_details_title).
                            setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(DefaultErrorActivity.this, DefaultErrorActivity.this.getIntent())).
                            setPositiveButton(R.string.customactivityoncrash_error_activity_error_details_close, null).
                            setNegativeButton(R.string.report_to_developer, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Thread() {
                                                public void run() {
                                                    super.run();
                                                    if (!TextUtils.isEmpty(CustomActivityOnCrash.getDingtalkUrl())) {
                                                        String url = CustomActivityOnCrash.getDingtalkUrl();
                                                        String json = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + CustomActivityOnCrash.getAllErrorDetailsFromIntent(DefaultErrorActivity.this, DefaultErrorActivity.this.getIntent()) + "\"}}";
                                                        requestPost(url, json);
                                                    }
                                                    ReportByEmail.sendEmail2(CustomActivityOnCrash.getApplicationName(DefaultErrorActivity.this), CustomActivityOnCrash.getAllErrorDetailsFromIntent(DefaultErrorActivity.this, DefaultErrorActivity.this.getIntent()));
                                                }
                                            }.start();
                                        }
                                    }

                            ).setNeutralButton(R.string.customactivityoncrash_error_activity_error_details_copy, new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialog, int which) {

                            DefaultErrorActivity.this.copyErrorToClipboard();
                            Toast.makeText(DefaultErrorActivity.this, R.string.customactivityoncrash_error_activity_error_details_copied, Toast.LENGTH_SHORT).show();
                        }


                    }).show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(0, DefaultErrorActivity.this.getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));


                }


            });


        } else {


            moreInfoButton.setVisibility(8);
        }

        int defaultErrorActivityDrawableId = CustomActivityOnCrash.getDefaultErrorActivityDrawableIdFromIntent(getIntent());
        ImageView errorImageView = (ImageView) findViewById(R.id.customactivityoncrash_error_activity_image);
        if (Build.VERSION.SDK_INT >= 21) {
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId, getTheme()));
        } else {
            errorImageView.setImageDrawable(getResources().getDrawable(defaultErrorActivityDrawableId));
        }
    }

    protected Builder myBuilder(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        Builder builder = new Builder(context);
        this.CustomView = inflater.inflate(R.layout.customview, null);
        return builder.setView(this.CustomView);
    }

    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());

        if (Build.VERSION.SDK_INT >= 11) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService("clipboard");
            ClipData clip = ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService("clipboard");
            clipboard.setText(errorInformation);
        }
    }

    /**
     * Post请求
     *
     * @param httpUrl
     * @param json
     */
    private void requestPost(String httpUrl, String json) {
        try {
            String baseUrl = httpUrl;
            //合成参数
            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
//                pos++;
//            }
//            String params = tempParams.toString();
            System.out.println("Post方式请求地址httpUrl--->" + httpUrl);
//            System.out.println("Post方式请求参数params--->" + params);
            // 请求的参数转换为byte数组
//            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            //设置请求头里面的数据，以下设置用于解决http请求code415的问题
            urlConn.setRequestProperty("Content-Type",
                    "application/json");

//            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(json.getBytes());
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                System.out.println("Post方式请求成功，result--->" + result);
            } else {
                System.out.println("Post方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
/* Location:              E:\kejiang\AndroidStudioProjects\inspection\app\libs\UncaughtExceptionSendEmail\classes.jar!\cat\ereza\customactivityoncrash\activity\DefaultErrorActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
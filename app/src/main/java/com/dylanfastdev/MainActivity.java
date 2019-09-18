package com.dylanfastdev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.base.adapter.GridViewAddImagesAdapter;
import cn.bluemobi.dylan.base.adapter.common.ViewHolder;
import cn.bluemobi.dylan.base.adapter.common.abslistview.CommonAdapter;
import cn.bluemobi.dylan.base.utils.MyImageLoader;
import cn.bluemobi.dylan.base.view.CircleImageView;
import cn.bluemobi.dylan.base.view.CycleViewPager;
import cn.bluemobi.dylan.base.view.RatingBar;
import cn.bluemobi.dylan.base.view.SelectPopupWindow;
import cn.bluemobi.dylan.base.view.iOSOneButtonDialog;
import cn.bluemobi.dylan.base.view.iOSSelectDialog;
import cn.bluemobi.dylan.base.view.iOSTwoButtonDialog;
import cn.bluemobi.dylan.photoview.ImagePagerActivity;
import cn.bluemobi.dylan.smartwebview.SmartWebView;

public class MainActivity extends BaseActivity {

    private final String TAG = "MainActivity";
    private WebView webView;
    private CycleViewPager cycle_view_pager;
    private FrameLayout fm;
    private List<String> paths = new ArrayList<>();
    private GridView gv;
    private GridViewAddImagesAdapter gridViewAddImgesAdpter;
    private SmartWebView smartWebView;
    private CircleImageView ci;
    private Button bt_pay;
    private Button bt_dialog;


    /**
     * 仿iOS对话框
     */
    private void testiOSDialog() {
        Button bt_dialog = (Button) findViewById(R.id.bt_dialog);
        bt_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new iOSTwoButtonDialog(mContext).setCenterCustomView(R.layout.dialog_add_score_radiot).show();
////                if (true) {
////                    return;
////                }
////                new iOSSelectDialog(MainActivity.this)
////                        .setTitle("请选择邀请方式")
////                        .addMenuText("手机号")
////                        .addMenuText("微信")
////                        .setMenuClickLisentner(new iOSSelectDialog.MenuClick() {
////                            @Override
////                            public void onMenuClick(String menuText) {
////                                Logger.d("menuText=" + menuText);
////                            }
////                        }).show();
                new iOSOneButtonDialog(mContext).setMessage("请不要这样")
                        .setTitleLineVisiBility(View.VISIBLE)
                        .show();

            }
        });
    }

    private void testPay() {
        bt_pay = (Button) findViewById(R.id.bt_pay);
//        bt_pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AliPay(MainActivity.this
//                        , ""
//                        , ""
//                        , "支付宝测试"
//                        , "支付商品"
//                        , OrderInfoUtil2_0.getOutTradeNo()
//                        , "0.02"
//                        , ""
//                        , true
//                ).pay(new PayListener() {
//                    @Override
//                    public void paySuccess() {
//                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        Logger.d("支付成功");
//                    }
//
//                    @Override
//                    public void payFailed() {
//                        Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                        Logger.d("支付失败");
//                    }
//
//                    @Override
//                    public void payCancel() {
//                        Toast.makeText(MainActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
//                        Logger.d("支付取消");
//                    }
//                });
//            }
//        });
    }

    /**
     * retrofit测试
     */
    private void testHttp() {
//        List<File> fileList=new ArrayList<>();
//        fileList.add(new File("/storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20170823_130257.mp4"));
//        Http.getHttp().init(ApiService.class, ApiService.baseUrl, "status", "data", "msg", 0);
//        Http.with(context)
//                .setObservable(Http.getApiService(ApiService.class)
//                        .upload(cn.bluemobi.dylan.http.RequestParameter.getRequestBody("Task")
//                                , cn.bluemobi.dylan.http.RequestParameter.getRequestBody("Task_an_AddNeed")
//                                , cn.bluemobi.dylan.http.RequestParameter.getRequestBody("13b30123e894904eafe2c4a9a7d64bea")
//                                , cn.bluemobi.dylan.http.RequestParameter.getRequestBody("46")
//                                , cn.bluemobi.dylan.http.RequestParameter.getRequestBody("1")
//                                , cn.bluemobi.dylan.http.RequestParameter.getRequestBody("2")
//                                , cn.bluemobi.dylan.http.RequestParameter.getRequestBody("1")
//                                ,cn.bluemobi.dylan.http.RequestParameter.getPartMap("attachment",fileList)
//                        )
//                )
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//
//                    }
//                });
        ci = (CircleImageView) findViewById(R.id.ci);
//        Http.getHttp()
//                .setDebugMode(BuildConfig.DEBUG)
//                .init(ApiService.class, ApiService4.BASE_URL, "returnCode", "data", "returnMsg", 200)
//                .setShowMessageModel(MessageManager.MessageModel.All);
//                .init(ApiService.class, ApiService4.BASE_URL, "state", "data", "msg", 1);
//        ci.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });
//        Http.with(context)
//                .setObservable(Http.getApiService(ApiService4.class).getCompetitionList("", 1, 10))
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//
//                    }
//                });

        /**设置网络请求**/
//        Http.getHttp().init(ApiService.class, ApiService2.BASE_HOST, "status", "data", "msg", 0);

//        Http.with(context)
//                .setObservable(Http.getApiService(ApiService2.class).getAd("Advert", "Advertr_an_HomeList"))
//                .setDataListener(new HttpCallBack() {
//                                     @Override
//                                     public void netOnSuccess(Map<String, Object> data) {
//
//                                     }
//                                 }
//                );


//       Http.with(context)
//               .setObservable( Http.getApiService(ApiService4.class)
//                       .getHomeData())
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//
//                    }
//                });
//        Http.getHttp().init(ApiService2.class, ApiService2.BASE_URL, "error", "content", "message", 0).setShowMessageModel(MessageManager.MessageModel.All);
//        Http.with(context)
//                .setObservable(Http.getApiService(ApiService2.class).getHomeData())
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//                    }
//                });

//        Http.with(context)
//                .setObservable(Http.getApiService(ApiService2.class).getClassifyData())
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//                    }
//                });
//        HttpUtils httpUtils = HttpUtils.getInstance();
//        httpUtils.init(ApiService.baseUrl, "status", "data", "msg", 0, null);
//        HttpUtils.getInstance()
//                .with(context)
//                .setObservable(
//                        HttpUtils.getApiService(ApiService.class)
//                                .getTopMove("Advert", "GetAdvert"))
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//
//                    }
//                });
//        String s = "Advert" + "GetAdvert" + ApiService.secret;
//        String sign = MD5Utils.md5(s);
//        HttpUtils.getInstance().post(context,
//                HttpUtils.getApiService(ApiService.class).getTopMove("Advert", "GetAdvert"),
//                new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
//                        Logger.d("data=" + data);
//                    }
//                });

//        cn.bluemobi.dylan.fastdev.http.HttpUtils.getInstance().addSignParameters(true);
//        RequestParams requestParams=new RequestParams(ApiService.baseUrl);
//        requestParams.addBodyParameter("app","Advert");
//        requestParams.addBodyParameter("class","GetAdvert");
//        ajax(requestParams);
//
//         requestParams=new RequestParams("http://175.102.24.27:8917/api/");
//        requestParams.addBodyParameter("app", "Serve");
//        requestParams.addBodyParameter("class", "Serve_an_ServeContent");
//        requestParams.addBodyParameter("sign", "b6653ef7ec1055ae491e24bd97bde65b");
//        requestParams.addBodyParameter("serve_content_id", "6");
//        requestParams.addBodyParameter("city_id", "142");
//        ajax(requestParams);
    }


    private void showAddImageDialog() {
        gv = (GridView) findViewById(R.id.gv);
        paths = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImagesAdapter(paths, mContext, 5, 10);
        gv.setAdapter(gridViewAddImgesAdpter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                showDialog();
            }
        });
        gridViewAddImgesAdpter.setImageClickListener(new GridViewAddImagesAdapter.ImageClickListener() {
            @Override
            public void onImageClick(int position) {
                Logger.d("position="+position);
            }
        });
    }
//
//    @Override
//    public void photoCompressStart(String path) {
//        super.photoCompressStart(path);
//        paths.add(path);
//        gridViewAddImgesAdpter.notifyDataSetChanged();
//    }
//
//    public void photoPath(String path) {
//        paths.set(paths.size() - 1, path);
//        gridViewAddImgesAdpter.notifyDataSetChanged();
//    }

    private void showSelectPopupWindow() {
        final Button bt = (Button) findViewById(R.id.bt);
        fm = (FrameLayout) findViewById(R.id.fm);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> datas = new ArrayList<>();
                datas.add("选择1");
                datas.add("选择2");
                datas.add("选择3");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
                datas.add("选择4");
//                SelectPopupWindow selectPopupWind = new SelectPopupWindow(MainActivity.this, bt, new CommonAdapter<String>(mContext, datas, R.layout.item) {
//                    @Override
//                    public void convert(ViewHolder holder, String s) {
//
//                    }
//
//                }, fm);
//                selectPopupWind.show();
            }
        });

    }

    private void showCycleViewPager() {
        cycle_view_pager = (CycleViewPager) findViewById(R.id.cv);
        //设置选中和未选中时的图片
        assert cycle_view_pager != null;
        cycle_view_pager.setIndicators(R.mipmap.dot_focus, R.mipmap.dot_normal);
        final List<String> urls = new ArrayList<>();
        urls.add("http://pic.58pic.com/58pic/15/37/32/458PICq58PICPd4_1024.jpg");
        urls.add("http://pic8.nipic.com/20100627/1614097_004848992811_2.jpg");
        urls.add("http://pic.58pic.com/58pic/15/35/53/14t58PICUJv_1024.jpg");
        cycle_view_pager.setData(urls, new CycleViewPager.ImageCycleViewListener() {
            @Override
            public void onImageClick(String url, int position, View imageView) {
                Logger.d("点击了=" + position);
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                String[] arr = new String[urls.size()];
                arr = urls.toArray(arr);
                intent.putExtra(ImagePagerActivity.EXTRA_IS_SHOW_SAVE_BUTTON, true);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arr);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }

        });
    }

    private void showCircleImage() {
        final String url = "http://img.blog.csdn.net/20161016171244996";
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.ci);
        MyImageLoader.loadRoundImage(mContext, url, circleImageView);
//        x.Ext.init(getApplication());
//        x.image().bind(circleImageView, url, new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_CROP).build());
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = new String[]{url};
                showBigImage(arr, 1);
            }
        });
    }

    /**
     * 浏览图片
     *
     * @param arr      图片地址集合
     * @param position 当前位置
     */
    private void showBigImage(String[] arr, int position) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arr);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
        //切换Activity的过渡动
        overridePendingTransition(R.anim.zoom_ente, R.anim.zoom_exit);
    }

    private void setMyRatingBar() {
        setContentView(R.layout.ac_ratingbar);
        smartWebView = (SmartWebView) findViewById(R.id.smartWebView);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rb);
        ratingBar.setClickable(true);//设置可否点击
        ratingBar.setStar(2.5f);//设置显示的星星个数
        ratingBar.setStepSize(RatingBar.StepSize.Half);//设置每次点击增加一颗星还是半颗星
        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {//点击星星变化后选中的个数
                Log.d("RatingBar", "RatingBar-Count=" + ratingCount);
            }
        });

//        String url = "http://nongzhangmen.cn:8911/page/Index/index?id=2406";
//        String url = "https://app.tongrunjacks.com//other//index?id=TkE9PQ==";
//        String url = "http://c.dituhui.com/maps/1082953";
//        String url = "http://www.kswjw.gov.cn/jsfw/m_index.asp?deviceType=3&womanId=42996&machineCode=8767fea2-6376-42be-bd1e-c01ccaf3e02c&requestStartTime=636263218692140000&os=Android6.0&network=WIFI";
//        String url = "http://mp.weixin.qq.com/s/E3zeRiZ9AxGH6sL_5JYLkg";
        String url = "http://xdkd.boxkj.com/app/appnotice/h5appLoadNotice?noticeId=1&userId=38069";
//        String url = "http://mobile.abchina.com/download/clientDownload/zh_CN/MB_Index.aspx?from=message&isappinstalled=0";
        smartWebView.loadUrl(url);
    }

    /**
     * 普通适配器的方法
     */
    private void myAdapterTest() {
//        setContentView(R.layout.pub_activity_main);
//        ListView listView = (ListView) findViewById(R.id.listview);
//        List<String> datas = new ArrayList<>();
//        datas.add("普通适配器测试1");
//        datas.add("普通适配器测试2");
//        datas.add("普通适配器测试3");
//        datas.add("普通适配器测试4");
//        listView.setAdapter(new MyAdapter(context, datas));
    }

    /**
     * 万能适配器的方法
     */
    private void commonAdapterTest() {
//        setContentView(R.layout.pub_activity_main);
//        ListView listView = (ListView) findViewById(R.id.listview);
//        List<String> datas = new ArrayList<>();
//        for (int i = 0; i < 18; i++) {
//            datas.add("万能适配器测试" + i);
//        }
//        listView.setAdapter(new CommonAdapter<String>(context, datas, R.layout.item) {
//
//            @Override
//            protected void convertView(View item, String s) {
//                TextView textView = CommonViewHolder.get(item, R.id.textView);
//                textView.setText(s);
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showDialog();
//            }
//        });
    }

    public void textException(View v) {
        throw new RuntimeException("测试异常");
    }

//    @Override
//    public void photoPath(String path) {
//        super.photoPath(path);
//        RequestParams requestParams = new RequestParams("http://10.58.178.120:8080/intco/mobi/member/uploadImage");
//        requestParams.addBodyParameter("mId", "1");
//        requestParams.addBodyParameter("imgFile", new File(path));
//        ajax(requestParams);
//    }

    /**
     * 网页测试
     */
    private void webViewTest() {
        webView = new WebView(this);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.getSettings().setDomStorageEnabled(true);
//
//
//        WebSettings ws = webView.getSettings();
//        /**
//         *
//         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
//         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
//         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
//         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
//         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
//         * setSupportZoom 设置是否支持变焦
//         * */
//        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
//        ws.setUseWideViewPort(true);// 可任意比例缩放
//        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
//        ws.setSavePassword(true);
//        ws.setSaveFormData(true);// 保存表单数据
//        ws.setJavaScriptEnabled(true);
//        ws.setGeolocationEnabled(true);// 启用地理定位
//        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
//        ws.setDomStorageEnabled(true);
//        ws.setPluginState(WebSettings.PluginState.ON); //设置webview支持插件
//        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);
//        webView.loadUrl("http://www.wzjgswj.gov.cn/wap/NewCont.aspx?ColumnID=34&ID=4107");
//        loadUrl(webView,"http://116.236.217.38:7679/V4.0_WebApp/Area/WebApp/Hospital/OnlineConsultation/Main.html?deviceType=3&machineCode=5c101e60-ca96-424e-8328-b9fa8b3daab8&womanId=42493&requestStartTime=636116568969480000&os=Android5.1&network=WIFI");
//        loadUrl(webView, "http://54.222.212.189/v_graph/v_graph.html?from_portal=1&_v=1462845840614");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        try {
//            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void initTitleBar() {
//        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_ratingbar;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        setMyRatingBar();
        showCircleImage();
        showCycleViewPager();
        showSelectPopupWindow();
//        showAddImageDialog();
//        new   LoadingDialog(this).show();
//        cn.bluemobi.dylan.http.Http.getHttp().setLoadingDialog(LoadingDialog.class);
        testHttp();
        testPay();
        testiOSDialog();
    }

//        setContentView(R.layout.pub_activity_main);


    @Override
    public void initData() {
//        RequestParams requestParams = new RequestParams("http://10.58.187.40:8080/WebDemo/login?userName=zhangsan&passWord=45678");
//        ajax(requestParams);
    }


    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}

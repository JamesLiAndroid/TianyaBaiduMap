package com.tianya;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hjhrq1991.library.BridgeWebView;
import com.hjhrq1991.library.CallBackFunction;
import com.hjhrq1991.library.DefaultHandler;
import com.hjhrq1991.library.SimpleBridgeWebViewClientListener;
import com.tianya.bean.LocationDataBean;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
//BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    private String url;
    BridgeWebView webView;
    Button backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(10000); // 每10秒请求一次
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        option.setIsNeedLocationDescribe(true);
//可选，是否需要位置描述信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的位置信息，此处必须为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求

        url = "file:///android_asset/test_location_in.html";

        initView();
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            Log.d("TAG", "d定位信息：\n 经纬度："+longitude+", "+latitude + "\n");
            Log.d("TAG", "无关紧要的信息："+ radius+",,,"+coorType+",,,"+errorCode);

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            Log.d("TAG", "获取的地址信息："+addr+"\n"+country+"\n"+province+"\n"+city+"\n"+district+"\n"+street);

            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            Log.d("TAG", "位置描述信息："+locationDescribe);
            // 缓存当前定位数据
            if (webView == null) {
                initView();
            } else {
                if (addr != null) {
                    LocationDataBean bean = new LocationDataBean();
                    bean.setLatitude(location.getLatitude());
                    bean.setLongitude(location.getLongitude());
                    bean.setAddr(location.getAddrStr());
                    bean.setCountry(location.getCountry());
                    bean.setProvince(location.getProvince());
                    bean.setCity(location.getCity());
                    bean.setDistrict(location.getDistrict());
                    bean.setStreet(location.getStreet());
                    bean.setLocationDescribe(location.getLocationDescribe());
                    Log.d("TAG", bean.toString());
                    webView.callHandler("click_location", bean.toString(), new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Log.i(TAG, "回传结果：" + data);
                            Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }
    }

    private void initView() {
        webView = (BridgeWebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        //设定支持h5viewport
        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕.
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWwebViewebView.getSettings().setUserAgentString(Constant.useragent);

        backBtn = (Button) findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        //=======================js桥使用改方法替换原有setWebViewClient()方法==========================
        webView.setBridgeWebViewClientListener(new SimpleBridgeWebViewClientListener() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "超链接：" + url);
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(TAG, "超链接：" + url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap bitmap) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            @Override
            public boolean onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                String message;
                switch (error.getPrimaryError()) {
                    case android.net.http.SslError.SSL_UNTRUSTED:
                        message = "证书颁发机构不受信任";
                        break;
                    case android.net.http.SslError.SSL_EXPIRED:
                        message = "证书过期";
                        break;
                    case android.net.http.SslError.SSL_IDMISMATCH:
                        message = "网站名称与证书不一致";
                        break;
                    case android.net.http.SslError.SSL_NOTYETVALID:
                        message = "证书无效";
                        break;
                    case android.net.http.SslError.SSL_DATE_INVALID:
                        message = "证书日期无效";
                        break;
                    case android.net.http.SslError.SSL_INVALID:
                    default:
                        message = "证书错误";
                        break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示").setMessage(message + "，是否继续").setCancelable(true)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                handler.proceed();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                handler.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });
        //=======================此方法必须调用==========================
        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.loadUrl(url);

        //description：如需使用自定义桥名，调用以下方法即可，
        // 传空或不调用setCustom方法即使用默认桥名。
        // 默认桥名：WebViewJavascriptBridge
        //=======================使用自定义桥名时调用以下代码即可==========================
//        webView.setCustom("桥名");
        webView.setCustom("TestJavascriptBridge");

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (webView.canGoBack())
                    webView.goBack();
                else
                    finish();
                break;
        }
    }


}

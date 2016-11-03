package com.android.anqiansong.xhttpsample.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.anqiansong.callback.Callback;
import com.android.anqiansong.request.XBitmap;
import com.android.anqiansong.request.XGet;
import com.android.anqiansong.xhttp.XHttp;
import com.android.anqiansong.xhttpsample.Constant;
import com.android.anqiansong.xhttpsample.R;
import com.android.anqiansong.xhttpsample.entity.JokeResponse;

import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private ImageView iv_image;
    private Button btnBitmap;// 请求bitmap
    private Button btnJson;// 请求json
    private Button btnObject;// 请求object,即解析后数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XHttp.onCreate(this);// 用于标识唯一网络请求,以activity为单位
        initView();
        setListeners();
    }

    private void initView() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        btnBitmap = (Button) findViewById(R.id.btn_bitmap);
        btnJson = (Button) findViewById(R.id.btn_json);
        btnObject = (Button) findViewById(R.id.btn_object);
    }

    /**
     * 设置监听
     */
    private void setListeners() {
        btnBitmap.setOnClickListener(this);
        btnJson.setOnClickListener(this);
        btnObject.setOnClickListener(this);
    }

    /**
     * 获取图片
     */
    private void getBitmap() {
        XBitmap xBitmap = new XBitmap();
        xBitmap.requestUrl = Constant.PIC;
        XHttp.request(xBitmap, new Callback<Bitmap>() {

            @Override
            public void onSuccess(Bitmap bitmap) {
                iv_image.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 获取json
     */
    private void getJson() {
        XGet xGet = new XGet();
        xGet.requestUrl = Constant.JOKE_URL;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("apikey", Constant.APP_KEY);
        xGet.requestHeaders = headers;
        XHttp.request(xGet, new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, s);
            }
        });
    }

    /**
     * 获取解析后的数据
     */
    private void getObject() {
        XGet xGet = new XGet();
        xGet.requestUrl = Constant.JOKE_URL;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("apikey", Constant.APP_KEY);
        xGet.requestHeaders = headers;
        XHttp.request(xGet, new Callback<JokeResponse>() {
            @Override
            public void onSuccess(JokeResponse response) {
                Log.d(TAG, response.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bitmap:
                getBitmap();
                break;
            case R.id.btn_json:
                getJson();
                break;
            case R.id.btn_object:
                getObject();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XHttp.onDestroy(this);
    }
}

package com.android.anqiansong.xhttp;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.anqiansong.callback.Callback;
import com.android.anqiansong.request.XBase;
import com.android.anqiansong.request.XBitmap;
import com.android.anqiansong.request.XGet;
import com.android.anqiansong.request.XPost;
import com.android.anqiansong.request.XPostJson;
import com.android.anqiansong.request.XPostString;
import com.android.anqiansong.request.XUploadFile;
import com.android.anqiansong.response.ErrorInfo;
import com.android.anqiansong.util.TypeTake;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * XHttp--轻量级的网络工具请求类,基于OkGo封装的一个实用性的网络工具请求类</br>
 * 此工具可能好与低版本的OKHttp有冲突</br>
 * 本工具中采用的是fastjson 1.2.9版本解析,因此在依赖过程中请勿多次引用fastjson</br>
 * 网络唯一标识是以Activity为单位的,如果需要控制网络请求与取消,可以在Activity调用</br>
 * onCreate(...)和onDestroy(...)时调用{@link #onCreate(Activity)}和{@link #onDestroy(Activity)}</br>
 * 方法即可.</br>
 * 特别注意:</br>
 * 在接口回调时,根据您需要的类型指定泛型,如请求Bitmap,则需要在Callback<T>T应指定为Bitmap</br>
 * 请求String,则需要在Callback<T>T应指定为String,请求解析后的实体类,则需要在Callback<T>T应指定为待解析的JavaBean</br>
 * <a href="https://github.com/jeasonlzy/okhttp-OkGo">OKGo</a></br>
 * XHttp作者博客:<a href="http://blog.csdn.net/qq243223991">安前松博客</a></br>
 */

public class XHttp {
    /**
     * 默认超时时间为15秒,包括连接超时,读取超时,写入超时
     */
    public static final int DEFAULT_MILLISECONDS = 15000;
    /**
     * 网络请求唯一标识
     */
    private static String tagActivity;

    /**
     * 是否已经初始化
     */
    private static boolean init = false;


    public static void init(Application application) {
        OkGo.init(application);// 初始化
        initDefaultConfig();// 默认参数配置
        init = true;
    }

    /**
     * 设置超时时间(单位:毫秒),默认为15秒
     *
     * @param milliseconds
     */
    public static void setTimeOut(int milliseconds) {
        if (!isInit()) return;
        OkGo.getInstance().setConnectTimeout(milliseconds).setReadTimeOut(milliseconds).setWriteTimeOut(milliseconds);
    }

    /**
     * 添加全局headers
     *
     * @param headers
     */
    public static void addHeaders(HashMap<String, String> headers) {
        if (!isInit()) return;
        OkGo.getInstance().addCommonHeaders(getHeaders(headers));
    }

    /**
     * 是否需要开启缓存
     *
     * @param cache 如果开启缓存,则优先先使用缓存，不管是否存在缓存，仍然请求网络</br>
     *              如果需要设置缓存模式,请查看{@link #setCacheMode(CacheMode)}</br>
     */
    public static void setCache(boolean cache) {
        if (!isInit()) return;
        if (!cache) return;
        OkGo.getInstance().setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST);
    }

    /**
     * @param cacheMode 设置缓存模式,不需要设置setCache(true),更多缓存模式请查看{@link CacheMode}
     */
    public static void setCacheMode(CacheMode cacheMode) {
        if (!isInit()) return;
        OkGo.getInstance().setCacheMode(cacheMode);
    }

    /**
     * 设置请求证书
     *
     * @param inputStream
     */
    public static void setCertificates(InputStream inputStream) {
        if (!isInit()) return;
        OkGo.getInstance().setCertificates(inputStream);
    }

    /**
     * 在activity调用onCreate(...)是调用,用于标志一个唯一网络请求
     *
     * @param activity
     */
    public static void onCreate(Activity activity) {
        tagActivity = activity.getClass().getName();
    }

    /**
     * 在activity调用onDestroy(...)时调用,用于取消唯一标志对应的网络请求
     *
     * @param activity
     */
    public static void onDestroy(Activity activity) {
        OkGo.getInstance().cancelTag(activity.getClass().getName());
    }


    /**
     * 默认参数配置
     */
    private static void initDefaultConfig() {
        try {
            OkGo.getInstance()
                    .setConnectTimeout(DEFAULT_MILLISECONDS)
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)
                    .setCookieStore(new PersistentCookieStore());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 是否已经初始化
     *
     * @return true:初始化,false 没有初始化,则会抛出异常
     */
    private static boolean isInit() {
        if (init) return true;
        try {
            throw new Exception("请先调用init(Application)进行初始化|You Must Call Method init(Application) First");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 基本请求
     *
     * @param callback 回调
     */
    public static void request(XBase xBase, Callback callback) {
        try {
            if (!isInit()) return;
            if (xBase instanceof XBitmap) {// Bitmap请求
                bitmap(xBase, callback, tagActivity);
                return;
            }
            if (xBase instanceof XGet) {// get请求
                get(xBase, callback, tagActivity);
                return;
            }
            if (xBase instanceof XPost) {// post请求
                post(xBase, callback, tagActivity);
                return;
            }
            if (xBase instanceof XPostJson) {// postjson请求
                postJson(xBase, callback, tagActivity);
                return;
            }
            if (xBase instanceof XPostString) {// postString请求
                postString(xBase, callback, tagActivity);
                return;
            }
            if (xBase instanceof XUploadFile) {// 上传文件
                uploadFile(xBase, callback, tagActivity);
                return;
            }
            try {
                throw new Exception("请求类型不支持|Your Request Is Not Allowed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载请求
     *
     * @param xBase        基本请求载体
     * @param destFileDir  存储路径
     * @param destFileName 存储文件名
     * @param callback
     * @param tag
     */
    public static void download(XBase xBase, String destFileDir, String destFileName, Callback callback, Object tag) {
        try {
            if (!isInit()) return;

            if (xBase instanceof XUploadFile) {// 下载文件
                downloadFile(xBase, destFileDir, destFileName, callback, tag);
                return;
            }
            try {
                throw new Exception("请求类型不支持|Your Request Is Not Allowed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * bitmap请求
     *
     * @param xBase    请求实体
     * @param callback 回调
     * @param tag      请求唯一标识
     */
    private static void bitmap(XBase xBase, final Callback callback, Object tag) {
        try {
            HttpHeaders httpHeaders = getHeaders(xBase);
            XBitmap bitmapRequest = (XBitmap) xBase;
            String url = bitmapRequest.requestUrl;
            String requestString = bitmapRequest.requestString;
            if (!TextUtils.isEmpty(requestString)) {// 拼接url
                url = url.concat(requestString);
            }
            GetRequest getRequest = OkGo.get(url).cacheKey(url).tag(tag);
            if (httpHeaders != null) {
                getRequest.headers(httpHeaders);
            }
            BitmapCallback bitmapCallback = new BitmapCallback() {
                @Override
                public void onSuccess(Bitmap bitmap, Call call, Response response) {
                    callback.onSuccess(bitmap);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }
            };
            getRequest.execute(bitmapCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求
     *
     * @param xBase    请求实体
     * @param callback 回调
     * @param tag      请求唯一标识
     */
    private static void get(XBase xBase, final Callback callback, Object tag) {
        try {
            XGet xGet = (XGet) xBase;
            HttpHeaders httpHeaders = getHeaders(xBase);
            String url = xGet.requestUrl;
            String requestString = xGet.requestString;
            if (!TextUtils.isEmpty(requestString)) {
                url = url.concat(requestString);
            }
            GetRequest getRequest = OkGo.get(url).cacheKey(url).tag(tag);
            if (httpHeaders != null) {
                getRequest.headers(httpHeaders);
            }
            StringCallback stringCallback = new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Class clzz = TypeTake.getClass(callback);
                    callback.onSuccess(parse(s, clzz));
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }

            };
            getRequest.execute(stringCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post请求
     *
     * @param xBase    请求实体
     * @param callback 回调
     * @param tag      请求唯一标识
     */
    private static void post(XBase xBase, final Callback callback, Object tag) {
        try {
            XPost xPost = (XPost) xBase;
            String url = xPost.requestUrl;
            HttpHeaders httpHeaders = getHeaders(xBase);
            PostRequest postRequest = OkGo.post(url).tag(tag).cacheKey(url);
            if (httpHeaders != null) {
                postRequest.headers(httpHeaders);
            }
            if (xPost.params != null) {
                postRequest.params(getParams(xPost));
            }
            StringCallback stringCallback = new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    callback.onSuccess(parse(s, TypeTake.getClass(callback)));
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }

            };
            postRequest.execute(stringCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * postJson请求
     *
     * @param xBase    请求实体
     * @param callback 回调
     * @param tag      请求唯一标识
     */
    private static void postJson(XBase xBase, final Callback callback, Object tag) {
        try {
            XPost xPost = (XPost) xBase;
            String url = xPost.requestUrl;
            HttpHeaders httpHeaders = getHeaders(xBase);
            PostRequest postRequest = OkGo.post(url).tag(tag).cacheKey(url);
            if (httpHeaders != null) {
                postRequest.headers(httpHeaders);
            }
            if (xPost.params != null) {
                String json = JSON.toJSONString(xPost.params);
                postRequest.upJson(json);
            }
            StringCallback stringCallback = new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    callback.onSuccess(parse(s, TypeTake.getClass(callback)));
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }

            };
            postRequest.execute(stringCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * postString请求
     *
     * @param xBase    请求实体
     * @param callback 回调
     * @param tag      请求唯一标识
     */
    private static void postString(XBase xBase, final Callback callback, Object tag) {
        try {
            XPostString xPost = (XPostString) xBase;
            String url = xPost.requestUrl;
            HttpHeaders httpHeaders = getHeaders(xBase);
            PostRequest postRequest = OkGo.post(url).tag(tag).cacheKey(url);
            if (httpHeaders != null) {
                postRequest.headers(httpHeaders);
            }
            if (TextUtils.isEmpty(xPost.string)) {
                postRequest.upString(xPost.string);
            }
            StringCallback stringCallback = new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    callback.onSuccess(parse(s, TypeTake.getClass(callback)));
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }

            };
            postRequest.execute(stringCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件请求,支持多文件上传,如果需要查看上传进度,请务必要重写{@link Callback}的</br>
     * onUploadProgress(long currentSize, long totalSize, float progress, long networkSpeed)</br>
     * 方法</br>
     *
     * @param xBase    请求实体
     * @param callback 回调
     * @param tag      请求唯一标识
     */
    private static void uploadFile(XBase xBase, final Callback callback, Object tag) {
        try {
            XUploadFile xUploadFile = (XUploadFile) xBase;
            String url = xUploadFile.requestUrl;
            HttpHeaders httpHeaders = getHeaders(xBase);
            PostRequest postRequest = OkGo.post(url).tag(tag).cacheKey(url);
            if (httpHeaders != null) {
                postRequest.headers(httpHeaders);
            }
            if (xUploadFile.params != null) {
                postRequest.params(getParams(xUploadFile));
            }
            if (xUploadFile.files != null) {
                postRequest.params(getFileParams(xUploadFile));
            } else {
                throw new Exception("上传文件不存在|Please Check Your File(s) Is Exist");
            }
            StringCallback stringCallback = new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    callback.onSuccess(parse(s, TypeTake.getClass(callback)));
                }

                @Override
                public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                    super.upProgress(currentSize, totalSize, progress, networkSpeed);
                    callback.onUploadProgress(currentSize, totalSize, progress, networkSpeed);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }

            };
            postRequest.execute(stringCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件请求,支持多文件下载,如果需要查看下载进度,请务必要重写{@link Callback}的</br>
     * onDownloadProgress(long currentSize, long totalSize, float progress, long networkSpeed)</br>
     * 方法,且{@link Callback}的泛型请务必指定为{@link File} </br>
     *
     * @param xBase        请求实体
     * @param callback     回调
     * @param destFileDir  文件存储路径 如果不传,则默认为sdcard/download/
     * @param destFileName 文件名称 如果不传 默认为从url中自动生成
     * @param tag          请求唯一标识
     */
    private static void downloadFile(XBase xBase, String destFileDir, String destFileName, final Callback callback, Object tag) {
        try {
            String url = xBase.requestUrl;
            HttpHeaders httpHeaders = getHeaders(xBase);
            GetRequest getRequest = OkGo.get(url).tag(tag);
            if (httpHeaders != null) {
                getRequest.headers(httpHeaders);
            }
            FileCallback fileCallback = new FileCallback(destFileDir, destFileName) {
                @Override
                public void onSuccess(File file, Call call, Response response) {
                    callback.onSuccess(file);
                }

                @Override
                public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                    super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                    callback.onDownloadProgress(currentSize, totalSize, progress, networkSpeed);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setErrorCode(NetCode.STATE_ERROR);
                    errorInfo.setErrorMessage(NetCode.RESUQST_FAILED);
                    callback.onFailed(errorInfo);
                }

            };
            getRequest.execute(fileCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * json解析
     *
     * @param json
     * @param type
     * @return
     */
    private static Object parse(String json, Class type) {
        try {
            Object obj = JSON.parseObject(json, type);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取headers
     *
     * @param xBase
     * @return
     */
    private static HttpHeaders getHeaders(XBase xBase) {
        try {
            if (xBase.requestHeaders == null) return null;
            return getHeaders(xBase.requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取post参数
     *
     * @param xPost
     * @return
     */
    private static HttpParams getParams(XPost xPost) {
        try {
            if (xPost.params == null) return null;
            HttpParams httpParams = new HttpParams();
            for (String key : xPost.params.keySet()) {
                Object obj = xPost.params.get(key);
                httpParams.put(key, String.valueOf(obj));
            }
            return httpParams;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取headers
     *
     * @param headers
     * @return
     */
    private static HttpHeaders getHeaders(HashMap<String, String> headers) {
        try {
            if (headers == null) return null;
            HttpHeaders httpHeaders = new HttpHeaders();
            for (String key : headers.keySet()) {
                Object obj = headers.get(key);
                httpHeaders.put(key, String.valueOf(obj));
            }
            return httpHeaders;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件上传参数
     *
     * @return
     */
    private static HttpParams getFileParams(XUploadFile xUploadFile) {
        try {
            if (xUploadFile.files == null) return null;
            HttpParams httpParams = new HttpParams();
            for (String key : xUploadFile.files.keySet()) {
                httpParams.putFileParams(key, xUploadFile.files.get(key));
            }
            return httpParams;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

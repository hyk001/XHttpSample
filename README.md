#联系方式
博客地址:<a href="http://blog.csdn.net/qq243223991">http://blog.csdn.net/qq243223991</a><br>
Github地址:<a href="https://github.com/anqiansong/XHttpSample">https://github.com/anqiansong/XHttpSample</a><br>
# XHttpSample
XHttp一个轻量级网络工具类,基于OKGo封装的一个简单、实用的网络请求工具,此工具因继承自OkGo,查看 OkGo便知,不能和OkHttp同时引用, 会导致冲突,在本工具中采用
是fastjson 1.2.9版本进行json解析的,因此在依赖过程中请勿重复依赖fastjson.
#Callback
<pre>
public abstract class Callback&lt;T&gt; {// 根据需要返回的类型指定泛型
    /**
     * 成功返回结果
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 失败返回结果,非必要实现
     *
     * @param errorInfo
     */
    public void onFailed(ErrorInfo errorInfo) {

    }

    /**
     * 上传进度,当需要上传文件时调用
     * @param currentSize 当前已上传的文件大小(单位:kb)
     * @param totalSize 需要上传的文件大小(单位:kb)
     * @param progress 当前上传进度(0-1)
     * @param networkSpeed 当前上传网速(单位秒)
     */
    public void onUploadProgress(long currentSize, long totalSize, float progress, long networkSpeed){

    }
    /**
     * 下载进度,当需要下载文件时调用
     * @param currentSize 当前已上传的文件大小(单位:kb)
     * @param totalSize 需要上传的文件大小(单位:kb)
     * @param progress 当前上传进度(0-1)
     * @param networkSpeed 当前上传网速(单位秒)
     */
    public void onDownloadProgress(long currentSize, long totalSize, float progress, long networkSpeed){

    }
}
</pre>
# 注意事项
在使用XHttp进行网络请求之前,需要调用init(Application)对其进行一次初始化,此 初始化建议在Application中进行,在初始化过程中,对默认配置进行了初始化,如果用户不满足默认参数配置,可以调用对应的方法进行设置, 建议在Application进行全局设置.默认配置中的超时间为15秒, 对Cookie进行了持久化处理.源码没有在继续上传,大家如果需要源码的可以联系我.
#使用方法
#gradle引用,优化上传文件bug
<pre>
compile 'com.android.anqiansong:xhttp:1.0.3'
</pre>
#1.在Application中初始化
<pre>
public class UApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XHttp.init(this);// 初始化
    }
}
</pre>
#2.根据需要进行参数配置 
<pre>
注意 :此过程尽量在Appcation中进行设置
设置超时时间
 setTimeOut(int)单位:秒,默认为15秒
 添加全局headers
 addHeaders(HashMap) 
 设置是否缓存
 setCache(boolean) true:缓存,false: 不缓存
 设置缓存模式
 setCacheMode(CacheMode)
 DEFAULT // 按照HTTP协议的默认缓存规则，例如有304响应头时缓存
 NO_CACHE // 不使用缓存
 REQUEST_FAILED_READ_CACHE// 请求网络失败后，读取缓存
 IF_NONE_CACHE_REQUEST// 如果缓存不存在才请求网络，否则使用缓存
 FIRST_CACHE_THEN_REQUEST //先使用缓存，不管是否存在，仍然请求网络
 设置证书
 setCertificates(InputStream)
 注:如果每个请求的header不一样,亦可以单独设置header,如:
 XGet xGet = new XGet();
 xGet.requestUrl = Constant.JOKE_URL; // 请求地址
 HashMap&lt;String, String&gt; headers = new HashMap&lt;&gt;();
 headers.put("apikey", Constant.APP_KEY);
 xGet.requestHeaders = headers;
 XHttp.request(xGet, new Callback&lt;String&gt;() {
     @Override
     public void onSuccess(String s) {
         Log.d(TAG, s);
     }
 });
</pre>
#3.Get请求
<h5>
获取json字符串
</h5>
<pre>
 XGet xGet = new XGet();
 xGet.requestUrl = Constant.JOKE_URL;// 请求地址
 // 根据需要配置请求参数和headers
 // xGet.requestString = "?page=1";
 // HashMap&lt;String, String&gt; headers = new HashMap&lt;&gt;();
 // headers.put("apikey", Constant.APP_KEY);
 // xGet.requestHeaders = headers;
  XHttp.request(xGet,Callback&lt;String&gt;(){// 这里需要指定泛型为String
     @Override
     public void onSuccess(String s) {
        // TODO dosometing
     }
  })
</pre>
<h5>
获取Bitmap
</h5>
<pre>
XBitmap xBitmap = new XBitmap();
xBitmap.requestUrl = Constant.PIC; // 请求地址
// 根据需要配置请求参数和headers
// xGet.requestString = "?page=1";
// HashMap&lt;String, String&gt; headers = new HashMap&lt;&gt;();
// headers.put("apikey", Constant.APP_KEY);
// xGet.requestHeaders = headers;
XHttp.request(xBitmap, new Callback&lt;Bitmap&gt;() {// 泛型需要指定为Bitmap
    @Override
    public void onSuccess(Bitmap bitmap) {
        iv_image.setImageBitmap(bitmap);
    }
});
</pre>
<h5>
获取实体(即直接返回解析后的数据实体)
</h5>
<pre>
 XGet xGet = new XGet();
 xGet.requestUrl = Constant.JOKE_URL; // 请求地址
 // 根据需要配置请求参数和headers
 // xGet.requestString = "?page=1";
 // HashMap&lt;String, String&gt; headers = new HashMap&lt;&gt;();
 // headers.put("apikey", Constant.APP_KEY);
 // xGet.requestHeaders = headers;
  XHttp.request(xGet,Callback&lt;JokeResponse&gt;(){// 这里需要指定泛型为String
     @Override
     public void onSuccess(JokeResponse response) {// JokeResponse为你的javabean
        // TODO dosometing
     }
  })
</pre>
#4.POST请求
<h5>
基本的post请求,key-value参数,获取json或者实体
</h5>
<pre>
XPost xPost = new XPost();
xPost.requestUrl = Constant.JOKE_URL;// 请求地址
// 根据需要配置
// HashMap&lt;String,String&gt; headers = new HashMap&lt;&gt;();
// headers.put("apikey", Constant.APP_KEY);
// xPost.requestHeaders = headers;
// HashMap&lt;String,Object&gt; params = new HashMap&lt;&gt;();
// params.put("page",1);
// xPost.params = params;
XHttp.request(xPost, new Callback&lt;String&gt;() {// 可以根据需要直接返回未具体的javabean实体,只要将泛型指定为对应的java实体类即可
    @Override
    public void onSuccess(String json) {
        // TDDO dosomething
    }
});
</pre>
<h5>
Post请求,上传json数据
</h5>
<pre>
XPostJson xPost = new XPostJson();
xPost.requestUrl = Constant.JOKE_URL;// 上传地址
// 根据需要配置
// HashMap&lt;String,String&gt; headers = new HashMap&lt;&gt;();
// headers.put("apikey", Constant.APP_KEY);
// xPost.requestHeaders = headers;
// 转换为json,然后作为参数发起post请求
HashMap&lt;String,Object&gt; params = new HashMap&lt;&gt;();
String json = JSON.toJSONString(params);
xPost.requestJson = json;
XHttp.request(xPost, new Callback&lt;String&gt;() {// 可以根据需要直接返回未具体的javabean实体,只要将泛型指定为对应的java实体类即可
    @Override
    public void onSuccess(String json) {
        // TDDO dosomething
    }
});
</pre>
<h5>
Post请求,上传String数据
</h5>
<pre>
XPostString xPost = new XPostString();
xPost.requestUrl = Constant.JOKE_URL;//  上传地址
// 根据需要配置
// HashMap&lt;String,String&gt; headers = new HashMap&lt;&gt;();
// headers.put("apikey", Constant.APP_KEY);
// xPost.requestHeaders = headers;
// 转换为json,然后作为参数发起post请求
String data = "待上传的字符串";
xPost.string = data;
XHttp.request(xPost, new Callback&lt;String&gt;() {// 可以根据需要直接返回未具体的javabean实体,只要将泛型指定为对应的java实体类即可
    @Override
    public void onSuccess(String json) {
        // TDDO dosomething
    }
});
</pre>
#5.上传文件
说明:支持多文件上传,需要重写onUploadProgress方法
<pre>
XUploadFile xUploadFile = new XUploadFile();
xUploadFile.requestUrl = Constant.JOKE_URL;//  上传地址
// 根据需要配置
// HashMap&lt;String,String&gt; headers = new HashMap&lt;&gt;();
// headers.put("apikey", Constant.APP_KEY);
// xUploadFile.requestHeaders = headers;
// HashMap&lt;String,Object&gt; params = new HashMap&lt;&gt;();
// params.put("page",1);
// xUploadFile.params = params;
File file = getFile();// 选择文件
List&lt;File&gt; files = new ArrayList&lt;&gt;();
files.add(file);
HashMap&lt;String,&lt;List&lt;File&gt;&gt;&gt; filesData = new HashMap&lt;&gt;();
xUploadFile.files = filesData;
XHttp.request(xUploadFile, new Callback&lt;String&gt;() {
    @Override
    public void onSuccess(String s) {
        // TODO dosometing
    }
    @Override
    public void onUploadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.onUploadProgress(currentSize, totalSize, progress, networkSpeed);
        // TODO dosomething
    }
});
</pre>
#6.下载文件
注:如果需要查看下载进度务必要重写onDownloadProgress方法
<pre>
XDownloadFile xDownloadFile = new XDownloadFile();
xDownloadFile.requestUrl = Constant.JOKE_URL;// 下载地址
// 根据需要配置
// HashMap&lt;String,String&gt; headers = new HashMap&lt;&gt;();
// headers.put("apikey", Constant.APP_KEY);
// xDownloadFile.requestHeaders = headers;
XHttp.download(xDownloadFile,"/SD/download","meituan.apk", new Callback&lt;String&gt;() {
    @Override
    public void onSuccess(String s) {
        // TODO dosometing
    }

    @Override
    public void onDownloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.onDownloadProgress(currentSize, totalSize, progress, networkSpeed);
        // TODO dosometing
    }
});
</pre>

#后序
感谢<h2>jeasonlzy</h2><br>
引自:<a href="https://github.com/jeasonlzy/okhttp-OkGo">jeasonlzy github地址</a><br>
如果有错误,希望大家指出<br>
邮箱:anqiansong@immiker.com<br>

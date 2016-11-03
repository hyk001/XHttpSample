# XHttpSample
XHttp一个轻量级网络工具类,基于OKGo封装的一个简单、实用的网络请求工具,此工具因继承自OkGo,查看 OkGo便知,不能和OkHttp同时引用, 会导致冲突,在本工具中采用
是fastjson 1.2.9版本进行json解析的,因此在依赖过程中请勿重复依赖fastjson.
# 注意事项
在使用XHttp进行网络请求之前,需要调用init(Application)对其进行一次初始化,此 初始化建议在Application中进行,在初始化过程中,对默认配置进行了初始化,如果用户不满足默认参数配置,可以调用对应的方法进行设置, 建议在Application进行全局设置.默认配置中的超时间为15秒, 对Cookie进行了持久化处理.
#使用方法
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
#2.根据需要进行参数配置, 
<pre>
注意 :此过程尽量在Appcation中进行设置
设置超时时间
 setTimeOut(int)单位:秒,默认为15秒
 添加全局headers
 addHeaders(HashMap<String,String>) 
 设置是否缓存
 setCache(boolean) true:缓存,false: 不缓存
 设置缓存模式
 setCacheMode(CacheMode)
 DEFAULT //按照HTTP协议的默认缓存规则，例如有304响应头时缓存
 NO_CACHE //不使用缓存
 REQUEST_FAILED_READ_CACHE// 请求网络失败后，读取缓存
 IF_NONE_CACHE_REQUEST// 如果缓存不存在才请求网络，否则使用缓存
 FIRST_CACHE_THEN_REQUEST //先使用缓存，不管是否存在，仍然请求网络
 设置证书
 setCertificates(InputStream)
 </pre>
 

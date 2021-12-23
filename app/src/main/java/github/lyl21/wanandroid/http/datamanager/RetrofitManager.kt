package github.lyl21.wanandroid.http.datamanager

import android.text.TextUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap
import java.util.concurrent.TimeUnit

class RetrofitManager {
    private val mRetrofitMap: MutableMap<String?, Retrofit>
    private fun getOkHttpClient(headerMap: Map<String, String>?): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (headerMap != null) {
            builder.addInterceptor(Interceptor { chain ->
                LogUtils.i(TAG, "getOkHttpClient==intercept==")
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                for ((key, value) in headerMap) {
                    requestBuilder.addHeader(key, value)
                }
                chain.proceed(requestBuilder.build())
            })
        }
        builder.callTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        builder.readTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        builder.writeTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.MINUTES)
        return builder.build()
    }

    /**
     * 利用静态内部类特性实现外部类的单例
     * 原理：Java中静态内部类可以访问其外部类的成员属性和方法，同时，静态内部类只有当被调用的时候才开始首次被加载，
     * 利用此特性，可以实现懒汉式，在静态内部类中静态初始化外部类的单一实例即可。
     */
    private object SingletonHolder {
        var mInstance: RetrofitManager = RetrofitManager()
    }

    /**
     * 获取单例类
     * @return
     */
    fun getInstance(): RetrofitManager {
        return SingletonHolder.mInstance
    }


    /**
     * 生成service对象
     * @param url IP地址
     * @param service
     * @param <T>
     * @return
    </T> */
    fun <T> create(url: String?, service: Class<T>?): T? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        if (!mRetrofitMap.containsKey(url)) {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .client(getOkHttpClient(null))
                .addConverterFactory(GsonConverterFactory.create(gson)) //设置数据解析的工具
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build()
            mRetrofitMap[url] = retrofit
        }
        return mRetrofitMap[url]!!.create(service)
    }

    fun <T> create(url: String?, service: Class<T>?, headerMap: Map<String, String>?): T? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        val gson = GsonBuilder().setLenient().create()
        val mRetrofit = Retrofit.Builder()
            .client(getOkHttpClient(headerMap))
            .addConverterFactory(GsonConverterFactory.create(gson)) //设置数据解析的工具
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .build()
        return mRetrofit.create(service)
    }

    companion object {
        private const val TAG = "RetrofitManager"
        private const val DEFAULT_TIME_OUT = 20 //5s
        private const val DEFAULT_READ_TIME_OUT = 10 //10s
    }

    init {
        mRetrofitMap = HashMap()
    }
}

import github.lyl21.wanandroid.common.ConstantParam.Companion.BingBaseUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by yechao on 2020/1/8/008.
 * Describe :
 */
object BingRetrofitService {

    val create: API.WanAndroidApi by lazy {
        Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
        )
        .baseUrl(BingBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API.WanAndroidApi::class.java)
    }


}
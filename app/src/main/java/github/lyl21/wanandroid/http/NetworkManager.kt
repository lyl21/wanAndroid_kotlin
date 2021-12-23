import github.lyl21.wanandroid.http.SSLSocketClient
import github.lyl21.wanandroid.http.config.LocalCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by yechao on 2020/1/8/008.
 * Describe :
 */
object NetworkManager {

    val create: API.WanAndroidApi by lazy {
        Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .callTimeout(5, TimeUnit.SECONDS)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .followRedirects(false)
                    .cookieJar(LocalCookieJar())
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .sslSocketFactory(SSLSocketClient.sSLSocketFactory,SSLSocketClient.sSLtrustManager[0])
                    .hostnameVerifier(SSLSocketClient.hostnameVerifier)
//                    .addInterceptor(AddCookiesInterceptor)
//                    .addInterceptor(ReceivedCookiesInterceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API.WanAndroidApi::class.java)
    }

}
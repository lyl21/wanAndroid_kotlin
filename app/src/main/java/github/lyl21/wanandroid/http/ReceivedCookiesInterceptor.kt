import com.blankj.utilcode.util.SPUtils
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by yechao on 2020/3/10/010.
 * Describe : 从响应头里拿到cookie并存起来，后面的每次请求再添加到请求头里
 */
object ReceivedCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
           SPUtils.getInstance().put("cookie", cookies)
        }
        return originalResponse
    }
}
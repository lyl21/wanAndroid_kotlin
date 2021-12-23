import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *
 * @author    popcomimico
 * @date    2021/9/29 17:05
 * Describe : 从响应头里拿到cookie并存起来，后面的每次请求再添加到请求头里
 */
object AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val stringSet = SPUtils.getInstance().getStringSet("cookie")
        for (cookie in stringSet) {
            builder.addHeader("Cookie", cookie.toString())
//            LogUtils.i("Adding Header: $cookie")
        }
        return chain.proceed(builder.build())
    }
}
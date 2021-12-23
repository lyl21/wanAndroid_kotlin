package github.lyl21.wanandroid.http

import java.lang.Exception
import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


object SSLSocketClient {
    //获取这个SSLSocketFactory
    val sSLSocketFactory: SSLSocketFactory
        get() = try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManager, SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    val sSLtrustManager: Array<X509TrustManager> = trustManager
    //获取TrustManager
    private val trustManager: Array<X509TrustManager>
        get() {
            return arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
        }

    //获取HostnameVerifier
    val hostnameVerifier: HostnameVerifier
        get() {
            return object : HostnameVerifier {
                override fun verify(s: String, sslSession: SSLSession): Boolean {
                    return true
                }
            }
        }
}
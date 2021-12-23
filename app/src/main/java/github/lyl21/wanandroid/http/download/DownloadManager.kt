package github.lyl21.wanandroid.http.download


import android.util.Log
import github.lyl21.wanandroid.http.datamanager.DownloadListener
import github.lyl21.wanandroid.util.StreamUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import kotlinx.coroutines.flow.catch
import okhttp3.Request
import java.io.File
import java.io.IOException
import github.lyl21.wanandroid.util.copyTo

object DownloadManager {

    fun downloadFile(
        downloadUrl: String,
        saveFilePath: String,
        listener: DownloadListener
    ): Flow<String> {
        return flow {
            val request = Request.Builder().url(downloadUrl).get().build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()
            if (response.isSuccessful) {
                response.body!!.let { body ->
                    StreamUtil.writeStreamToFileWithListener(
                        body.byteStream(),
                        saveFilePath,
                        body.contentLength(),
                        listener
                    )
                    emit(saveFilePath)
                }
            } else {
                throw IOException(response.toString())
            }
        }.catch {
//            file.delete()
//            emit(DownloadStatus.Err(it))
            Log.e("lll","downloadFile  catch" )
        }
    }

    


}


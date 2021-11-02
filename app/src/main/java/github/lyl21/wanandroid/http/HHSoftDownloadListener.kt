package github.lyl21.wanandroid.http

interface HHSoftDownloadListener {

    fun onProgress(progress: Int)

    fun onCompleted()

    fun onError(msg: String?)
}
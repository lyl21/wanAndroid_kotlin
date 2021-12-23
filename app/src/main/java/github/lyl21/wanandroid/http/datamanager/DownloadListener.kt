package github.lyl21.wanandroid.http.datamanager

interface DownloadListener {
    fun onProgress(progress: Int)
    fun onCompleted()
    fun onError(msg: String)
}
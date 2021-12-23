package github.lyl21.wanandroid.update

import java.io.File

interface IDownloadCallback {
    /**
     * 下载成功，在此处理
     * @param apkFile
     */
    fun onSuccess(apkFile: File?)

    /**
     * 下载进度，在此处理
     * @param progress
     */
    fun progress(progress: Int)

    /**
     * 下载失败，在此处理
     * @param throwable
     */
    fun onFailure(throwable: Throwable?)
}

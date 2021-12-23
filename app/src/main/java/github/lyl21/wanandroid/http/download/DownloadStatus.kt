package github.lyl21.wanandroid.http.download

import java.io.File

/**
 * 下载状态
 */
sealed class DownloadStatus {
    data class Progress(val progress: Int) : DownloadStatus()
    data class Err(val t: Throwable) : DownloadStatus()
    data class Done(val file: File) : DownloadStatus()
}
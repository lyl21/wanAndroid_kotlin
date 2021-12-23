package github.lyl21.wanandroid.update

import java.io.File

interface INetManager {
    /**
     * 发起请求
     *
     * @param url         地址
     * @param netCallback 处理返回的结果
     * @param tag         标识当前的请求
     */
    operator fun get(url: String?, netCallback: INetCallback?, tag: Any?)

    /**
     * 下载
     *
     * @param url              资源地址
     * @param targetFile       保存到：targetFile
     * @param downloadCallback 下载结果回调
     * @param tag              标识当前的下载请求
     */
    fun download(url: String?, targetFile: File?, downloadCallback: IDownloadCallback?, tag: Any?)

    /**
     * 取消数据请求
     *
     * @param tag 标识要取消的请求
     */
    fun cancel(tag: Any?)
}
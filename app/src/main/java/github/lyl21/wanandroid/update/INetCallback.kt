package github.lyl21.wanandroid.update

interface INetCallback {
    /**
     * 请求成功，再此进行处理
     * @param response
     */
    fun onSuccess(response: String?)

    /**
     * 请求失败，在此进行处理
     * @param throwable
     */
    fun onFailed(throwable: Throwable?)
}
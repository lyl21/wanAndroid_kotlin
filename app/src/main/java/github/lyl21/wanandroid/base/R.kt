package github.lyl21.wanandroid.base

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 23:53
 */
data class Response<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
)

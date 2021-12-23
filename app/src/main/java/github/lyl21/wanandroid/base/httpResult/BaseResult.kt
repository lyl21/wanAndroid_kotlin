package github.lyl21.wanandroid.base.httpResult

/**
 * @author    popcomimico
 * @date    2021/9/28 23:53
 */
data class BaseResult<T>(
    var errorMsg: String = "",
    var errorCode: Int = 0,
    var data: T? = null,
    var dataState: DataState? = null,
    var errException: Throwable? = null
)

enum class DataState {
//    CREATE,//创建
    LOADING,//加载中
    SUCCESS,//成功
    COMPLETED,//完成
    EMPTY,//数据为null
//    ERROR,//接口请求成功但是服务器返回error
    FAILED,//请求失败
    UNKNOWN//未知
}
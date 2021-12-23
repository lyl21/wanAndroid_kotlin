package github.lyl21.wanandroid.base.ui

import API
import BingRetrofitService.create
import NetworkManager
import android.net.ParseException
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.httpResult.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.HttpException
import java.io.EOFException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

open class BaseVM : ViewModel() {

    var request: API.WanAndroidApi = NetworkManager.create

    /**
     * 根据Flow的不同请求状态，如onStart、onEmpty、onCompletion等设置baseResult.dataState状态值，
     * 最后通过stateLiveData分发给UI层。
     *
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    fun <T> requestWithLoadingFlow(
        block: suspend () -> BaseResult<T>,
        stateFlow: MutableLiveData<BaseResult<T>>
    ) {
        var baseResult = BaseResult<T>()
        viewModelScope.launch {
            try {
                flow {
                    val respResult = block.invoke()
                    baseResult = respResult
                    LogUtils.d("BaseVM", "executeReqWithFlow: ${baseResult.errorCode}")
                    baseResult.dataState = DataState.SUCCESS
                    stateFlow.value =baseResult
                    emit(respResult)
                }
                    .onStart {
                        LogUtils.d("BaseVM", "requestWithLoadingFlow:onStart")
                        baseResult.dataState = DataState.LOADING
                        stateFlow.value = baseResult
                    }
                    .onEmpty {
                        LogUtils.d("BaseVM", "requestWithLoadingFlow:onEmpty")
                        baseResult.dataState = DataState.EMPTY
                        stateFlow.value =baseResult
                    }
                    .catch {
                        LogUtils.d("BaseVM", "requestWithLoadingFlow:code")
                        when (it) {
                            is HttpException,
                            is EOFException,
                            is SocketException,
                            is TimeoutException,
                            is ConnectException,
                            is UnknownHostException,
                            is InterruptedIOException,
                            is SocketTimeoutException -> {
//                                ToastUtils.showShort("网络链接超时")
                                LogUtils.e(
                                    "BaseVM",
                                    "网络链接超时" + baseResult.errorCode + "," + it.message
                                )

                                baseResult.dataState = DataState.FAILED
                                baseResult.errException = it
                                stateFlow.value = baseResult
                            }
                            is SSLHandshakeException -> {
//                                ToastUtils.showShort("网络链接错误")
                                LogUtils.e(
                                    "BaseVM",
                                    "网络链接错误" + block.invoke().errorCode + "," + it.message
                                )
                            }
                            is ParseException,
                            is JSONException,
                            is JsonSyntaxException,
                            is JsonParseException,
                            -> {
//                                ToastUtils.showShort("网络异常，请稍后再试")
                                LogUtils.e(
                                    "BaseVM",
                                    "Json 解析出错" + block.invoke().errorCode + "," + it.message
                                )
                            }
                            else -> {
//                                ToastUtils.showShort("未知错误")
                                LogUtils.e(
                                    "BaseVM",
                                    "未知错误" + block.invoke().errorCode + "," + it.message
                                )
                            }
                        }
                        baseResult.dataState = DataState.FAILED
                        baseResult.errException = it
                        stateFlow.value = baseResult
                    }
                    .onCompletion {
                        LogUtils.d("BaseVM", "requestWithLoadingFlow:onCompletion")
                        baseResult.dataState = DataState.COMPLETED
                        stateFlow.value = baseResult
                    }
                    .collect {
                        LogUtils.d("BaseVM", "requestWithLoadingFlow: collect")
                        stateFlow.value = baseResult
                    }
            } catch (e: Exception) {
                LogUtils.e("lll", e)
            }
        }
    }


    /**
     * 结合Flow请求数据。
     * 根据Flow的不同请求状态，如onStart、onEmpty、onCompletion等设置baseResult.dataState状态值，
     * 最后通过stateLiveData分发给UI层。
     *
     * @param block api的请求方法
     * @param stateLiveData 每个请求传入相应的LiveData，主要负责网络状态的监听
     */
    fun <T> requestWithFlow(
        block: suspend () -> BaseResult<T>,
        stateFlow: MutableStateFlow<BaseResult<T>>
    ) {
        var baseResult = BaseResult<T>()
        viewModelScope.launch {
            flow {
                val respResult = block.invoke()
                baseResult = respResult
                Log.d("lll", "requestWithLoadingFlow: $baseResult")
                baseResult.dataState = DataState.SUCCESS
                stateFlow.value = baseResult
                emit(respResult)
            }
                .onStart {
                    Log.d("lll", "requestWithLoadingFlow:onStart")
                    baseResult.dataState = DataState.LOADING
                    stateFlow.value = baseResult
                }
                .onEmpty {
                    Log.d("lll", "requestWithLoadingFlow:onEmpty")
                    baseResult.dataState = DataState.EMPTY
                    stateFlow.value = baseResult
                }
                .onCompletion {
                    Log.e("lll", "requestWithLoadingFlow:onCompletion")
//                    baseResult.dataState = DataState.COMPLETED
                    stateFlow.value = baseResult
                }
                .catch { exception ->
                    run {
                        Log.d("lll", "requestWithLoadingFlow:code  ${baseResult.errorCode}")
                        exception.printStackTrace()
//                        baseResult.dataState = DataState.ERROR
                        baseResult.errException = exception
                        stateFlow.value = baseResult
                    }
                }
                .collect {
                    Log.d("lll", "requestWithLoadingFlow: collect")
                    stateFlow.value = baseResult
                }
        }
    }


}
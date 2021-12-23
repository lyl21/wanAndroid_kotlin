package github.lyl21.wanandroid.moudle.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.BingImgInfo
import github.lyl21.wanandroid.common.ConstantParam
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class SplashVM : BaseVM() {

    val getTodayBingImg = MutableLiveData<String>()

    fun getTodayBingImg(url: String) {
        viewModelScope.launch {
            flow {
                emit(BingRetrofitService.create.getTodayBingImg(url))
            }.catch {
                it.printStackTrace()
            }
                .collect {
                    val gson = GsonBuilder().enableComplexMapKeySerialization().create()
                    val jsonString = gson.toJson(it.body())
                    val fromJson = gson.fromJson(jsonString, BingImgInfo::class.java)
                    val imgUrl = fromJson.images[0].url

                    getTodayBingImg.postValue(ConstantParam.BingBaseUrl + imgUrl)
                }
        }
//        requestWithLoadingFlow({ request.getTodayBingImg(url) }, getTodayBingImg)
    }

    /* fun getTodayBingImg(url:String) {
         BingRetrofitService.create
             .getTodayBingImg(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<Any>> {
                override fun onSubscribe(d: Disposable) {
//                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: retrofit2.Response<Any>) {
//                    LogUtils.e("onNext")
                    val gson = GsonBuilder().enableComplexMapKeySerialization().create()
                    val jsonString = gson.toJson(t.body())
                    val fromJson = gson.fromJson(jsonString, BingImgInfo::class.java)
                    val imgUrl= ConstantParam.BingBaseUrl+fromJson.images[0].url
                    LogUtils.e("lll",imgUrl)
//                    splashView.getTodayBingImg(imgUrl)
                    getTodayBingImg.postValue(imgUrl)
                    //保存今日图片
                    MMKV.defaultMMKV().encode("getTodayBingImg",imgUrl)
                }

                override fun onError(e: Throwable) {
//                    LogUtils.e("onError")
//                    splashView.getTodayBingImgError(e.message+"")
                }

                override fun onComplete() {
//                    LogUtils.e("onComplete")
                }
            })

    }*/

}
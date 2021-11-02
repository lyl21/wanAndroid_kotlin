package github.lyl21.wanandroid.moudle.navi

import com.blankj.utilcode.util.LogUtils
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.NaviInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class NaviPresenter(private var naviView: NaviView) {


    fun getNaviInfo() {
        RetrofitService
            .getApiService()
            .getNavi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<MutableList<NaviInfo>>> {
                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<MutableList<NaviInfo>>) {
                    LogUtils.e("onNext")
                    naviView.getNavi(t)
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    naviView.getNaviError("获取失败" + e.message)
                }

                override fun onComplete() {
                    LogUtils.e("onComplete")
                }

            })
    }


}



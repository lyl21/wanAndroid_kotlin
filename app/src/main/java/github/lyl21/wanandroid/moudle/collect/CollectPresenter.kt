package github.lyl21.wanandroid.moudle.collect

import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.CollectInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CollectPresenter(private var collectView:CollectView) {

    fun unMyCollect(id:Int,originId:Int){
        RetrofitService
            .getApiService()
            .unMyCollection(id,originId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<String>>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<String>) {
                    ToastUtils.showLong(t.errorMsg)
                    collectView.unCollect("取消收藏成功")
                }

                override fun onError(e: Throwable) {
                    ToastUtils.showLong(e.message+"")
                }

                override fun onComplete() {
                }

            })
    }

    fun getCollectList(page:Int){
        RetrofitService
            .getApiService()
            .getCollectList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<CollectInfo>>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<CollectInfo>) {
                        collectView.getCollectList(t)
                }

                override fun onError(e: Throwable) {
                    collectView.getCollectListError(e.message+"")
                }

                override fun onComplete() {
                }

            })
    }
    fun getCollectMoreList(page:Int){
        RetrofitService
            .getApiService()
            .getCollectList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<CollectInfo>>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Response<CollectInfo>) {
                        collectView.getCollectMoreList(t)
                }

                override fun onError(e: Throwable) {
                    collectView.getCollectMoreListError(e.message+"")
                }

                override fun onComplete() {
                }

            })
    }

}
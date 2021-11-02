package github.lyl21.wanandroid.moudle.search

import com.blankj.utilcode.util.LogUtils
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.HotSearchInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchPresenter(private var searchView: SearchView) {

    fun getHotSearchInfo(){
        RetrofitService
            .getApiService()
            .getHotkey()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<MutableList<HotSearchInfo>>>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<MutableList<HotSearchInfo>>) {
                    searchView.getHotkey(t)
                }

                override fun onError(e: Throwable) {
                    searchView.getHotkeyError("获取失败" + e.message)
                }

                override fun onComplete() {
                }

            })
    }

    fun getArticleInfoList(page: Int, key: String) {

        RetrofitService.getApiService()
            .getSearchList(page, key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ArticleInfo>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<ArticleInfo>) {
                    searchView.getArticleList(t)
                }

                override fun onError(e: Throwable) {
                    searchView.getArticleError("获取失败" + e.message)
                }
            })
    }

    fun getArticleInfoMoreList(page: Int, key: String) {

        RetrofitService.getApiService()
            .getSearchList(page, key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ArticleInfo>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<ArticleInfo>) {
                    searchView.getArticleMoreList(t)
                }

                override fun onError(e: Throwable) {
                    searchView.getArticleMoreError("获取失败" + e.message)
                }
            })
    }

    fun collect(id: Int) {
        RetrofitService.getApiService()
            .toCollection(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<String>> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Response<String>) {
                    if (-1001 == t.errorCode) {
                        searchView.login(t.errorMsg )
                    } else {
                        searchView.collect("收藏成功")
                    }
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                }
            })
    }

    fun unCollect(id: Int) {
        RetrofitService.getApiService()
            .unCollection(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<String>> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Response<String>) {
                    searchView.unCollect("取消成功")
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                }
            })
    }

}
package github.lyl21.wanandroid.moudle.home

import HomeView
import com.blankj.utilcode.util.LogUtils
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.IndexBannerInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 13:21
 */
class HomePresenter( private var mHomeView: HomeView ) {


    /*获取首页banner图*/
    fun getBanner() {
        RetrofitService
            .getApiService()
            .getBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<MutableList<IndexBannerInfo>>> {
                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<MutableList<IndexBannerInfo>>) {
                    LogUtils.e("onNext")
                    mHomeView.getBanner(t);
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    mHomeView.getBannerError("获取失败！" + e.message)
                }

                override fun onComplete() {
                    LogUtils.e("onComplete")
                }


            })

    }

    /*获取文章列表*/
    fun getArticleList(page: Int) {
        RetrofitService.getApiService()
            .getArticleList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ArticleInfo>> {
                override fun onComplete() {
                    LogUtils.e("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<ArticleInfo>) {
                    LogUtils.e("onNext")
                    mHomeView.getArticleList(t)
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    mHomeView.getArticleError("获取失败" + e.message)
                }
            })
    }

    fun getArticleMoreList(page: Int) {
        RetrofitService.getApiService()
            .getArticleList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ArticleInfo>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<ArticleInfo>) {
                    mHomeView.getArticleMoreList(t)
                }

                override fun onError(e: Throwable) {
                    mHomeView.getArticleMoreError("获取失败" + e.message)
                }
            })
    }
    /*收藏文章*/
    fun toCollection(id: Int) {
        RetrofitService
            .getApiService()
            .toCollection(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<String>> {
                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<String>) {
                    LogUtils.e("onNext")
                    if (-1001 == t.errorCode) {
                        mHomeView.login(t.errorMsg + "")
                    } else {
                        mHomeView.toCollection("收藏成功 ")
                    }
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                }

                override fun onComplete() {
                    LogUtils.e("onComplete")
                }
            })
    }


    /*取消收藏*/
    fun unCollection(id: Int) {
        RetrofitService
            .getApiService()
            .unCollection(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<String>> {
                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<String>) {
                    LogUtils.e("onNext")
                    mHomeView.unCollection("取消收藏成功")
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                }

                override fun onComplete() {
                    LogUtils.e("onComplete")
                }
            })
    }

}





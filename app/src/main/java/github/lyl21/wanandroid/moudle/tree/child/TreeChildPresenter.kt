package github.lyl21.wanandroid.moudle.tree.child

import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo
import com.yechaoa.yutilskt.LogUtil
import com.yechaoa.yutilskt.ToastUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TreeChildPresenter(private var treeChildView: TreeChildView) {

    fun getTreeChild(page: Int, cid: Int) {
        RetrofitService
            .getApiService()
            .getTreeChild(page, cid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ArticleInfo>> {
                override fun onSubscribe(d: Disposable) {
                    LogUtil.e("onSubscribe")
                }

                override fun onNext(t: Response<ArticleInfo>) {
                    treeChildView.getTreeChild(t)
                }

                override fun onError(e: Throwable) {
                    ToastUtil.show("onError")
                    treeChildView.getTreeChildError("获取失败！" + e.message)
                }

                override fun onComplete() {
                    LogUtil.e("onComplete")
                }
            })
    }

    fun getTreeMoreChild(page: Int, cid: Int) {
        RetrofitService
            .getApiService()
            .getTreeChild(page, cid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ArticleInfo>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<ArticleInfo>) {
                    treeChildView.getTreeMoreChild(t)
                }

                override fun onError(e: Throwable) {
                    ToastUtil.show("onError")
                    treeChildView.getTreeChildMoreError("获取失败" + e.message)
                }

                override fun onComplete() {
                }
            })
    }

    fun collect(id: Int) {
        RetrofitService
            .getApiService()
            .toCollection(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<String>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<String>) {
                    if (-1001 == t.errorCode) {
                        treeChildView.login(t.errorMsg)
                    } else {
                        treeChildView.collect("收藏成功")
                    }
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
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
                    treeChildView.unCollect("取消收藏成功")
                }

                override fun onError(e: Throwable) {
                    ToastUtil.show(e.message+"")
                }
            })
    }
}
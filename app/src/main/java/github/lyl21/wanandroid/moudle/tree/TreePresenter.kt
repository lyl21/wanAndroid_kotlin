package github.lyl21.wanandroid.moudle.tree

import BasePresenter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.SysTree
import com.yechaoa.yutilskt.LogUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TreePresenter(treeView: TreeView) {

    private var mTreeView: TreeView = treeView

    fun getTree() {
        RetrofitService
            .getApiService()
            .getTree()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<MutableList<SysTree>>> {
                override fun onSubscribe(d: Disposable) {
                    LogUtil.e("onSubscribe")
                }

                override fun onNext(t: Response<MutableList<SysTree>>) {
                    LogUtil.e("onNext")
                    mTreeView.getTree(t)
                }

                override fun onError(e: Throwable) {
                    LogUtil.e("onError")
                    mTreeView.getTreeError("获取失败" +e.message)
                }

                override fun onComplete() {
                    LogUtil.e("onComplete")
                }

            })

    }
}
package github.lyl21.wanandroid.moudle.project.child

import com.blankj.utilcode.util.LogUtils
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ProjectClassInfoChild
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProjectChildPresenter(private var mProjectChildView: ProjectChildView) {

    fun getProjectChildInfo(page:Int,cid:Int){
        RetrofitService
            .getApiService()
            .getProjectChild(page,cid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<ProjectClassInfoChild>>{
                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<ProjectClassInfoChild>) {
                    LogUtils.e("onNext")
                    mProjectChildView.getProjectChild(t)
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    mProjectChildView.getProjectChildError("获取失败" + e.message)
                }

                override fun onComplete() {
                    LogUtils.e("onComplete")
                }
            })
    }

    fun getProjectChildMore(page: Int,cid: Int){
        RetrofitService
            .getApiService()
            .getProjectChild(page, cid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<ProjectClassInfoChild>>{
                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<ProjectClassInfoChild>) {
                    LogUtils.e("onNext")
                    mProjectChildView.getProjectChildMore(t)
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    mProjectChildView.getProjectChildMoreError("获取失败" + e.message)
                }

                override fun onComplete() {
                    LogUtils.e("onComplete")
                }

            })
    }

}
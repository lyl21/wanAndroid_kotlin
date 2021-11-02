package github.lyl21.wanandroid.moudle.project

import BasePresenter
import com.blankj.utilcode.util.LogUtils
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ProjectClassInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProjectPresenter(private var mProjectView: ProjectView) {
    fun getProjectInfo() {
            RetrofitService
                .getApiService()
                .getProject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :Observer<Response<MutableList<ProjectClassInfo>>>{
                    override fun onSubscribe(d: Disposable) {
                        LogUtils.e("onSubscribe")
                    }

                    override fun onNext(t: Response<MutableList<ProjectClassInfo>>) {
                        LogUtils.e("onNext")
                        mProjectView.getProject(t)
                    }

                    override fun onError(e: Throwable) {
                        LogUtils.e("onError")
                        mProjectView.getProjectError("获取失败" + e.message)
                    }

                    override fun onComplete() {
                        LogUtils.e("onComplete")
                    }

                })
    }
}
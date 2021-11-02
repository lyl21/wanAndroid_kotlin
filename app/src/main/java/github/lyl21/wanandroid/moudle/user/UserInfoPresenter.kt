package github.lyl21.wanandroid.moudle.user

import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.UserDataInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserInfoPresenter(private var userInfoView:UserInfoView) {
    fun getUserInfo(username:String){
        RetrofitService
            .getApiService()
            .getUserInfo(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<Response<UserDataInfo>>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Response<UserDataInfo>) {
                    if (-1001==t.errorCode){
                        userInfoView.login(t.errorMsg)
                    }else{
                        userInfoView.getUserInfo(t)
                    }
                }

                override fun onError(e: Throwable) {
                    userInfoView.getUserInfoErr(e.message+"")
                }

                override fun onComplete() {
                }

            })
    }
}
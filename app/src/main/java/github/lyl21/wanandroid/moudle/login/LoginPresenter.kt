package github.lyl21.wanandroid.moudle.login

import com.blankj.utilcode.util.LogUtils
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.UserInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter(  private var loginView: LoginView ) {

    fun login(username: String, password: String) {
        RetrofitService.getApiService()
            .login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<UserInfo>> {
                override fun onComplete() {
                    LogUtils.e("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<UserInfo>) {
                    LogUtils.e("onNext")
                    if (-1 == t.errorCode) {
                        loginView.showLoginFailed("登录失败" + t.errorMsg)
                    } else {
                        loginView.showLoginSuccess("登录成功")
                        //保存用户名
                        MMKV.defaultMMKV().encode("username",username)
                        loginView.doSuccess(t)
                    }
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    loginView.showLoginFailed("登录失败" + e.message)
                }

            })

    }

    fun logout() {
        RetrofitService.getApiService()
            .logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<String>> {
                override fun onComplete() {
                    LogUtils.e("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    LogUtils.e("onSubscribe")
                }

                override fun onNext(t: Response<String>) {
                    LogUtils.e("onNext")
                    loginView.showLogoutSuccess(t.errorMsg)
                }

                override fun onError(e: Throwable) {
                    LogUtils.e("onError")
                    loginView.showLoginFailed("登出失败" + e.message)
                }

            })

    }

}
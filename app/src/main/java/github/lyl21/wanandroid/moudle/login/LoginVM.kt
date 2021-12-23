package github.lyl21.wanandroid.moudle.login

import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.UserInfo

class LoginVM:BaseVM() {

    var toLogin: MutableLiveData<BaseResult<UserInfo>> = MutableLiveData()
    var toLogout: MutableLiveData<BaseResult<String>> = MutableLiveData()

    fun login(username: String, password: String) {
        requestWithLoadingFlow({request.login(username,password)},toLogin)
    }

    fun toLogout() {
        requestWithLoadingFlow({request.logout()},toLogout)
    }

}
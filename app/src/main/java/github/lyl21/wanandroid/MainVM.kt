package github.lyl21.wanandroid

import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.UserDataInfo

class MainVM:BaseVM() {
    var getUserInfo: MutableLiveData<BaseResult<UserDataInfo>> =
        MutableLiveData()
    var toLogout: MutableLiveData<BaseResult<String>> = MutableLiveData()


    fun getUserInfo(username:String){
        requestWithLoadingFlow({request.getUserInfo(username)},getUserInfo)
    }
    fun toLogout() {
        requestWithLoadingFlow({request.logout()},toLogout)
    }
}
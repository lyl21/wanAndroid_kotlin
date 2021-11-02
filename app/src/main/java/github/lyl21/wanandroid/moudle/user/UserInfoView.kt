package github.lyl21.wanandroid.moudle.user

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.HotSearchInfo
import github.lyl21.wanandroid.entity.UserDataInfo

interface UserInfoView : BaseView {
    fun getUserInfo(userInfo: Response<UserDataInfo>)
    fun getUserInfoErr(msg:String)
    fun login(msg: String)
}
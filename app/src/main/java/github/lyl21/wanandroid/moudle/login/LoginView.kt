package github.lyl21.wanandroid.moudle.login

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.UserInfo

interface LoginView:BaseView {

    fun showLoginSuccess(msg: String)

    fun showLoginFailed(msg: String)

    fun doSuccess(user: Response<UserInfo>)

    fun showLogoutSuccess(msg: String)

    fun showLogoutFailed(msg: String)

}
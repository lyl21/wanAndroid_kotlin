package github.lyl21.wanandroid.moudle.navi

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.NaviInfo

interface NaviView:BaseView {

    fun getNavi(navi: Response<MutableList<NaviInfo>>)

    fun getNaviError(msg: String)
}
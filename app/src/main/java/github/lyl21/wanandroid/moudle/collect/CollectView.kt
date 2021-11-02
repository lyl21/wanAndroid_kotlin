package github.lyl21.wanandroid.moudle.collect

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.CollectInfo

interface CollectView : BaseView {

    fun unCollect(msg: String)
    fun getCollectList(info: Response<CollectInfo>)
    fun getCollectListError(msg: String)
    fun getCollectMoreList(info: Response<CollectInfo>)
    fun getCollectMoreListError(msg: String)

}
package github.lyl21.wanandroid.moudle.tree.child

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo

interface TreeChildView:BaseView {

    fun getTreeChild(treeChild: Response<ArticleInfo>)

    fun getTreeChildError(msg: String)

    fun getTreeMoreChild(treeChild: Response<ArticleInfo>)

    fun getTreeChildMoreError(msg: String)

    fun login(msg: String)

    fun collect(msg: String)

    fun unCollect(msg: String)

}
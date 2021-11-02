package github.lyl21.wanandroid.moudle.tree

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.SysTree

interface TreeView:BaseView {

    fun getTree(tree: Response<MutableList<SysTree>>)
    fun getTreeError(msg: String)

}
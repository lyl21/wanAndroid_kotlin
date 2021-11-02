package github.lyl21.wanandroid.moudle.project.child

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ProjectClassInfoChild

interface ProjectChildView:BaseView {

    fun getProjectChild(projectChild: Response<ProjectClassInfoChild>)

    fun getProjectChildError(msg: String)

    fun getProjectChildMore(projectChild: Response<ProjectClassInfoChild>)

    fun getProjectChildMoreError(msg: String)
}
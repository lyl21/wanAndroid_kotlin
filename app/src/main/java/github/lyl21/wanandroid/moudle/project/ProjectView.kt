package github.lyl21.wanandroid.moudle.project

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ProjectClassInfo

interface ProjectView:BaseView {

    fun getProject(project: Response<MutableList<ProjectClassInfo>>)

    fun getProjectError(msg: String)
}
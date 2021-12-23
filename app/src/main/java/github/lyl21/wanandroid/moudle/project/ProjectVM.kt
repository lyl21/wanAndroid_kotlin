package github.lyl21.wanandroid.moudle.project


import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.ProjectClassInfo

class ProjectVM:BaseVM() {
    var getProjectInfo: MutableLiveData<BaseResult<MutableList<ProjectClassInfo>>>
    =MutableLiveData()

    fun getProjectInfo() {
        requestWithLoadingFlow({request.getProject()},getProjectInfo)
    }

}
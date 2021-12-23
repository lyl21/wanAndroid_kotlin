package github.lyl21.wanandroid.moudle.project.child

import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.ProjectClassInfoChild

class ProjectChildVM:BaseVM() {
    var getProjectChildInfo: MutableLiveData<BaseResult<BasePagingResult<MutableList<ProjectClassInfoChild>>>>
            = MutableLiveData()

    fun getProjectChildInfo(page:Int,cid:Int) {
        requestWithLoadingFlow({request.getProjectChild(page,cid)},getProjectChildInfo)
    }

}
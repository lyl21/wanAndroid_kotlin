package github.lyl21.wanandroid.moudle.navi

import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.NaviInfo


class NaviVM : BaseVM() {
    var getNaviInfo: MutableLiveData<BaseResult<MutableList<NaviInfo>>> = MutableLiveData()
    fun getNaviInfo() {
        requestWithLoadingFlow({request.getNavi()},getNaviInfo)
    }


}



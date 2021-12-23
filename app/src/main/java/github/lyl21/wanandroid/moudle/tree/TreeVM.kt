package github.lyl21.wanandroid.moudle.tree


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.SysTree
import kotlinx.coroutines.launch

class TreeVM : BaseVM() {
    var getTree: MutableLiveData<BaseResult<MutableList<SysTree>>> = MutableLiveData()

    fun getTree() {
        viewModelScope.launch {
            requestWithLoadingFlow({request.getTree()},getTree)
        }
    }
}
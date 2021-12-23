package github.lyl21.wanandroid.moudle.tree.child


import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.ArticleInfo

class TreeChildVM : BaseVM() {
    var getTreeChild : MutableLiveData<BaseResult<BasePagingResult<MutableList<ArticleInfo>>>> = MutableLiveData()
    var toCollect : MutableLiveData<BaseResult<String>> = MutableLiveData()
    var unCollect : MutableLiveData<BaseResult<String>> = MutableLiveData()

    fun getTreeChild(page: Int, cid: Int) {
        requestWithLoadingFlow({request.getTreeChild(page,cid)},getTreeChild)
    }
    /*收藏文章*/
    fun toCollect(id: Int) {
        requestWithLoadingFlow({request.toCollect(id)},toCollect)
    }
    /*取消收藏*/
    fun unCollect(id: Int) {
        requestWithLoadingFlow({request.unCollect(id)},unCollect)
    }
}
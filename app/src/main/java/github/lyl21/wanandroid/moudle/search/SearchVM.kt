package github.lyl21.wanandroid.moudle.search


import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.ArticleInfo
import github.lyl21.wanandroid.bean.HotSearchInfo

class SearchVM : BaseVM() {

    var getHotSearchInfo: MutableLiveData<BaseResult<MutableList<HotSearchInfo>>> =
        MutableLiveData()

    var getSearchList: MutableLiveData<BaseResult<BasePagingResult<MutableList<ArticleInfo>>>> =
        MutableLiveData()

    var toCollect: MutableLiveData<BaseResult<String>> = MutableLiveData()
    var unCollect: MutableLiveData<BaseResult<String>> = MutableLiveData()


    fun getHotSearchInfo() {
        requestWithLoadingFlow({request.getHotkey()},getHotSearchInfo)
    }

    fun getSearchList(page: Int, key: String) {
        requestWithLoadingFlow({request.getSearchList(page,key)},getSearchList)
    }
    /*收藏文章*/
    fun toCollect(id: Int) {
        requestWithLoadingFlow({request.toCollect(id)},toCollect)
    }
    /*取消收藏*/
    fun unCollect(id: Int) {
        requestWithLoadingFlow({request.unCollect(id)},toCollect)
    }
}
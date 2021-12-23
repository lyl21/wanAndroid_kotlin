package github.lyl21.wanandroid.moudle.collect


import androidx.lifecycle.MutableLiveData
import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.bean.CollectInfo

class CollectVM :BaseVM(){

    private var unMyCollect: MutableLiveData<BaseResult<String>> = MutableLiveData()
    var getCollectList: MutableLiveData<BaseResult<BasePagingResult<MutableList<CollectInfo>>>> =
        MutableLiveData()


    fun unMyCollect(id:Int,originId:Int){
        requestWithLoadingFlow({request.unMyCollection(id,originId)},unMyCollect)
    }

    fun getCollectList(page:Int) {
        requestWithLoadingFlow({request.getCollectList(page)},getCollectList)
    }

}
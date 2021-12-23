package github.lyl21.wanandroid.moudle.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Engine
import com.google.gson.GsonBuilder
import github.lyl21.wanandroid.base.ui.BaseVM
import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.httpResult.DataState
import github.lyl21.wanandroid.bean.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeVM : BaseVM() {

    var getBingImgList: MutableLiveData<BingImgInfo> = MutableLiveData()
    var getBannerList: MutableLiveData<BaseResult<MutableList<IndexBannerInfo>>> = MutableLiveData()

    var getArticleList: MutableLiveData<BaseResult<BasePagingResult<MutableList<ArticleInfo>>>> = MutableLiveData()

    var toCollect: MutableLiveData<BaseResult<String>> = MutableLiveData()
    var unCollect: MutableLiveData<BaseResult<String>> = MutableLiveData()


    //获取必应图集并保存到本地
    fun getBingImgList(url: String) {
        viewModelScope.launch {
            flow {
                emit(BingRetrofitService.create.getTodayBingImg(url))
            }
                .catch {
                it.printStackTrace()
            }.collect {
                val gson = GsonBuilder().create()
                val toJson = gson.toJson(it.body())
                val fromJson = gson.fromJson(toJson, BingImgInfo::class.java)
                getBingImgList.postValue(fromJson)
            }
        }
    }

    /**
     * 获取首页banner图
     */
    fun getBannerList() {
        requestWithLoadingFlow({ request.getBannerList() }, getBannerList)
    }

    /**
     * 请求首页文章，
     */
    fun getArticleList(page: Int) {
        requestWithLoadingFlow({ request.getArticleList(page) }, getArticleList)
    }


    /*收藏文章*/
    fun toCollect(id: Int) {
        requestWithLoadingFlow({ request.toCollect(id) }, toCollect)
    }


    /*取消收藏*/
    fun unCollect(id: Int) {
        requestWithLoadingFlow({ request.unCollect(id) }, unCollect)
    }

}
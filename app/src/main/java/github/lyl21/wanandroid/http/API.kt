import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.bean.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by yechao on 2020/1/8/008.
 * Describe :
 */
interface API {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }


    interface WanAndroidApi {

        //今日必应壁纸
        @GET
        suspend fun getTodayBingImg(@Url url: String): Response<Any>

        //必应壁纸列表  limit--8
        @GET
        suspend fun getBingImgList(@Url url: String): Response<Any>


        //-----------------------【登录】----------------------
        //登录
        @FormUrlEncoded
        @POST("user/login")
        suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): BaseResult<UserInfo>

        //登出
        @GET("user/logout/json")
        suspend fun logout(): BaseResult<String>

        //个人信息
        @GET("user/lg/userinfo/json?")
        suspend fun getUserInfo(@Query("username") username: String): BaseResult<UserDataInfo>


        //-----------------------【首页相关】----------------------

        //首页文章列表
        @GET("article/list/{page}/json")
        suspend fun getArticleList(@Path("page") page: Int): BaseResult<BasePagingResult<MutableList<ArticleInfo>>>

        //首页banner
        @GET("banner/json")
        suspend fun getBannerList(): BaseResult<MutableList<IndexBannerInfo>>


        //-----------------------【 体系 】----------------------

        //体系数据
        @GET("tree/json")
        suspend fun getTree(): BaseResult<MutableList<SysTree>>

        //知识体系下的文章
        @GET("article/list/{page}/json?")
        suspend fun getTreeChild(
            @Path("page") page: Int,
            @Query("cid") cid: Int
        ): BaseResult<BasePagingResult<MutableList<ArticleInfo>>>

        //-----------------------【 导航 】----------------------

        //导航数据
        @GET("navi/json")
        suspend fun getNavi(): BaseResult<MutableList<NaviInfo>>


        //-----------------------【 项目 】----------------------

        //项目分类
        @GET("project/tree/json")
        suspend fun getProject(): BaseResult<MutableList<ProjectClassInfo>>

        //项目列表数据
        @GET("project/list/{page}/json?")
        suspend fun getProjectChild(
            @Path("page") page: Int,
            @Query("cid") cid: Int
        ): BaseResult<BasePagingResult<MutableList<ProjectClassInfoChild>>>


        //-----------------------【 搜索 】----------------------

        //搜索
        @FormUrlEncoded
        @POST("article/query/{page}/json?")
        suspend fun getSearchList(
            @Path("page") page: Int,
            @Field("k") k: String
        ): BaseResult<BasePagingResult<MutableList<ArticleInfo>>>

        //搜索热词
        @GET("hotkey/json")
        suspend fun getHotkey(): BaseResult<MutableList<HotSearchInfo>>

        //-----------------------【 收藏 】----------------------

        //收藏文章列表
        @GET("lg/collect/list/{page}/json?")
        suspend fun getCollectList(@Path("page") page: Int): BaseResult<BasePagingResult<MutableList<CollectInfo>>>

        //收藏站内文章
        @POST("lg/collect/{id}/json")
        suspend fun toCollect(@Path("id") id: Int): BaseResult<String>

        //取消收藏（文章列表）
        @POST("lg/uncollect_originId/{id}/json")
        suspend fun unCollect(
            @Path("id") id: Int,
        ): BaseResult<String>

        //取消收藏（收藏 列表）
        @POST("lg/uncollect/{id}/json")
        suspend fun unMyCollection(
            @Path("id") id: Int,
            @Field("originId") originId: Int
        ): BaseResult<String>

    }

}
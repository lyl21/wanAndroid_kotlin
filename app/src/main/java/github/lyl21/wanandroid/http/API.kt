import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.*
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by yechao on 2020/1/8/008.
 * Describe :
 */
class API {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com/"
    }


    interface WanAndroidApi {

        @Streaming
        @GET
        fun downloadFile(@Url url: String?): Observable<ResponseBody?>?

        @Multipart
        @POST
        fun callPostRequestMultipartURL(
            @Url url: String?,
            @Part paramMap: RequestBody?,
            @Part fileMap: List<Part?>?
        ): Call<String?>?


        //-----------------------【登录】----------------------

        //登录
        @FormUrlEncoded
        @POST("user/login")
        fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): Observable<Response<UserInfo>>

        //登出
        @GET("user/logout/json")
        fun logout(): Observable<Response<String>>

        //个人信息
        @GET("user/lg/userinfo/json?")
        fun getUserInfo(@Query("username") username: String): Observable<Response<UserDataInfo>>


        //-----------------------【首页相关】----------------------

        //首页文章列表
        @GET("article/list/{page}/json")
        fun getArticleList(@Path("page") page: Int): Observable<Response<ArticleInfo>>

        //首页banner
        @GET("banner/json")
        fun getBanner(): Observable<Response<MutableList<IndexBannerInfo>>>


        //-----------------------【 体系 】----------------------

        //体系数据
        @GET("tree/json")
        fun getTree(): Observable<Response<MutableList<SysTree>>>

        //知识体系下的文章
        @GET("article/list/{page}/json?")
        fun getTreeChild(
            @Path("page") page: Int,
            @Query("cid") cid: Int
        ): Observable<Response<ArticleInfo>>

        //-----------------------【 导航 】----------------------

        //导航数据
        @GET("navi/json")
        fun getNavi(): Observable<Response<MutableList<NaviInfo>>>


        //-----------------------【 项目 】----------------------

        //项目分类
        @GET("project/tree/json")
        fun getProject(): Observable<Response<MutableList<ProjectClassInfo>>>

        //项目列表数据
        @GET("project/list/{page}/json?")
        fun getProjectChild(
            @Path("page") page: Int,
            @Query("cid") cid: Int
        ): Observable<Response<ProjectClassInfoChild>>


        //-----------------------【 搜索 】----------------------

        //搜索
        @FormUrlEncoded
        @POST("article/query/{page}/json?")
        fun getSearchList(
            @Path("page") page: Int,
            @Field("k") k: String
        ): Observable<Response<ArticleInfo>>

        //搜索热词
        @GET("hotkey/json")
        fun getHotkey(): Observable<Response<MutableList<HotSearchInfo>>>

        //-----------------------【 收藏 】----------------------

        //收藏文章列表
        @GET("lg/collect/list/{page}/json?")
        fun getCollectList(@Path("page") page: Int): Observable<Response<CollectInfo>>

        //收藏站内文章
        @POST("lg/collect/{id}/json")
        fun toCollection(@Path("id") id: Int): Observable<Response<String>>

        //取消收藏（文章列表）
        @POST("lg/uncollect_originId/{id}/json")
        fun unCollection(
            @Path("id") id: Int,
        ): Observable<Response<String>>

        //取消收藏（收藏 列表）
        @POST("lg/uncollect/{id}/json")
        fun unMyCollection(
            @Path("id") id: Int,
            @Field("originId") originId: Int
        ): Observable<Response<String>>

    }

}
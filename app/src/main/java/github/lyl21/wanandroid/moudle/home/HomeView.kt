import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.IndexBannerInfo

/**
 * Created by yechao on 2020/1/9/009.
 * Describe :
 */
interface HomeView : BaseView {

    fun getBanner(banners: Response<MutableList<IndexBannerInfo>>)

    fun getBannerError(msg: String)

    fun getArticleList(article: Response<ArticleInfo>)

    fun getArticleError(msg: String)

    fun getArticleMoreList(article: Response<ArticleInfo>)
    fun getArticleMoreError(msg: String)

    fun login(msg: String)

    fun toCollection(msg: String)

    fun unCollection(msg: String)

}
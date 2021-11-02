package github.lyl21.wanandroid.moudle.search

import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.HotSearchInfo

interface SearchView:BaseView {
    fun getHotkey(hotkey: Response<MutableList<HotSearchInfo>>)

    fun getHotkeyError(msg: String)

    fun getArticleList(article: Response<ArticleInfo>)

    fun getArticleError(msg: String)

    fun getArticleMoreList(article: Response<ArticleInfo>)

    fun getArticleMoreError(msg: String)

    fun login(msg: String)

    fun collect(msg: String)

    fun unCollect(msg: String)
}
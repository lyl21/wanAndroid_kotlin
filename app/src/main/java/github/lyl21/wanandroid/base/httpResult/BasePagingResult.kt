package github.lyl21.wanandroid.base.httpResult

data class BasePagingResult<T>(
    var curPage: Int=0,
    var datas: T? =null,
    var offset: Int=0,
    var over: Boolean=false,
    var pageCount: Int=0,
    var size: Int=0,
    var total: Int=0
)
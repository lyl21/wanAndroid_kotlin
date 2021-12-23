package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor
@Data
@NoArgsConstructor
data class NaviInfo(
    val articles: MutableList<NaviArticleDetail> ,
    val cid: Int,
    val name: String,
    var isChecked:Boolean
)
@Data
@NoArgsConstructor
data class NaviArticleDetail(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Any> = emptyList(),
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)



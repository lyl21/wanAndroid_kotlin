package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author    popcomimico
 * @date    2021/9/28 23:28
 */
@Data
@NoArgsConstructor
data class ArticleInfo(
    val apkLink: String ,
    val audit: Int ,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
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
    val tags: List<TagInfo> ,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)




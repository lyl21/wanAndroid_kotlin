package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author    popcomimico
 * @date    2021/9/28 23:45
 */
@Data
@NoArgsConstructor
data class ProjectClassInfo(
    val children: ArrayList<ProjectClassInfoChild> ,
    val courseId:Int,
    val id:Int,
    val name:String,
    val order:Int,
    val parentChapterId:Int,
    val visible:Int,
)
@Data
@NoArgsConstructor
data class ProjectClassInfoChild(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean=false,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val fresh: Boolean=false,
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
    val tags: List<ProjectClassInfoChildrenTag> = emptyList(),
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)
@Data
@NoArgsConstructor
data class ProjectClassInfoChildrenTag(
    val name: String,
    val url: String
)
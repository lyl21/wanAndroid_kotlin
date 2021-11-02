package github.lyl21.wanandroid.entity

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 23:45
 */
data class ProjectClassInfo(
    val children: ArrayList<ProjectClassInfoChild>,
    val courseId:Int,
    val id:Int,
    val name:String,
    val order:Int,
    val parentChapterId:Int,
    val visible:Int,
)


data class ProjectClassInfoChild(
    val curPage: Int,
    val datas: MutableList<ProjectClassInfoChildData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)
data class ProjectClassInfoChildData(
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
    val tags: List<ProjectClassInfoChildrenTag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)

data class ProjectClassInfoChildrenTag(
    val name: String,
    val url: String
)
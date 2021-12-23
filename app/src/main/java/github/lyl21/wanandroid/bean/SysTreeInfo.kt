package github.lyl21.wanandroid.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable

/**
 * @author    popcomimico
 * @date    2021/9/28 23:39
 */
@Data
@NoArgsConstructor
data class SysTree(
    var isShow: Boolean,
    val children: ArrayList<SysTreeChildren> ,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: String
)

//@Parcelize
@Data
@NoArgsConstructor
data class SysTreeChildren(
    val children: ArrayList<Any> ,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: String
): Serializable
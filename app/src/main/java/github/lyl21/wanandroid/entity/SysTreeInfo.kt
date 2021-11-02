package github.lyl21.wanandroid.entity

import java.io.Serializable

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 23:39
 */
data class SysTree(
    var isShow: Boolean,
    val children: ArrayList<SysTreeChildren>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: String
) : Serializable


data class SysTreeChildren(
    val children: ArrayList<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: String
) : Serializable
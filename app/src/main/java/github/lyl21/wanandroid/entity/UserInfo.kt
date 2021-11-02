package github.lyl21.wanandroid.entity

import java.io.Serializable

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 23:22
 */
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Any>,
    val email: String,  // 可用
    val icon: String,
    val id: Int,   // 可用
    val nickname: String,  // 可用
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String,  // 可用
):Serializable
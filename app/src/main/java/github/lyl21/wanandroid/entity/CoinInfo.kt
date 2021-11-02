package github.lyl21.wanandroid.entity

import java.io.Serializable

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 23:21
 */
data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String,
) : Serializable


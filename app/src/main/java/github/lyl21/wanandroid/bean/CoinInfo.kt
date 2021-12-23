package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor


/**
 * @author    popcomimico
 * @date    2021/9/28 23:21
 */
@Data
@NoArgsConstructor
data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String,
)


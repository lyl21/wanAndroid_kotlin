package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author    popcomimico
 * @date    2021/9/28 23:30
 */
@Data
@NoArgsConstructor
data class TagInfo(
    val name:String,
    val url:String,
)

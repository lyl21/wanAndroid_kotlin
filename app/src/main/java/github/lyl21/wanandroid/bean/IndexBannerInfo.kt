package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor


/**
 * @author    popcomimico
 * @date    2021/9/28 23:34
 */
@Data
@NoArgsConstructor
data class IndexBannerInfo(
    val desc: String ,
    val id:Int,
    val imagePath:String,
    val isVisible:Int,
    val order:Int,
    val title:String,
    val type:Int,
    val url:String
)

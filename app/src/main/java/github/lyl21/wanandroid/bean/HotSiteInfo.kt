package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor

/**
 * @author    popcomimico
 * @date    2021/9/28 23:36
 */
@Data
@NoArgsConstructor
data class HotSiteInfo(
    val category:String,
    val icon:String,
    val id:Int,
    val link:String,
    val name:String,
    val order:Int,
    val visible:Int,
)

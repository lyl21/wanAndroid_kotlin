package github.lyl21.wanandroid.bean

import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
data class BingImgInfo(
    var images:List<Img> ,
    var tooltips: Tooltips,
)
@Data
@NoArgsConstructor
data class Img(
    var startdate:String,
    var fullstartdate:String,
    var enddate:String,
    var url:String,
    var urlbase:String,
    var copyright:String,
    var copyrightlink:String,
    var title:String,
    var quiz:String,
    var wp:Boolean,
    var hsh:String,
    var drk:Int,
    var top:Int,
    var bot:Int,
    var hs:List<String>,
)
@Data
@NoArgsConstructor
data class Tooltips(
    var loading:String,
    var previous:String,
    var next:String,
    var walle:String,
    var walls:String,
)

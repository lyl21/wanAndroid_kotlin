package github.lyl21.wanandroid.common

import android.os.Environment
import com.luck.picture.lib.tools.SdkVersionUtils
import github.lyl21.wanandroid.App
import github.lyl21.wanandroid.util.FileUtil
import github.lyl21.wanandroid.view.CommonDialog


/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 16:51
 */
class ConstantParam {
    companion object {

        const val IP: String = "https://www.wanandroid.com/"
        const val PAGE_SIZE = 10

        const val PACKAGE_NAME: String = "github.lyl21.wanandroid"
        const val APK_NAME: String = "wanandroid"

        // https://cn.bing.com
        const val BingBaseUrl="https://cn.bing.com"
        //必应今日壁纸
        //https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN
        const val TodayBingImgUrl="/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN"
        //必应壁纸列表
        //https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&mkt=zh-CN
        const val BingImgListUrl="/HPImageArchive.aspx?format=js&idx=0&n=8&mkt=zh-CN"

        const val DEFAULT_TIME_FORMAT = "yyyy-MM-dd"
        const val FILE_PROVIDER = ".FileProvider"


        // 填写你的项目在 Agora 控制台中生成的 App ID。
         const val Agora_APPID = "72a528564ce34aae85b21733f4374516"
        // 填写频道名称。
         const val channelName = "test"
        // 填写 Agora 控制台中生成的临时 Token。
         const val token = "00672a528564ce34aae85b21733f4374516IAC1NLvXod9OzdKwZ90/i++YdIToPxsBLDzNDxNu/02atAx+f9gAAAAAEABgg+xai56oYQEAAQCNnqhh"


        /*文件存储配置*/ //图片的缓存路径
        val IMAGE_SAVE_CACHE: String = getBasePictureCacheDir()
        private fun getBasePictureCacheDir(): String {
            return if (FileUtil.isSDExist) {
                /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                           return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + PACKAGE_NAME;
                       } else {
                           return HuahanApplication.getMyApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";
                       }*/
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/" + APK_NAME + "/"
            } else {
                "/data/data/$PACKAGE_NAME/Pictures/"
            }
        }


        /**
         * 获取图片缓存目录
         *
         * @param fileType 0默认；1裁剪；2下载(图片或视频下载，更新相册可以显示，APK的下载与此不同)；3APK下载
         * @return
         */
        private fun getBasePictureCacheDir(fileType: Int): String? {
            return if (FileUtil.isSDExist) {
                when (fileType) {
                    1 -> {
                        return if (SdkVersionUtils.isR()) {
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                .toString() + "/"
                        } else {
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                .toString() + "/" + APK_NAME + "/"
                        }
                    }
                    2 -> {
                        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .toString() + "/" + APK_NAME + "/"
                    }
                    3 -> {
                        return App().applicationContext
                            .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
                    }
                    else -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString() + "/" + APK_NAME + "/"
                }
            } else {
                "/data/data/$PACKAGE_NAME/Pictures/"
            }
        }


    }

    object FilePaths {
        val IMAGE_CROP_CACHE = getBasePictureCacheDir(1)
    }



}
package github.lyl21.wanandroid.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.yalantis.ucrop.view.OverlayView
import github.lyl21.wanandroid.App
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.common.ConstantParam
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object ImageUtil {

    /**
     * 图片选择
     *
     * @param fragment   上下文
     * @param mimeType   图片类型
     * @param maxCount   最大张数
     * @param isCompress 是否压缩
     */
    fun pictureChoose(fragment: Fragment?, mimeType: Int, maxCount: Int, isCompress: Boolean) {
        FileUtil.createDirectory(ConstantParam.IMAGE_SAVE_CACHE)
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(fragment)
            .openGallery(mimeType) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .imageEngine(GlideEngine.createGlideEngine())
            .setCameraImageFormat(PictureMimeType.JPEG_Q)
            .setCameraVideoFormat(PictureMimeType.MP4_Q)
            .setCameraAudioFormat(PictureMimeType.AMR_Q)
            .isPreviewEggs(true)//预览图片时是否增强左右滑动图片体验

            .isWeChatStyle(true)
//            .theme(R.style.app_picture_style) // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
            .maxSelectNum(maxCount) // 最大图片选择数量
            .minSelectNum(1) // 最小选择数量
            .imageSpanCount(4) // 每行显示个数
            .selectionMode(PictureConfig.MULTIPLE) // 多选 or 单选
            .isPreviewImage(false) // 是否可预览图片
            .isPreviewVideo(false) // 是否可预览视频
            .isEnablePreviewAudio(true) // 是否可播放音频
            .isCamera(true) // 是否显示拍照按钮
            .isZoomAnim(false) // 图片列表点击 缩放效果 默认true
            //  .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .setOutputCameraPath(ConstantParam.APK_NAME) //                .setOutputCameraPath(ConstantParam.IMAGE_SAVE_CACHE)// 自定义拍照保存路径
//            .isOriginalImageControl(false) //是否原图
            //                .compressSavePath(ConstantParam.IMAGE_SAVE_CACHE)//压缩图片保存地址
            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//            .glideOverride(160, 160) // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
            .cropImageWideHigh(1,1)// 裁剪宽高比，设置如果大于图片本身宽高则无效
            .withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .hideBottomControls(true) // 是否显示uCrop工具栏，默认不显示
            .isGif(true) // 是否显示gif图片
            .isEnableCrop(true) // 是否裁剪
//            .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
            .freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// 裁剪框拖动模式
            // .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            // .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .circleDimmedLayer(true) // 是否圆形裁剪
            //.isCropDragSmoothToCenter(true)// 裁剪框拖动时图片自动跟随居中
            .rotateEnabled(true)//裁剪是否可旋转图片
            .scaleEnabled(true)//裁剪是否可放大缩小图片
            .isMultipleSkipCrop(true)//多图裁剪是否支持跳过
            .isMultipleRecyclerAnimation(true)// 多图裁剪底部列表显示动画效果
            .isCompress(true) // 是否压缩
            .minimumCompressSize(200) // 小于200kb的图片不压缩
            .hideBottomControls(true)//显示底部uCrop工具栏
            .synOrAsy(false)//开启同步or异步压缩  默认同步
            .isOpenClickSound(false) // 是否开启点击声音
            //.selectionMedia(selectList)// 是否传入已选图片
//            .compressQuality(90)// 裁剪压缩质量 默认100
            .videoMaxSecond(Int.MAX_VALUE / 1000)
            .videoMinSecond(1) //                .videoQuality(1)// 视频录制质量 0 or 1
            //.videoSecond()//显示多少秒以内的视频or音频也可适用
            //.recordVideoSecond()//录制视频秒数 默认60s
            .isAutomaticTitleRecyclerTop(false)//图片列表超过一屏连续点击顶部标题栏快速回滚至顶部
            .forResult(PictureConfig.CHOOSE_REQUEST) //结果回调onActivityResult code
    }

    /**
    * @param mimeType 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
     * @param  isEnableCrop  是否裁剪
     * @param isCompress 是否压缩
     * @param requestCode  PictureConfig.CHOOSE_REQUEST
    * */
    fun getImagePictureSelector(
        context: Context?,
        mimeType: Int,
        maxCount: Int,
        isEnableCrop:Boolean,
        isCompress: Boolean ) {
        FileUtil.createDirectory(ConstantParam.IMAGE_SAVE_CACHE)
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(context as Activity?)
            .openGallery(mimeType) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//            .theme(R.style.app_picture_style) // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
            .isWeChatStyle(true)
            .imageEngine(GlideEngine.createGlideEngine())
            .setCameraImageFormat(PictureMimeType.JPEG_Q)
            .setCameraVideoFormat(PictureMimeType.MP4_Q)
            .setCameraAudioFormat(PictureMimeType.AMR_Q)
            .maxSelectNum(maxCount) // 最大图片选择数量
            .minSelectNum(1) // 最小选择数量
            .imageSpanCount(4) // 每行显示个数
            .selectionMode(PictureConfig.MULTIPLE) // 多选 or 单选
            .isPreviewImage(true) // 是否可预览图片
            .isPreviewVideo(true) // 是否可预览视频
            .isEnablePreviewAudio(true) // 是否可播放音频
            .isCamera(true) // 是否显示拍照按钮
//            .isZoomAnim(false) // 图片列表点击 缩放效果 默认true
            //                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .setOutputCameraPath(ConstantParam.APK_NAME) //                .setOutputCameraPath(ConstantParam.IMAGE_SAVE_CACHE)// 自定义拍照保存路径
            .isEnableCrop(isEnableCrop) // 是否裁剪
            .isCompress(isCompress) // 是否压缩
//            .isOriginalImageControl(false) //是否原图
            .synOrAsy(false) //同步true或异步false 压缩 默认同步
//            .compressSavePath(ConstantParam.IMAGE_SAVE_CACHE)//压缩图片保存地址
            .minimumCompressSize(200) // 小于200kb的图片不压缩
            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//            .glideOverride(160, 160) // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
            .cropImageWideHigh(1,1)// 裁剪宽高比，设置如果大于图片本身宽高则无效
            .withAspectRatio(1, 1) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .hideBottomControls(true) // 是否显示uCrop工具栏，默认不显示
            .isGif(true) // 是否显示gif图片
            .freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// 裁剪框拖动模式
//            .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
            .circleDimmedLayer(true) // 是否圆形裁剪
            .rotateEnabled(false)//裁剪是否可旋转图片
            .scaleEnabled(true)//裁剪是否可放大缩小图片
            .isMultipleSkipCrop(true)//多图裁剪是否支持跳过
            .isMultipleRecyclerAnimation(true)// 多图裁剪底部列表显示动画效果
            .isOpenClickSound(false) // 是否开启点击声音
            .recordVideoSecond(15)
            .videoMaxSecond(15)
            .videoMinSecond(1)
            .forResult( PictureConfig.CHOOSE_REQUEST) //结果回调onActivityResult code
    }

//    fun pictureVideoPlay(context: Context, videoPath: String) {
//        Log.i("chen", "pictureVideoPlay==$videoPath")
//        val intent = Intent(context, HHSoftPictureVideoPlayActivity::class.java)
//        intent.putExtra("video_path", videoPath)
//        context.startActivity(intent)
//    }

    /**
     * 创建图片路径
     */
    fun createSystemCropOutPath(): String? {
        return try {
            FileUtil.createDirectory(ConstantParam.FilePaths.IMAGE_CROP_CACHE)
            val context: Context = App().applicationContext 
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Date())
            val fileName = "IMG_CROP_$timeStamp.jpg"
            val imgFile: File
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                imgFile = File(ConstantParam.FilePaths.IMAGE_CROP_CACHE.toString() + fileName)
                // 通过 MediaStore API 插入file 为了拿到系统裁剪要保存到的uri（因为App没有权限不能访问公共存储空间，需要通过 MediaStore API来操作）
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, imgFile.absolutePath)
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                imgFile = File(ConstantParam.FilePaths.IMAGE_CROP_CACHE.toString() + fileName)
            }
            imgFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 创建图片路径
     */
    fun createImgPath(): String {
        FileUtil.createDirectory(ConstantParam.IMAGE_SAVE_CACHE)
        return ConstantParam.IMAGE_SAVE_CACHE + System.currentTimeMillis() + ".jpg"
    }

    /**
     * 创建视频路径
     */
    fun createVideoPath(videoLoad: String): String {
        FileUtil.createDirectory(ConstantParam.IMAGE_SAVE_CACHE)
        return ConstantParam.IMAGE_SAVE_CACHE.toString() + videoLoad + ".mp4"
    }


    /**
     * 查看大图
     *
     * @param context
     * @param position
     * @param list
     */
//    fun lookBigImage(context: Context, position: Int, list: ArrayList<out IImageBrower?>?) {
//        val intent = Intent(context, PictureBrowserActivity::class.java)
//        intent.putExtra(PictureBrowserActivity.FLAG_IMAGE_POSITION, position)
//        intent.putExtra(PictureBrowserActivity.FLAG_IMAGE_LIST, list)
//        context.startActivity(intent)
//    }

    /**
     * 本地视频获取缩略图
     *
     * @param videoPath
     * @return
     */
    fun videoThumbImage(videoPath: String?): String {
        if (!TextUtils.isEmpty(videoPath)) {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(videoPath)
            val recordedTime = SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Date())
            val targetFile = getOutputMediaFile(recordedTime, 1)
            var isSuccess = false
            val bitmap = mmr.frameAtTime
            if (bitmap != null) {
                try {
                    var out: FileOutputStream? = FileOutputStream(targetFile)
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                        isSuccess = true
                        out!!.flush()
                        out.close()
                        out = null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    LogUtils.i("lll", "getThumbImage==error==" + Log.getStackTraceString(e))
                }
            }
            mmr.release()
            if (isSuccess) {
                return targetFile!!.path
            }
        }
        return ""
    }

    fun getOutputMediaFile(fileName: String, type: Int): File? {
        // To be safe, you should agree_un_check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        val mediaStorageDir: File = File(ConstantParam.IMAGE_SAVE_CACHE) ?: return null
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            LogUtils.i("lll", "failed to create directory")
            return null
        }

        // Create a media file name
        val mediaFile: File
        mediaFile = if (type == 1) {
            //图片
            File(mediaStorageDir.path + File.separator + fileName + ".jpg")
        } else if (type == 2) {
            //视频
            File(mediaStorageDir.path + File.separator + fileName + ".mp4")
        } else {
            return null
        }
        return mediaFile
    }

    fun setLocalImageSize(imagePath: String?): Map<String, Int>? {
        val file = File(imagePath)
        if (file.exists()) {
            val map: MutableMap<String, Int> = HashMap()
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, options)
            map["width"] = options.outWidth
            map["height"] = options.outHeight
            return map
        }
        return null
    }

    fun setNetImageSize(imagePath: String): Map<String, Int>? {
        val underline = imagePath.split("_").toTypedArray()
        val point: String
        var ratio: Array<String?>? = null
        var image_width = 0
        var image_height = 0
        if (underline.size > 0) {
            point = underline[underline.size - 1]
            val w_h = point.split("\\.").toTypedArray()
            if (w_h.size == 2) {
                ratio = w_h[0].split("x").toTypedArray()
                if (ratio.size == 2) {
                    val map: MutableMap<String, Int> = HashMap()
                    if (!TextUtils.isEmpty(ratio[0])) {
                        image_width = Integer.valueOf(ratio[0])
                        map["width"] = image_width
                    }
                    if (!TextUtils.isEmpty(ratio[1])) {
                        image_height = Integer.valueOf(ratio[1])
                        map["height"] = image_height
                    }
                    return map
                }
            }
        }
        return null
    }

}
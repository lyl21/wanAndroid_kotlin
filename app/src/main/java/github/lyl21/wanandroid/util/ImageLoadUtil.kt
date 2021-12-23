package github.lyl21.wanandroid.util

import android.content.Context
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.core.util.Consumer
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.math.BigDecimal
import java.util.ArrayList


/**
 * @类说明 图片加载工具类
 * @作者
 * @创建日期 2019/8/21 16:14
 * 注意：
 * 一、在Android P的系统上，所有Http的请求都被默认阻止了，导致glide在9.0加载不出来图片
 * 解决方案：在清单文件中
 * <application
 * ********
 * android:usesCleartextTraffic="true"
 * **********
 * >
 */
class ImageLoadUtil {
    /**
     * 加载矩形图片
     *
     * @param context
     * @param defaultImageResourceId 占位图片
     * @param imagePath              图片路径
     * @param imageView              ImageView对象
     */
    fun loadImage(
        context: Context?,
        defaultImageResourceId: Int,
        imagePath: String?,
        imageView: ImageView?
    ) {
        Glide.with(context!!)
            .asBitmap()
            .load(imagePath)
            .placeholder(defaultImageResourceId)
            .error(defaultImageResourceId)
            .centerCrop()
            .into(imageView!!)
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param defaultImageResourceId 占位图片
     * @param imagePath              图片路径
     * @param imageView              ImageView对象
     */
    fun loadRoundImage(
        context: Context?,
        defaultImageResourceId: Int,
        imagePath: String?,
        imageView: ImageView?
    ) {
        //设置图片圆角角度
        val roundedCorners = RoundedCorners(SizeUtils.dp2px(5f))
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        val options = RequestOptions().transform(CenterCrop(), roundedCorners)
        Glide.with(context!!)
            .asBitmap()
            .load(imagePath)
            .placeholder(defaultImageResourceId)
            .error(defaultImageResourceId)
            .apply(options)
            .into(imageView!!)
    }

    /**
     * 加载自定义圆角图片
     *
     * @param context
     * @param defaultImageResourceId 占位图片
     * @param imagePath              图片路径
     * @param imageView              ImageView对象
     * @param radius                 图片圆角数组、按照左、上、右、下的顺序添加，偿长度是4，单位是dp
     */
    fun loadCustomRoundImage(
        context: Context?,
        defaultImageResourceId: Int,
        imagePath: String?,
        imageView: ImageView?,
        radius: IntArray?
    ) {
        if (radius == null || radius.size != 4) {
            loadRoundImage(context, defaultImageResourceId, imagePath, imageView)
        } else {
            val leftTopRadius: Int = SizeUtils.dp2px(radius[0].toFloat())
            val rightTopRadius: Int = SizeUtils.dp2px(radius[1].toFloat())
            val leftBottomRadius: Int = SizeUtils.dp2px(radius[2].toFloat())
            val rightBottomRadius: Int = SizeUtils.dp2px(radius[3].toFloat())
            val roundedCorners = CustomRoundedCorners(
                leftTopRadius,
                rightTopRadius,
                leftBottomRadius,
                rightBottomRadius
            )
            val options = RequestOptions().transform(CenterCrop(), roundedCorners)
            Glide.with(context!!)
                .asBitmap()
                .load(imagePath)
                .placeholder(defaultImageResourceId)
                .error(defaultImageResourceId)
                .apply(options)
                .into(imageView!!)
        }
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param defaultImageResourceId 占位图片
     * @param imagePath              图片路径
     * @param imageView              ImageView对象
     */
    fun loadCircleImage(
        context: Context?,
        defaultImageResourceId: Int,
        imagePath: String?,
        imageView: ImageView?
    ) {
        val options = RequestOptions.circleCropTransform()
        //                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//                .skipMemoryCache(true);//不做内存缓存
        Glide.with(context!!)
            .asBitmap()
            .load(imagePath)
            .placeholder(defaultImageResourceId)
            .error(defaultImageResourceId)
            .apply(options)
            .into(imageView!!)
    }

    /**
     * 加载动画，只播放一次
     *
     * @param context
     * @param defaultImageResourceId
     * @param imagePath
     * @param imageView
     */
    fun loadGifImage(
        context: Context?,
        defaultImageResourceId: Int,
        imagePath: String?,
        imageView: ImageView
    ) {
        Glide.with(context!!)
            .asGif()
            .load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(defaultImageResourceId)
            .error(defaultImageResourceId)
            .into(object : SimpleTarget<GifDrawable?>() {
                override fun onResourceReady(
                    resource: GifDrawable,
                    transition: Transition<in GifDrawable?>?
                ) {
                    if (resource is GifDrawable) {
                        resource.setLoopCount(2)
                        imageView.setImageDrawable(resource)
                        resource.start()
                    }
                }
            })
    }

    /**
     * 清除图片磁盘缓存
     *
     * @param context
     */
    fun clearImageDiskCache(context: Context?) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread { Glide.get(context!!).clearDiskCache() }.start()
            } else {
                Glide.get(context!!).clearDiskCache()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除图片内存缓存
     *
     * @param context
     */
    fun clearImageMemoryCache(context: Context?) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { // 只能在主线程执行
                Glide.get(context!!).clearMemory()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清空Glide缓存
     *
     * @param context
     */
    fun clearImageAllCache(context: Context) {
        clearImageDiskCache(context)
        clearImageMemoryCache(context)
        val imageExternalCatchDir = context.externalCacheDir
            .toString() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR
        FileUtil.deleteFolder(imageExternalCatchDir)
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @param context
     * @return 返回字节长度，获取失败返回0
     */
    fun cacheSize(context: Context): Long {
        try {
            val filePath =
                context.cacheDir.toString() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR
            return FileUtil.fileSize(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取格式化的Glide造成的缓存大小
     *
     * @param context
     * @return 格式话字符串，以Byte、KB、MB、GB、TB结尾
     */
    fun formatCacheSize(context: Context): String? {
        return getFormatSize(cacheSize(context).toDouble())
    }


    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    private fun getFormatSize(size: Double): String? {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 图片压缩：异步单张压缩图片
     *
     * @param context
     * @param sourceImagePath 原图片路径
     * @param targetDirPath   目标文件夹路径eg:/storage/emulated/0/HHSoftLib/
     * @param callBack        压缩回调，压缩成功返回压缩后的路径eg:/storage/emulated/0/HHSoftLib/1566460459501169.jpeg；压缩失败返回原路径sourceImagePath
     */
    fun compressAsync(
        context: Context?,
        sourceImagePath: String?,
        targetDirPath: String?,
        callBack: Consumer<String?>
    ) {
//        Luban.with(context)
//            .load(sourceImagePath)
//            .ignoreBy(200) //不压缩的阈值，单位为K
//            .setTargetDir(targetDirPath)
//            .filter(object : CompressionPredicate() {
//                fun apply(path: String?): Boolean {
//                    return !(TextUtils.isEmpty(path) || FileUtil.FileType.IMAGE_GIF === FileUtil.fileTypeForImageData(
//                        context,
//                        sourceImagePath
//                    ))
//                }
//            })
//            .setRenameListener(object : OnRenameListener() {
//                fun rename(filePath: String?): String? {
//                    return System.currentTimeMillis().toString() + ".jpg"
//                }
//            })
//            .setCompressListener(object : OnCompressListener() {
//                fun onStart() {
//                    Log.i("chen", "onStart==")
//                }
//
//                fun onSuccess(file: File) {
//                    Log.i("chen", "onSuccess==" + file.absolutePath)
//                    callBack.accept(file.absolutePath)
//                }
//
//                fun onError(e: Throwable?) {
//                    Log.i("chen", "onError==" + Log.getStackTraceString(e))
//                    callBack.accept(sourceImagePath)
//                }
//            }).launch()
    }

    /**
     * 图片压缩：异步多张压缩图片
     *
     * @param context
     * @param sourceImages  原图片路径集合
     * @param targetDirPath 目标文件夹路径eg:/storage/emulated/0/HHSoftLib/
     * @param callBack      压缩回调，返回集合，压缩成功返回压缩后的路径eg:/storage/emulated/0/HHSoftLib/1566460459501169.jpeg；压缩失败返回原路径sourceImagePath
     */
    fun compressListAsync(
        context: Context?,
        sourceImages: List<String>,
        targetDirPath: String?,
        callBack: Consumer<List<String>?>
    ) {
        val compressImageList: MutableList<String> = ArrayList()
        val position = intArrayOf(0)
//        Luban.with(context)
//            .load(sourceImages)
//            .ignoreBy(200)
//            .setTargetDir(targetDirPath)
//            .filter(object : CompressionPredicate() {
//                fun apply(path: String?): Boolean {
//                    return !(TextUtils.isEmpty(path) || FileUtil.FileType.IMAGE_GIF === FileUtil.fileTypeForImageData(
//                        context,
//                        path
//                    )) || FileUtil.isHttpUrl(path)
//                }
//            })
//            .setRenameListener(object : OnRenameListener() {
//                fun rename(filePath: String?): String? {
//                    return System.currentTimeMillis().toString() + ".jpg"
//                }
//            })
//            .setCompressListener(object : OnCompressListener() {
//                fun onStart() {
//                    position[0]++
//                }
//
//                fun onSuccess(file: File) {
//                    compressImageList.add(file.absolutePath)
//                    if (compressImageList.size == sourceImages.size) {
//                        callBack.accept(compressImageList)
//                    }
//                }
//
//                fun onError(e: Throwable?) {
//                    compressImageList.add(sourceImages[position[0]])
//                    if (compressImageList.size == sourceImages.size) {
//                        callBack.accept(compressImageList)
//                    }
//                }
//            }).launch()
    }

    /**
     * 图片压缩：同步单张压缩，避免在UI线程使用，阻塞线程
     *
     * @param context
     * @param sourceImagePath 原图片路径
     * @param targetDirPath   目标文件夹路径eg:/storage/emulated/0/HHSoftLib/
     * @return 压缩成功返回压缩后的路径eg:/storage/emulated/0/HHSoftLib/1566460459501169.jpeg；压缩失败返回原路径sourceImagePath
     */
    fun compressSync(context: Context?, sourceImagePath: String?, targetDirPath: String?): String? {
//        try {
//            return Luban.with(context)
//                .load(sourceImagePath)
//                .ignoreBy(200)
//                .setTargetDir(targetDirPath)
//                .filter(object : CompressionPredicate() {
//                    fun apply(path: String?): Boolean {
//                        return !(TextUtils.isEmpty(path) || FileUtil.FileType.IMAGE_GIF === FileUtil.fileTypeForImageData(
//                            context,
//                            path
//                        ))
//                    }
//                })
//                .setRenameListener(object : OnRenameListener() {
//                    fun rename(filePath: String?): String? {
//                        return System.currentTimeMillis().toString() + ".jpg"
//                    }
//                }).get(sourceImagePath).getAbsolutePath()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        return sourceImagePath
    }

    /**
     * 图片压缩：同步多张压缩，避免在UI线程使用，阻塞线程
     *
     * @param context
     * @param sourceImageList 原图片路径集合
     * @param targetDirPath   目标文件夹路径eg:/storage/emulated/0/HHSoftLib/
     * @return 图片路径集合，压缩成功返回压缩后的路径eg:/storage/emulated/0/HHSoftLib/1566460459501169.jpeg；压缩失败返回原路径sourceImagePath
     */
    fun compressListSync(
        context: Context?,
        sourceImageList: List<String>?,
        targetDirPath: String?
    ): List<String>? {
//        try {
//            val files: List<File> = Luban.with(context)
//                .load(sourceImageList)
//                .ignoreBy(200)
//                .setTargetDir(targetDirPath)
//                .filter(object : CompressionPredicate() {
//                    fun apply(path: String?): Boolean {
//                        return !(TextUtils.isEmpty(path) || FileUtil.FileType.IMAGE_GIF === FileUtil.fileTypeForImageData(
//                            context,
//                            path
//                        ))
//                    }
//                })
//                .setRenameListener(object : OnRenameListener() {
//                    fun rename(filePath: String?): String? {
//                        return System.currentTimeMillis().toString() + ".jpg"
//                    }
//                }).get()
//            val compressList: MutableList<String> = ArrayList()
//            for (file in files) {
//                compressList.add(file.absolutePath)
//            }
//            return compressList
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        return sourceImageList
    }

    /**
     * 加载长矩形图片
     *
     * @param context
     * @param defaultImageResourceId 占位图片
     * @param imagePath              图片路径
     * @param imageView              ImageView对象
     */
    fun loadBigImage(
        context: Context?,
        defaultImageResourceId: Int,
        imagePath: String?,
        imageView: ImageView?
    ) {
        Glide.with(context!!)
            .asBitmap()
            .load(imagePath)
            .placeholder(defaultImageResourceId)
            .error(defaultImageResourceId)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(imageView!!)
    }
}
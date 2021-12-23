package github.lyl21.wanandroid.util

import android.content.Context
import android.graphics.Bitmap
import com.luck.picture.lib.engine.CompressEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnCallbackListener
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author：luck
 * @date：2021/5/19 9:41 AM
 * @describe：图片压缩引擎
 */
class ImageCompressEngine private constructor() : CompressEngine {
    override fun onCompress(
        context: Context,
        compressData: List<LocalMedia>,
        listener: OnCallbackListener<List<LocalMedia>>
    ) {
        // TODO 1、使用自定义压缩框架进行图片压缩
        GlobalScope.launch {
//            val compressedImageFile = Compressor.compress(context, actualImageFile){
//                resolution(1280, 720)
//                quality(80)
//                format(Bitmap.CompressFormat.JPEG)
//                size(2_097_152) // 2 MB
//            }
        }
        // TODO 2、压缩成功后需要把compressData数据源中的LocalMedia里的isCompress和CompressPath字段赋值

        listener.onCall(compressData)
    }

    companion object {
        private var instance: ImageCompressEngine? = null
        fun createCompressEngine(): ImageCompressEngine? {
            if (null == instance) {
                synchronized(ImageCompressEngine::class.java) {
                    if (null == instance) {
                        instance = ImageCompressEngine()
                    }
                }
            }
            return instance
        }
    }
}

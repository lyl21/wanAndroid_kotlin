package github.lyl21.wanandroid.util

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CustomRoundedCorners(
    leftTopRadius: Int,
    rightTopRadius: Int,
    leftBottomRadius: Int,
    rightBottomRadius: Int
) :
    BitmapTransformation() {
    /**
     * 单位像素
     */
    private var leftTop = 10
    private var rightTop = 10
    private var leftBottom = 10
    private var rightBottom = 10
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool[width, height, Bitmap.Config.ARGB_8888]
        bitmap.setHasAlpha(true)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val radii = floatArrayOf(
            leftTop.toFloat(),
            leftTop.toFloat(),
            rightTop.toFloat(),
            rightTop.toFloat(),
            rightBottom.toFloat(),
            rightBottom.toFloat(),
            leftBottom.toFloat(),
            leftBottom.toFloat()
        )
        val path = Path()
        path.addRoundRect(rect, radii, Path.Direction.CW)
        canvas.drawPath(path, paint)
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "com.bumptech.glide.load.resource.bitmap.CustomRoundedCorners"
        private val ID_BYTES = ID.toByteArray(CHARSET)
    }

    /**
     *
     * @param leftTopRadius
     * @param rightTopRadius
     * @param leftBottomRadius
     * @param rightBottomRadius
     */
    init {
        leftTop = leftTopRadius
        rightTop = rightTopRadius
        leftBottom = leftBottomRadius
        rightBottom = rightBottomRadius
    }
}
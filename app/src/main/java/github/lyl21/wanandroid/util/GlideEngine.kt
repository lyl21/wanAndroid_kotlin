package github.lyl21.wanandroid.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import github.lyl21.wanandroid.R

object GlideEngine : ImageEngine {
    /**
     * 加载图片
     *
     * @param context   上下文
     * @param url       资源url
     * @param imageView 图片承载控件
     */
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        if (!ImageLoaderUtil.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .into(imageView)
    }

    /**
     * 加载网络图片适配长图方案
     *
     * @param context       上下文
     * @param url           资源url
     * @param imageView     图片承载控件
     * @param longImageView 长图承载控件
     * @param callback      网络图片加载回调监听
     */
    override fun loadImage(
        context: Context, url: String,
        imageView: ImageView, longImageView: SubsamplingScaleImageView,
        callback: OnImageCompleteCallback?
    ) {
        if (!ImageLoaderUtil.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    callback?.onShowLoading()
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback?.onHideLoading()
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    callback?.onHideLoading()
                    val eqLongImage = MediaUtils.isLongImg(
                        resource.width,
                        resource.height
                    )
                    longImageView.visibility = if (eqLongImage) View.VISIBLE else View.GONE
                    imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                    if (eqLongImage) {
                        // 加载长图
                        longImageView.isQuickScaleEnabled = true
                        longImageView.isZoomEnabled = true
                        longImageView.setDoubleTapZoomDuration(100)
                        longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                        longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                        longImageView.setImage(
                            ImageSource.cachedBitmap(resource),
                            ImageViewState(0f, PointF(0f, 0f), 0)
                        )
                    } else {
                        // 普通图片
                        imageView.setImageBitmap(resource)
                    }
                }
            })
    }

    /**
     * 加载相册目录
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        if (!ImageLoaderUtil.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(url)
            .override(180, 180)
            .centerCrop()
            .sizeMultiplier(0.5f)
            .placeholder(R.drawable.picture_image_placeholder)
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.resources, resource)
                    circularBitmapDrawable.cornerRadius = 8f
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }


    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        if (!ImageLoaderUtil.assertValidRequest(context)) {
            return
        }
        Glide.with(context)
            .load(url)
            .override(200, 200)
            .centerCrop()
            .placeholder(R.drawable.picture_image_placeholder)
            .into(imageView)
    }


    private var instance: GlideEngine? = null

    fun createGlideEngine(): GlideEngine? {
        if (null == instance) {
            synchronized(GlideEngine::class.java) {
                if (null == instance) {
                    instance = GlideEngine
                }
            }
        }
        return instance
    }
}
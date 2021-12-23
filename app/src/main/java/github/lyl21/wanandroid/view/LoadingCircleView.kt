package github.lyl21.wanandroid.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class LoadingCircleView : AppCompatImageView {
    private var mAnimationDrawable: AnimationDrawable? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    fun startLoadingAnim() {
        if (this.background is AnimationDrawable) {
            mAnimationDrawable = this.background as AnimationDrawable
            post { mAnimationDrawable!!.start() }
        }
    }

    fun stopLoaddingAnim() {
        if (this == null) {
            return
        }
        if (mAnimationDrawable != null && mAnimationDrawable!!.isRunning) {
            mAnimationDrawable!!.stop()
        }
    }
}
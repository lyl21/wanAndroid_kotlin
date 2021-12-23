package github.lyl21.wanandroid.util

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

object TextUtil {

     fun setGradientText(textView: TextView) {
        val endX = textView.paint.textSize * textView.text.length
        val linearGradient = LinearGradient(
            0f, 0f, endX, 0f,
            Color.parseColor("#FFFF68FF"),
            Color.parseColor("#FFFED732"),
            Shader.TileMode.CLAMP
        )
        textView.paint.shader = linearGradient
        textView.invalidate()
    }

}
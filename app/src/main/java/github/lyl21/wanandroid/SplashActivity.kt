package github.lyl21.wanandroid

import BaseActivity
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.adapters.SpinnerBindingAdapter
import androidx.viewbinding.ViewBinding
import github.lyl21.wanandroid.databinding.ActivitySplashBinding
import github.lyl21.wanandroid.moudle.login.LoginActivity


class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    override fun createPresenter() {}


    override fun getLayoutId() = R.layout.activity_splash


    override fun initView() {
        //设置字体渐变
        setGradient(binding.tvSplashTitle)

        object : Thread() {
            override fun run() {
                try {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun setGradient(textView: TextView) {
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

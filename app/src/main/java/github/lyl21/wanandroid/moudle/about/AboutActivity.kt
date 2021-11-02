package github.lyl21.wanandroid.moudle.about

import BaseActivity
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.Utils.getApp
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.databinding.ActivityAboutBinding
import com.google.android.material.snackbar.Snackbar

class AboutActivity : BaseActivity<ActivityAboutBinding>() {


    override fun createPresenter() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        setBackEnabled()
        binding.tvAboutVersionInfo.text = AppUtils.getAppVersionName()
        setGradient(binding.tvAboutVersionInfo)

        binding.aboutGithub.setOnClickListener {
            //复制到粘贴板
            ClipboardUtils.copyText(binding.aboutGithub.text )
            Snackbar.make(binding.llAbout, "复制成功", Snackbar.LENGTH_LONG).show()
        }
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
package github.lyl21.wanandroid.moudle.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.widget.TextView
import github.lyl21.wanandroid.MainActivity
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.common.ConstantParam
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import coil.load
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.base.ui.BaseVMActivity
import github.lyl21.wanandroid.bean.BingImgInfo
import github.lyl21.wanandroid.databinding.ActivitySplashBinding
import github.lyl21.wanandroid.util.LoadingDialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import splitties.views.onClick


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseVMActivity<ActivitySplashBinding, SplashVM>() {

    //设置定时时间
    private var countDownTimer: CountDownTimer? = null
    private var isClicked: Boolean = false

    override fun initData() {
        vm.getTodayBingImg.observe(this) {
            //保存今日图片
            MMKV.defaultMMKV().encode("getTodayBingImg", it)
            LogUtils.e("getTodayBingImg", it)
            db.ivSplashBg.load(it)
        }
    }


    override fun onLoad() {
        val bg = MMKV.defaultMMKV().decodeString("getTodayBingImg")
        if (TextUtils.isEmpty(bg)) {
            vm.getTodayBingImg(ConstantParam.TodayBingImgUrl)
        } else {
            db.ivSplashBg.load(bg)
        }
    }

    override fun onClick(v: View?) {}
    override fun initListener() {}

    override fun initView() {
        //设置字体渐变
        setGradient(db.tvSplashTitle)
        db.tvSplashTitle.onClick {
            isClicked = true
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (!isClicked) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        }.start()
    }

    override fun getLayoutId() = R.layout.activity_splash

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


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel();
    }


    override fun vmClass(): Class<SplashVM> {
        return SplashVM::class.java
    }



}

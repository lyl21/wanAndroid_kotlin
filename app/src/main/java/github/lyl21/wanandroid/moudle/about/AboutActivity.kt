package github.lyl21.wanandroid.moudle.about

import github.lyl21.wanandroid.base.ui.BaseActivity
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.R
import com.google.android.material.snackbar.Snackbar
import github.lyl21.wanandroid.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity<ActivityAboutBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    override fun initListener() {}
    override fun initView() {
//        setBackEnabled()
        supportActionBar?.hide()
        db.title.setTitle("关于")
        db.tvAboutVersionInfo.text = AppUtils.getAppVersionName()
        setGradient(db.tvAboutVersionInfo)

        db.aboutGithub.setOnClickListener {
            //复制到粘贴板
            ClipboardUtils.copyText(db.aboutGithub.text)
            Snackbar.make(db.llAbout, "复制成功", Snackbar.LENGTH_LONG).show()
        }

        ToastUtils.showLong("接收到的数据为：" + intent.getStringExtra("info"))
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("info", "back_info")
            })
            finish()
        }
        return super.onOptionsItemSelected(item)
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
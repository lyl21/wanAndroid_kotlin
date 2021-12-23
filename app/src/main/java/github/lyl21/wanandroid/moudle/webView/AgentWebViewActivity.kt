package github.lyl21.wanandroid.moudle.webView

import github.lyl21.wanandroid.base.ui.BaseActivity
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.webkit.WebSettings
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.R
import com.just.agentweb.AgentWeb
import github.lyl21.wanandroid.databinding.ActivityAgentwebviewBinding

class AgentWebViewActivity : BaseActivity<ActivityAgentwebviewBinding>() {

    companion object {
        const val WEB_URL: String = "web_url"
        const val WEB_TITLE: String = "web_title"
    }

    private lateinit var mAgentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    override fun initListener() {}
    override fun initView() {
        title = intent.getStringExtra(WEB_TITLE)!!
//        setBackEnabled()
        initAgentWeb()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_agentwebview
    }
    private fun getLoadUrl(): String? {
        return intent.getStringExtra(WEB_URL)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(db.webContent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(getLoadUrl())

        val webView = mAgentWeb.webCreator.webView
        //获取手势焦点
        webView.requestFocusFromTouch()
        webView.settings.apply {
            //开启js支持
            javaScriptEnabled = true
            //支持缩放
            setSupportZoom(true)
            builtInZoomControls = true
            //是否显示缩放按钮
            displayZoomControls = false
            //自适应屏幕
            useWideViewPort = true
            loadWithOverviewMode = true
            //设置布局方式，支持内容重新布局
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        }

        val frameLayout=mAgentWeb.webCreator.webParentLayout as FrameLayout
        val title = "技术由 AgentWeb 提供"
        val mTextView = TextView(frameLayout.context)
        mTextView.text = title
        mTextView.textSize = 16f
        mTextView.setTextColor(Color.parseColor("#727779"))
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"))
        val mFlp = FrameLayout.LayoutParams(-2, -2)
        mFlp.gravity = Gravity.CENTER_HORIZONTAL
        val scale: Float = frameLayout.context.resources.displayMetrics.density
        mFlp.topMargin = (15 * scale + 0.5f).toInt()
        frameLayout.addView(mTextView, 0, mFlp)
    }

    /**
     * 事件处理
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    /**
     * 跟随 Activity Or Fragment 生命周期 ， 释放 CPU 更省电 。
     */
    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }


}
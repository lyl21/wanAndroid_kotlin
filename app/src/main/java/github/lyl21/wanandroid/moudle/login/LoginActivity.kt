package github.lyl21.wanandroid.moudle.login

import BaseActivity
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import github.lyl21.wanandroid.MainActivity
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.databinding.ActivityLoginBinding
import github.lyl21.wanandroid.entity.UserInfo
import com.google.android.material.snackbar.Snackbar
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.common.LoadingDialog

class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginView {

    private lateinit var mLoginPresenter: LoginPresenter
    private lateinit var  snackBarLayout: LinearLayout


    override fun createPresenter() {
        mLoginPresenter = LoginPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        snackBarLayout = findViewById(R.id.ll_login)
        //toolbar  返回键
        setBackEnabled()

        setGradient(binding.tvUsername)
        setGradient(binding.tvPwd)

        binding.tvLogin.setOnClickListener {
            checkLogin()
        }
        binding.etPassword.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                checkLogin()
                true
            } else
                false
        }
    }

    private fun checkLogin() {
        KeyboardUtils.hideSoftInput(this)

        binding.etUsername.error = null
        binding.etPassword.error = null

        val username = binding.etUsername.text.toString().trim()
        val pwd = binding.etPassword.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        if (pwd.isEmpty()) {
            binding.etPassword.error = "密码不能为空"
            focusView = binding.etPassword
            cancel = true
        }

        if (username.isEmpty()) {
            binding.etUsername.error = "账号不能为空"
            focusView = binding.etUsername
            cancel = true
        }

        if (cancel)
            focusView?.requestFocus()
        else
            toLogin(username, pwd)
    }

    private fun toLogin(Username: String, pwd: String) {
        LoadingDialog(this).show()
        mLoginPresenter.login(Username, pwd)
    }

    override fun showLoginSuccess(msg: String) {
        LoadingDialog(this).dismiss()
        Snackbar.make(snackBarLayout, msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {

            }).show()
    }

    override fun showLoginFailed(msg: String) {
        LoadingDialog(this).dismiss()
        Snackbar.make(snackBarLayout, msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {

            }).show()
    }

    override fun doSuccess(user: Response<UserInfo>) {
        MMKV.defaultMMKV().encode("isLogin", true)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showLogoutSuccess(msg: String) {
        Snackbar.make(snackBarLayout, msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {

            }).show()
    }

    override fun showLogoutFailed(msg: String) {
        Snackbar.make(snackBarLayout, msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {

            }).show()
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
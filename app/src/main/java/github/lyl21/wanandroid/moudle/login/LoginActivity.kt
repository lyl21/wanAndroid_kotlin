package github.lyl21.wanandroid.moudle.login

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.MainActivity
import github.lyl21.wanandroid.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.ui.BaseVMActivity
import github.lyl21.wanandroid.util.LoadingDialogUtil
import github.lyl21.wanandroid.util.TextUtil

class LoginActivity : BaseVMActivity<ActivityLoginBinding, LoginVM>() {

    private lateinit var snackBarLayout: LinearLayout

    override fun initData() {
        vm.toLogin.observe(this) {
            LoadingDialogUtil(context).dismiss()
            if (-1 == it.errorCode) {
                LogUtils.e("登录失败" + it.errorMsg)
                Snackbar.make(snackBarLayout, it.errorMsg, Snackbar.LENGTH_LONG)
                    .setAction(R.string.sure) {
                        startActivity(Intent(context, MainActivity::class.java))
                    }.show()
            } else {
                LogUtils.e("登录成功")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                //保存登路状态用户名
                MMKV.defaultMMKV().encode("isLogin", true)
                MMKV.defaultMMKV().encode("username", it.data!!.username)
            }
        }
    }

    override fun onLoad() {}
    override fun onClick(v: View?) {}
    override fun initListener() {}
    override fun initView() {
        snackBarLayout = findViewById(R.id.ll_login)
        //toolbar  返回键
        setBackEnabled()

        TextUtil.setGradientText(db.tvUsername)
        TextUtil.setGradientText(db.tvPwd)

        ClickUtils.applySingleDebouncing(db.tvLogin, 2000) {
        }

        db.tvLogin.setOnClickListener {
            checkLogin()
        }


        db.etPassword.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                checkLogin()
                true
            } else
                false
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }


    private fun checkLogin() {
        KeyboardUtils.hideSoftInput(this)

        db.etUsername.error = null
        db.etPassword.error = null

        val username = db.etUsername.text.toString().trim()
        val pwd = db.etPassword.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        if (pwd.isEmpty()) {
            db.etPassword.error = "密码不能为空"
            focusView = db.etPassword
            cancel = true
        }

        if (username.isEmpty()) {
            db.etUsername.error = "账号不能为空"
            focusView = db.etUsername
            cancel = true
        }

        if (cancel)
            focusView?.requestFocus()
        else
            toLogin(username, pwd)
    }

    private fun toLogin(username: String, pwd: String) {
        LoadingDialogUtil(context).show()
        vm.login(username, pwd)
    }

    override fun vmClass(): Class<LoginVM> {
        return LoginVM::class.java
    }




}
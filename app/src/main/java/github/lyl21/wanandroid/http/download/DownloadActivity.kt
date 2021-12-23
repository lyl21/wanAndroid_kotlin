package github.lyl21.wanandroid.http.download

import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.ui.BaseActivity
import github.lyl21.wanandroid.databinding.ActivityDialogBinding

//参数 url-string， file-string
class DialogActivity : BaseActivity<ActivityDialogBinding>() {


    override fun initListener() {
    }

    override fun initView() {
        title = ""
        setFinishOnTouchOutside(true)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dialog
    }



}
package github.lyl21.wanandroid.base.ui

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import github.lyl21.wanandroid.R

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var context: Activity
    lateinit var db: DB

    private var rootView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context=this
        db = DataBindingUtil.setContentView(this,getLayoutId())

        initView()
        initListener()
    }





    abstract fun initListener()
    abstract fun initView()
    abstract fun getLayoutId(): Int


    /**
     * 返回键
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * 统一处理返回键
     */
    protected fun setBackEnabled() {
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }



}

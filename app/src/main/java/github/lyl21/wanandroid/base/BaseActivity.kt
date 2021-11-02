
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.inflateBindingWithGeneric
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.base.BaseView
import com.vmadalin.easypermissions.EasyPermissions
import github.lyl21.wanandroid.common.LoadingDialog


abstract  class BaseActivity<VB : ViewBinding>: AppCompatActivity(), BaseView {

    lateinit var binding: VB
//    protected var presenter: P? = null
    protected abstract fun createPresenter()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBindingWithGeneric(layoutInflater)
        setContentView(binding.root)
        //竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

//        presenter = createPresenter()
//        presenter?.attachView(this)

        createPresenter()

        initView()
    }


    protected fun setMyTitle(title: String) {
        supportActionBar?.title = title
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    override fun showLoading() {
        LoadingDialog(this).show()
    }

    override fun hideLoading() {
        LoadingDialog(this).dismiss()
    }

    override fun onErrorCode(bean: Response<Any>) {

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

    override fun onDestroy() {
        super.onDestroy()
//        presenter?.detachView()
    }

}

package github.lyl21.wanandroid.base.ui


import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider



abstract class BaseVMActivity<DB : ViewDataBinding, VM : BaseVM> : BaseActivity<DB>(),View.OnClickListener {

    lateinit var vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindVM()

        onLoad()
        initData()
    }

    abstract fun initData()
    abstract fun onLoad()

    open fun bindVM() {
//        val vmClass = (this
//            .javaClass
//            .genericSuperclass as ParameterizedType)
//            .actualTypeArguments[0] as Class<VM>

        vm = ViewModelProvider(this)[vmClass()]
//        vm = ViewModelProvider(this)[vmClass]
        db.lifecycleOwner = this
    }

    abstract fun vmClass(): Class<VM>

}

package github.lyl21.wanandroid.base.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider



abstract class BaseVMFragment<DB : ViewDataBinding, VM : BaseVM> : BaseFragment<DB>(),
    View.OnClickListener {

    lateinit var vm: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindVM()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLoad()
        initData()
    }

    abstract fun initData()
    abstract fun onLoad()


//    fun showPageContent(){
//
//    }
//
//    fun showLoading(){
//
//    }
//
//    fun showEmpty(){
//
//    }
//
//    fun showFailed(){
//
//    }

    /**
     * 失败重试,加载事件
     */
//     abstract fun onRetryBtnClick()


//    open fun setLoadSir(view: View?) {
//        mLoadService = LoadSir.getDefault()
//            .register(view,
//                Callback.OnReloadListener { v: View? -> onRetryBtnClick() } as Callback.OnReloadListener)
//    }


    private fun bindVM() {
//        val vmClass = (this
//            .javaClass
//            .genericSuperclass as ParameterizedType)
//            .actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this)[vmClass()]
    }

    abstract fun vmClass(): Class<VM>

}
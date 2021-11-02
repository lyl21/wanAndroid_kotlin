import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.dylanc.viewbinding.base.inflateBindingWithGeneric
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.base.BaseView
import github.lyl21.wanandroid.common.LoadingDialog


abstract class BaseFragment<VB : ViewBinding> : Fragment(), BaseView {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!
    protected lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view: View = inflater.inflate(getLayoutId(), container, false)
        mContext = ActivityUtils.getTopActivity()!!
        createPresenter()
//        return view
        _binding = inflateBindingWithGeneric(layoutInflater, container, false)
        return binding.root
    }

    /**
     * 初始化方法要放在onViewCreated或者onActivityCreated方法里，不然找不到控件
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPresenter()
        initView()
        initData()
    }

    /**
     * 懒加载，当Fragment显示的时候再请求数据
     * 如果数据不需要每次都刷新，可以先判断数据是否存在
     * 不存在 -> 请求数据，存在 -> 什么都不做
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
//        if (hidden) initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun createPresenter()

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    override fun showLoading() {
        LoadingDialog(ActivityUtils.getTopActivity()!!).show()
    }

    override fun hideLoading() {
        LoadingDialog(ActivityUtils.getTopActivity()!!).dismiss()
    }

    override fun onErrorCode(bean: Response<Any>) {

    }
}
package github.lyl21.wanandroid.base.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.zy.multistatepage.MultiStateContainer
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.databinding.BaseFgLayoutBinding


abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    private lateinit var multiStateContainer: MultiStateContainer
    lateinit var mBaseContainBinding: BaseFgLayoutBinding
    lateinit var db: DB
    lateinit var context: Activity
    internal var view: View? = null
    var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (view == null) {
            db = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            view = db.root
            isInit = true
        } else {
            val parent = requireView().parent as ViewGroup
            parent.removeView(view)
        }
        if (view != null) isInit = false

        initView()
        initListener()

        return view
    }



    abstract fun initListener()
    abstract fun initView()
    abstract fun getLayoutId(): Int


}
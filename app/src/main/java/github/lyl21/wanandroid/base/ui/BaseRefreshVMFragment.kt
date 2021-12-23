package github.lyl21.wanandroid.base.ui

import android.view.View
import androidx.databinding.ViewDataBinding
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener


abstract class BaseRefreshVMFragment< DB : ViewDataBinding,VM : BaseVM> : BaseVMFragment<DB,VM>(),
    View.OnClickListener,  OnRefreshListener {


    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.autoRefresh()
        onLoad()
        refreshLayout.finishRefresh()
    }


}
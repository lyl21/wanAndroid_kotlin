package github.lyl21.wanandroid.base.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import java.lang.reflect.ParameterizedType


abstract class BaseRefreshVMActivity<DB : ViewDataBinding, VM : BaseVM> :BaseVMActivity<DB,VM>(),
    View.OnClickListener, OnRefreshListener {


    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.autoRefresh()
        onLoad()
        refreshLayout.finishRefresh()
    }



}

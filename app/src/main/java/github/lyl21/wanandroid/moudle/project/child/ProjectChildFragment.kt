package github.lyl21.wanandroid.moudle.project.child

import github.lyl21.wanandroid.base.ui.BaseRefreshVMFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.ProjectChildAdapter
import github.lyl21.wanandroid.bean.ProjectClassInfoChild
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.databinding.FragmentProjectChildBinding
import github.lyl21.wanandroid.moudle.home.HomeFragment
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import github.lyl21.wanandroid.util.LoadingDialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectChildFragment : BaseRefreshVMFragment<FragmentProjectChildBinding, ProjectChildVM>(),
    OnLoadMoreListener, OnItemClickListener {
    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数

        /**
         * 创建fragment
         */
        fun newInstance(cid: Int): ProjectChildFragment {
            val projectChildFragment = ProjectChildFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            projectChildFragment.arguments = bundle
            return projectChildFragment
        }
    }


    private var mCid: Int = 0
    private var mDataList: MutableList<ProjectClassInfoChild> = mutableListOf()
    private val mProjectChildAdapter by lazy { ProjectChildAdapter() }


    override fun initData() {
        vm.getProjectChildInfo.observe(this) {
            CURRENT_SIZE = it.data!!.size
            if (it.data!!.datas!!.isNotEmpty()) {
                db.fragmentProjectChild.finishLoadMore()
            } else {
                db.fragmentProjectChild.finishLoadMoreWithNoMoreData()
            }
            if (CURRENT_PAGE > 0) {
                mDataList.addAll(it.data!!.datas!!)
                mProjectChildAdapter.addData(it.data!!.datas!!)
            } else {
                mDataList = it.data!!.datas!!
                mProjectChildAdapter.setList(mDataList)
            }
        }
    }

    override fun onLoad() {
        mCid = arguments?.getInt("cid")!!
        vm.getProjectChildInfo(0, mCid)
    }

    override fun onClick(v: View?) {}

    override fun initListener() {
//        db.fragmentProjectChild.setOnRefreshListener(this)
        mProjectChildAdapter.run {
            animationEnable = true
            setOnItemClickListener(this@ProjectChildFragment)
        }
    }

    override fun initView() {
        db.rvProjectChild.adapter = mProjectChildAdapter

        db.rvProjectChild.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project_child
    }


    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (CURRENT_SIZE >= ConstantParam.PAGE_SIZE) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            CURRENT_PAGE++
            vm.getProjectChildInfo(CURRENT_PAGE, mCid)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        startActivity(Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, mDataList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, mDataList[position].title)
        })
    }

    override fun vmClass(): Class<ProjectChildVM> {
        return ProjectChildVM::class.java
    }


}
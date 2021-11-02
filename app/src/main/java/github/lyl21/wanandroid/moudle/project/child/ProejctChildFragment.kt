package github.lyl21.wanandroid.moudle.project.child

import BaseFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.ProjectChildAdapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.config.ConstantParam
import github.lyl21.wanandroid.databinding.FragmentProjectChildBinding
import github.lyl21.wanandroid.entity.ProjectClassInfoChild
import github.lyl21.wanandroid.entity.ProjectClassInfoChildData
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity

class ProjectChildFragment : BaseFragment<FragmentProjectChildBinding>(), ProjectChildView, OnLoadMoreListener,
    OnItemClickListener {
    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 1//当前加载页数

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
    private lateinit var mProjectChildPresenter: ProjectChildPresenter
    private lateinit var mDataList: MutableList<ProjectClassInfoChildData>
    private lateinit var mProjectChildAdapter: ProjectChildAdapter

    override fun createPresenter() {
        mProjectChildPresenter = ProjectChildPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project_child
    }

    override fun initView() {
        binding.rvProjectChild.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun initData() {
        mCid = arguments?.getInt("cid")!!
        mProjectChildPresenter.getProjectChildInfo(CURRENT_PAGE, mCid)
    }

    override fun getProjectChild(projectChild: Response<ProjectClassInfoChild>) {
        CURRENT_SIZE = projectChild.data.datas.size
        mDataList = projectChild.data.datas
        mProjectChildAdapter = ProjectChildAdapter().apply {
            setOnItemClickListener(this@ProjectChildFragment)
            loadMoreModule.setOnLoadMoreListener(this@ProjectChildFragment)
        }
        binding.rvProjectChild.adapter = mProjectChildAdapter
        mProjectChildAdapter.setList(mDataList)
    }

    override fun getProjectChildError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun getProjectChildMore(projectChild: Response<ProjectClassInfoChild>) {
        CURRENT_SIZE = projectChild.data.datas.size
        mDataList.addAll(projectChild.data.datas)
        mProjectChildAdapter.addData(projectChild.data.datas)
        mProjectChildAdapter.loadMoreModule.loadMoreComplete()
    }


    override fun getProjectChildMoreError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun onLoadMore() {
        if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
            mProjectChildAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            CURRENT_PAGE++
            mProjectChildPresenter.getProjectChildMore(CURRENT_PAGE, mCid)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val intent = Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, mDataList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, mDataList[position].title)
        }
        startActivity(intent)
    }
}
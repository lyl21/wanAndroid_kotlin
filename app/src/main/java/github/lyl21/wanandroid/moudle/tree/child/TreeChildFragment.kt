package github.lyl21.wanandroid.moudle.tree.child

import BaseFragment
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.ArticleAdapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.config.ConstantParam
import github.lyl21.wanandroid.databinding.FragmentTreeChildBinding
import github.lyl21.wanandroid.entity.ArticleDetail
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.yechaoa.yutilskt.ToastUtil

class TreeChildFragment : BaseFragment<FragmentTreeChildBinding>(), TreeChildView,
    OnLoadMoreListener,
    OnItemChildClickListener, OnItemClickListener {

    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数

        fun newInstance(cid: Int): TreeChildFragment {
            val treeChildFragment = TreeChildFragment()
            val bundle = Bundle()
            bundle.putInt("cid", cid)
            treeChildFragment.arguments = bundle
            return treeChildFragment
        }
    }

    private var mCid: Int = 0
    private lateinit var mTreeChildPresenter: TreeChildPresenter
    private lateinit var treeChildList: MutableList<ArticleDetail>
    private lateinit var mArticleAdapter: ArticleAdapter
    private var mPosition: Int = 0

    override fun createPresenter() {
        mTreeChildPresenter = TreeChildPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tree_child
    }

    override fun initView() {
    }

    override fun initData() {
        mCid = arguments?.getInt("cid")!!
        mTreeChildPresenter.getTreeChild(CURRENT_PAGE, mCid)
    }

    override fun getTreeChild(treeChild: Response<ArticleInfo>) {
        CURRENT_SIZE = treeChild.data.datas.size
        treeChildList = treeChild.data.datas
        mArticleAdapter = ArticleAdapter().apply {
            setOnItemClickListener(this@TreeChildFragment)
            setOnItemChildClickListener(this@TreeChildFragment)
            loadMoreModule.setOnLoadMoreListener(this@TreeChildFragment)
        }

        binding.rvTreeChild.adapter = mArticleAdapter
        mArticleAdapter.setList(treeChild.data.datas)
    }

    override fun getTreeChildError(msg: String) {
        ToastUtil.show(msg)
    }

    override fun getTreeMoreChild(treeChild: Response<ArticleInfo>) {
        CURRENT_SIZE = treeChild.data.datas.size
        treeChildList.addAll(treeChild.data.datas)
        mArticleAdapter.addData(treeChild.data.datas)
        mArticleAdapter.loadMoreModule.loadMoreComplete()
    }

    override fun getTreeChildMoreError(msg: String) {
        ToastUtil.show(msg)
    }

    override fun login(msg: String) {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.tips)
            setMessage(msg)
            setPositiveButton(R.string.sure) { _, _ ->
.0
                startActivity(Intent(mContext, LoginActivity::class.java))
            }
            setNegativeButton(R.string.cancel, null)
        }
        builder.create().show()
    }


    override fun collect(msg: String) {
        treeChildList[mPosition].collect = true
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun unCollect(msg: String) {
        treeChildList[mPosition].collect = false
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun onLoadMore() {
        if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
            mArticleAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            CURRENT_PAGE++
            mTreeChildPresenter.getTreeMoreChild(CURRENT_PAGE, mCid)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val intent = Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, treeChildList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, treeChildList[position].title)
        }
        startActivity(intent)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position
        if (treeChildList[position].collect) {
            mTreeChildPresenter.unCollect(treeChildList[position].id)
        } else {
            mTreeChildPresenter.collect(treeChildList[position].id)
        }
    }


}
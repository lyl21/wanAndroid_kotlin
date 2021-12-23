package github.lyl21.wanandroid.moudle.tree.child

import android.content.DialogInterface
import github.lyl21.wanandroid.base.ui.BaseRefreshVMFragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.ArticleAdapter
import github.lyl21.wanandroid.base.httpResult.BasePagingResult
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.databinding.FragmentTreeChildBinding
import github.lyl21.wanandroid.bean.ArticleInfo
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import github.lyl21.wanandroid.util.DialogUtil

class TreeChildFragment : BaseRefreshVMFragment<FragmentTreeChildBinding, TreeChildVM>(),
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
    private lateinit var treeChildList: MutableList<ArticleInfo>
    private lateinit var mArticleAdapter: ArticleAdapter
    private var mPosition: Int = 0

    override fun initData() {
        mCid = arguments?.getInt("cid")!!
            vm.getTreeChild.observe(this){
                if (CURRENT_PAGE > 0) {
                    getTreeMoreChild(it)
                } else {
                    getTreeChild(it)
                }
            }

            vm.toCollect.observe(this){
                if (-1001 == it.errorCode) {
                    //去设置密码
                    DialogUtil.showTip(
                        context,
                        it.errorMsg,
                        DialogInterface.OnClickListener { dialog, which ->
                            startActivity(Intent(context, LoginActivity::class.java))
                        }
                    )
                } else {
                    treeChildList[mPosition].collect = true
                    mArticleAdapter.notifyDataSetChanged()
                    ToastUtils.showShort("收藏成功")
                }
            }


            vm.unCollect.observe(this){
                treeChildList[mPosition].collect = false
                mArticleAdapter.notifyDataSetChanged()
                ToastUtils.showShort("取消收藏")
            }
        }


    override fun onLoad() {
        vm.getTreeChild(0, mCid)
    }
    override fun onClick(v: View?) {
    }

    override fun initListener() {
        mArticleAdapter = ArticleAdapter().apply {
            setOnItemClickListener(this@TreeChildFragment)
            setOnItemChildClickListener(this@TreeChildFragment)
            loadMoreModule.setOnLoadMoreListener(this@TreeChildFragment)
        }
    }

    override fun initView() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tree_child
    }



    private fun getTreeChild(treeChild: BaseResult<BasePagingResult<MutableList<ArticleInfo>>>) {
        CURRENT_SIZE = treeChild.data!!.datas!!.size
        treeChildList = treeChild.data!!.datas!!

        db.rvTreeChild.adapter = mArticleAdapter
        mArticleAdapter.setList(treeChild.data!!.datas)
    }

    private fun getTreeMoreChild(treeChild: BaseResult<BasePagingResult<MutableList<ArticleInfo>>>) {
        CURRENT_SIZE = treeChild.data!!.datas!!.size
        treeChildList.addAll(treeChild.data!!.datas!!)
        mArticleAdapter.addData(treeChild.data!!.datas!!)
        mArticleAdapter.loadMoreModule.loadMoreComplete()
    }

    override fun onLoadMore() {
        if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
            mArticleAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            CURRENT_PAGE++
            vm.getTreeChild(CURRENT_PAGE, mCid)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        startActivity(Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, treeChildList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, treeChildList[position].title)
        })
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position
        if (treeChildList[position].collect) {
            vm.unCollect(treeChildList[position].id)
        } else {
            vm.toCollect(treeChildList[position].id)
        }
    }

    override fun vmClass(): Class<TreeChildVM> {
        return TreeChildVM::class.java
    }




}
package github.lyl21.wanandroid.moudle.collect

import BaseActivity
import BaseFragment
import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.CollectAdapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.config.ConstantParam
import github.lyl21.wanandroid.databinding.ActivityCollectBinding
import github.lyl21.wanandroid.entity.CollectDetail
import github.lyl21.wanandroid.entity.CollectInfo
import github.lyl21.wanandroid.moudle.home.HomeFragment
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity

class CollectActivity : BaseActivity<ActivityCollectBinding>(), CollectView, OnLoadMoreListener,
    OnItemClickListener, OnItemChildClickListener {

    private lateinit var collectPresenter: CollectPresenter
    private lateinit var infoList: MutableList<CollectDetail>
    private lateinit var adapter: CollectAdapter
    private var mPosition: Int = 0


    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数
    }


    override fun createPresenter() {
        collectPresenter = CollectPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_collect
    }

    override fun initView() {
        title = "我的收藏"
        setBackEnabled()
        collectPresenter.getCollectList(CURRENT_PAGE)
    }


    override fun unCollect(msg: String) {
        ToastUtils.showLong(msg)
        collectPresenter.getCollectList(0)
        adapter.notifyDataSetChanged()
    }

    override fun getCollectList(info: Response<CollectInfo>) {

        CURRENT_SIZE = info.data.datas.size
        infoList = info.data.datas

        LogUtils.e("lll", infoList.toString())

        if (infoList == null) {
            binding.tvCollectNoData.isVisible = true
            binding.rvCollect.isVisible = false
        } else {
            binding.tvCollectNoData.isVisible = false
            binding.rvCollect.isVisible = true
        }

        adapter = CollectAdapter().apply {
            animationEnable = true
            setOnItemClickListener(this@CollectActivity)
            setOnItemChildClickListener(this@CollectActivity)
            loadMoreModule.setOnLoadMoreListener(this@CollectActivity)
        }
        binding.rvCollect.adapter = adapter
        adapter.setList(infoList)
    }

    override fun getCollectListError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun getCollectMoreList(info: Response<CollectInfo>) {
        infoList.addAll(info.data.datas)
        CURRENT_SIZE = info.data.datas.size
        adapter.addData(info.data.datas)
        adapter.loadMoreModule.loadMoreComplete()
    }

    override fun getCollectMoreListError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun onLoadMore() {
        binding.rvCollect.postDelayed({
            if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
                adapter.loadMoreModule.loadMoreEnd(true)
            } else {
                CURRENT_PAGE++
                collectPresenter.getCollectMoreList(CURRENT_PAGE)
            }
        }, 0)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val intent = Intent(this, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, infoList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, infoList[position].title)
        }
        startActivity(intent)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position
        var originId: Int = infoList[position].originId
        if (originId==null){
            originId=-1
        }
        // TODO: 2021/10/13 api 参数
//        collectPresenter.unMyCollect(infoList[position].id,originId)
    }

}
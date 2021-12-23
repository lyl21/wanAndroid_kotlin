package github.lyl21.wanandroid.moudle.collect

import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.CollectAdapter
import github.lyl21.wanandroid.base.ui.BaseVMActivity
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.bean.CollectInfo
import github.lyl21.wanandroid.databinding.ActivityCollectBinding
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import github.lyl21.wanandroid.util.LoadingDialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectActivity : BaseVMActivity<ActivityCollectBinding, CollectVM>(), OnLoadMoreListener,
    OnItemClickListener, OnItemChildClickListener {

    private lateinit var infoList: MutableList<CollectInfo>
    private lateinit var adapter: CollectAdapter
    private var mPosition: Int = 0
    private var isOnLoadMore = false

    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数
    }


    override fun initData() {
        vm.getCollectList.observe(this) {
            if (isOnLoadMore) {
                CURRENT_SIZE = it.data!!.size
                infoList.addAll(it.data!!.datas!!)
                adapter.addData(it.data!!.datas!!)
                adapter.loadMoreModule.loadMoreComplete()
            } else {
                CURRENT_SIZE = it.data!!.size
                infoList = it.data!!.datas!!
                LogUtils.e("lll", infoList.toString())
                if (infoList.isEmpty()) {
                    db.tvCollectNoData.isVisible = true
                    db.rvCollect.isVisible = false
                } else {
                    db.tvCollectNoData.isVisible = false
                    db.rvCollect.isVisible = true
                }
                adapter.setList(infoList)
            }
        }
    }

    override fun onLoad() {
        vm.getCollectList(0)
    }

    override fun onClick(v: View?) {
    }

    override fun initListener() {}
    override fun initView() {
        title = "我的收藏"
//        setBackEnabled()

        adapter = CollectAdapter().apply {
            animationEnable = true
            setOnItemClickListener(this@CollectActivity)
            setOnItemChildClickListener(this@CollectActivity)
            loadMoreModule.setOnLoadMoreListener(this@CollectActivity)
        }
        db.rvCollect.adapter = adapter
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_collect
    }


//    override fun unCollect(msg: String) {
//        ToastUtils.showLong(msg)
//        collectPresenter.getCollectList(0)
//        adapter.notifyDataSetChanged()
//    }

    override fun onLoadMore() {
        db.rvCollect.postDelayed({
            if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
                adapter.loadMoreModule.loadMoreEnd(true)
            } else {
                CURRENT_PAGE++
                vm.getCollectList(CURRENT_PAGE)
            }
        }, 2000)
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
        if (originId == null) {
            originId = -1
        }
        // TODO: 2021/10/13 api 参数
//        collectPresenter.unMyCollect(infoList[position].id,originId)
    }

    override fun vmClass(): Class<CollectVM> {
        return CollectVM::class.java
    }



}
package github.lyl21.wanandroid.moudle.navi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import github.lyl21.wanandroid.databinding.FragmentNaviBinding
import github.lyl21.wanandroid.bean.NaviArticleDetail
import github.lyl21.wanandroid.bean.NaviInfo
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.NaviLeftListAdapter
import github.lyl21.wanandroid.base.ui.BaseVMFragment
import github.lyl21.wanandroid.util.LoadingDialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NaviFragment : BaseVMFragment<FragmentNaviBinding, NaviVM>(),
    TagFlowLayout.OnTagClickListener, OnItemClickListener {

    private lateinit var naviLeftListAdapter: NaviLeftListAdapter

    private lateinit var mNaviList: MutableList<NaviInfo>
    private lateinit var mArticles: MutableList<NaviArticleDetail>

    override fun initData() {
            vm.getNaviInfo.observe(this){
                mNaviList = it.data!!
                // 默认选中第一个
                mNaviList[0].isChecked = true
                naviLeftListAdapter.setList(mNaviList)
                //默认选中第一个
                mArticles = mNaviList[0].articles
                setFlowLayout(mArticles)
            }
    }

    override fun onLoad() {
        vm.getNaviInfo()
    }
    override fun onClick(v: View?) {}
    override fun initListener() {
        naviLeftListAdapter.setOnItemClickListener(this)
    }

    override fun initView() {

        naviLeftListAdapter = NaviLeftListAdapter()
        db.fragmentNaviRv.adapter = naviLeftListAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_navi
    }


    /**
     * 填充FlowLayout数据
     */
    private fun setFlowLayout(articles: MutableList<NaviArticleDetail>) {
        db.naviFlowLayout.adapter = object : TagAdapter<NaviArticleDetail>(articles) {
            override fun getView(parent: FlowLayout, position: Int, s: NaviArticleDetail): View {
                val tvTag = LayoutInflater.from(activity).inflate(
                    R.layout.item_navi,
                    db.naviFlowLayout, false
                ) as TextView
                tvTag.text = s.title
                return tvTag
            }
        }
        //设置点击事件
        db.naviFlowLayout.setOnTagClickListener(this)
    }

    override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        val intent = Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, mArticles[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, mArticles[position].title)
        }
        startActivity(intent)
        return true
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        db.fragmentNaviRv.layoutManager?.smoothScrollToPosition(
            db.fragmentNaviRv,
            null,
            position
        )
        mNaviList[position].isChecked = true
        naviLeftListAdapter.notifyDataSetChanged()
        mArticles = mNaviList[position].articles
        setFlowLayout(mArticles)
    }

    override fun vmClass(): Class<NaviVM> {
        return NaviVM::class.java
    }



}
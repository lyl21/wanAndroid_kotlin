package github.lyl21.wanandroid.moudle.navi

import BaseFragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.databinding.FragmentNaviBinding
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.NaviArticleDetail
import github.lyl21.wanandroid.entity.NaviInfo
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView

class NaviFragment : BaseFragment<FragmentNaviBinding>(), NaviView, VerticalTabLayout.OnTabSelectedListener,
    TagFlowLayout.OnTagClickListener {

    private lateinit var mNaviPresenter: NaviPresenter
    private lateinit var mNaviList: MutableList<NaviInfo>
    private lateinit var mArticles: MutableList<NaviArticleDetail>

    override fun createPresenter() {
        mNaviPresenter = NaviPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_navi
    }

    override fun initView() {
    }

    override fun initData() {
        mNaviPresenter.getNaviInfo()
    }


    override fun getNavi(navi: Response<MutableList<NaviInfo>>) {
        mNaviList = navi.data
        binding.verticalTabLayout.setTabAdapter(object : TabAdapter {
            override fun getCount(): Int {
                return navi.data.size
            }

            override fun getBadge(position: Int): ITabView.TabBadge? {
                return null
            }

            override fun getIcon(position: Int): ITabView.TabIcon? {
                return null
            }

            override fun getTitle(position: Int): ITabView.TabTitle {
                return ITabView.TabTitle.Builder()
                    .setContent(navi.data[position].name)
                    .setTextColor(-0xde690d, -0x8a8a8b)
                    .setTextSize(16)
                    .build()
            }

            override fun getBackground(position: Int): Int {
                return 0
            }
        })
        /**
         * 设置点击事件
         */
        binding.verticalTabLayout.addOnTabSelectedListener(this)
        /**
         * 默认选中第一个
         */
        mArticles = mNaviList[0].articles
        setFlowLayout(mArticles)
    }

    /**
     * 填充FlowLayout数据
     */
    private fun setFlowLayout(articles: MutableList<NaviArticleDetail>) {
        binding.naviFlowLayout.adapter = object : TagAdapter<NaviArticleDetail>(articles) {
            override fun getView(parent: FlowLayout, position: Int, s: NaviArticleDetail): View {
                val tvTag = LayoutInflater.from(activity).inflate(
                    R.layout.item_navi,
                    binding.naviFlowLayout, false
                ) as TextView
                tvTag.text = s.title
                return tvTag
            }
        }
        //设置点击事件
        binding.naviFlowLayout.setOnTagClickListener(this)
    }

    override fun getNaviError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun onTabSelected(tab: TabView?, position: Int) {
        mArticles = mNaviList[position].articles
        setFlowLayout(mArticles)
    }


    /**
     * Tab选中Tab再次选中
     */
    override fun onTabReselected(tab: TabView?, position: Int) {
    }

    override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        val intent = Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, mArticles[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, mArticles[position].title)
        }
        startActivity(intent)
        return true
    }
}
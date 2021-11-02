package github.lyl21.wanandroid.moudle.search

import BaseActivity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.ArticleAdapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.config.ConstantParam
import github.lyl21.wanandroid.databinding.ActivitySearchBinding
import github.lyl21.wanandroid.entity.ArticleDetail
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.HotSearchInfo
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import java.lang.StringBuilder
import java.util.*

class SearchActivity : BaseActivity<ActivitySearchBinding>(), SearchView, OnItemClickListener,
    OnLoadMoreListener,
    OnItemChildClickListener, TagFlowLayout.OnTagClickListener {


    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数
    }

    lateinit var mSearchPresenter: SearchPresenter
    private lateinit var mDataList: MutableList<ArticleDetail>
    private lateinit var mArticleAdapter: ArticleAdapter
    private lateinit var mKey: String
    private var mPosition: Int = 0
    private lateinit var mHotkeyList: MutableList<HotSearchInfo>

    private lateinit var mEditText: EditText

    override fun createPresenter() {
        mSearchPresenter = SearchPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initView() {
        setMyTitle("搜索")
        setBackEnabled()
        mSearchPresenter.getHotSearchInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //引用menu文件
        menuInflater.inflate(R.menu.menu_search, menu)

        //找到SearchView并配置相关参数
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView: androidx.appcompat.widget.SearchView = MenuItemCompat.getActionView(searchItem) as androidx.appcompat.widget.SearchView

        searchView.apply {
            //搜索图标是否显示在搜索框内
            setIconifiedByDefault(true)
            //设置搜索框展开时是否显示提交按钮，可不显示
            isSubmitButtonEnabled = false
            //让键盘的回车键设置成搜索
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            //搜索框是否展开，false表示展开
            isIconified = true
            //获取焦点
            isFocusable = true
            requestFocusFromTouch()
            //设置提示词
            queryHint = "请输入关键字"
        }
        //设置输入框文字颜色
        mEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        mEditText.setHintTextColor(ContextCompat.getColor(this, R.color.white))
        mEditText.setTextColor(ContextCompat.getColor(this, R.color.white))

        //设置搜索文本监听
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            // 当搜索内容改变时触发该方法
            override fun onQueryTextChange(newText: String): Boolean {
                //当没有输入任何内容的时候显示搜索热词，看实际需求
                binding.llHotkey.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
                return false
            }

            // 当点击搜索按钮时触发该方法
            override fun onQueryTextSubmit(query: String): Boolean {
              LogUtils.e("搜索内容", "搜索内容===$query")
                mKey = query
                CURRENT_PAGE = 0 //重置分页，避免二次加载分页混乱
                //搜索请求
                mSearchPresenter.getArticleInfoList(CURRENT_PAGE, mKey)
                //清除焦点，收软键盘
                searchView.clearFocus()
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun getHotkey(hotkey: Response<MutableList<HotSearchInfo>>) {
        mHotkeyList = hotkey.data
        binding.flowLayout.adapter = object : TagAdapter<HotSearchInfo>(mHotkeyList) {
            override fun getView(parent: FlowLayout, position: Int, s: HotSearchInfo): View {
                val tvTag = LayoutInflater.from(this@SearchActivity).inflate(
                    R.layout.item_navi,
                    binding.flowLayout, false
                ) as TextView
                tvTag.text = s.name
                tvTag.setTextColor(randomColor())
                return tvTag
            }
        }
        //设置点击事件
        binding.flowLayout.setOnTagClickListener(this)
    }

    /**
     * 随机颜色
     */
    fun randomColor(): Int {
        Random().run {
            //rgb取值0-255，但是值过大,就越接近白色,会看不清,所以限制在200
            val red = nextInt(200)
            val green = nextInt(200)
            val blue = nextInt(200)
            return Color.rgb(red, green, blue)
        }
    }

    override fun getHotkeyError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun getArticleList(article: Response<ArticleInfo>) {
        binding.rvSearch.visibility = View.VISIBLE
        binding.llHotkey.visibility = View.GONE

        CURRENT_SIZE = article.data.datas.size
        mDataList = article.data.datas
        mArticleAdapter = ArticleAdapter().apply {
            animationEnable = true
            //item点击事件
            setOnItemClickListener(this@SearchActivity)
            //item子view点击事件
            setOnItemChildClickListener(this@SearchActivity)
            //加载更多
            loadMoreModule.setOnLoadMoreListener(this@SearchActivity)
        }
        binding.rvSearch.adapter = mArticleAdapter
        mArticleAdapter.setList(mDataList)
    }

    override fun getArticleError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun getArticleMoreList(article: Response<ArticleInfo>) {
        CURRENT_SIZE = article.data.datas.size
        mDataList.addAll(article.data.datas)
        mArticleAdapter.addData(article.data.datas)
        mArticleAdapter.loadMoreModule.loadMoreComplete()
    }

    override fun getArticleMoreError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun login(msg: String) {
        val builder = AlertDialog.Builder(this@SearchActivity).apply {
            setTitle(R.string.tips)
            setMessage(msg)
            setPositiveButton(R.string.sure) { _, _ ->
                startActivity(Intent(this@SearchActivity, LoginActivity::class.java))
            }
            setNegativeButton(R.string.cancel, null)
        }
        builder.create().show()
    }

    override fun collect(msg: String) {
        ToastUtils.showLong(msg)
        mDataList[mPosition].collect = true
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun unCollect(msg: String) {
        ToastUtils.showLong(msg)
        mDataList[mPosition].collect = true
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val intent = Intent(this, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, mDataList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, mDataList[position].title)
        }
        startActivity(intent)
    }

    override fun onLoadMore() {
        if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
            mArticleAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            CURRENT_PAGE++
            mSearchPresenter.getArticleInfoMoreList(CURRENT_PAGE, mKey)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position
        if (mDataList[position].collect) {
            mSearchPresenter.unCollect(mDataList[position].id)
        } else {
            mSearchPresenter.collect(mDataList[position].id)
        }
    }

    override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        KeyboardUtils.hideSoftInput()
        mKey = mHotkeyList[position].name
        //填充搜索框
        mEditText.setText(mKey)
        CURRENT_PAGE = 0 //重置分页，避免二次加载分页混乱
        mSearchPresenter.getArticleInfoList(CURRENT_PAGE, mKey)
        return true
    }
}
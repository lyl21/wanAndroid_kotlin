package github.lyl21.wanandroid.moudle.search

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
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.adapter.ArticleAdapter
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.databinding.ActivitySearchBinding
import github.lyl21.wanandroid.bean.HotSearchInfo
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.ui.BaseVMActivity
import github.lyl21.wanandroid.bean.ArticleInfo
import github.lyl21.wanandroid.util.LoadingDialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class SearchActivity : BaseVMActivity<ActivitySearchBinding, SearchVM>(), OnItemClickListener,
    OnLoadMoreListener,
    OnItemChildClickListener, TagFlowLayout.OnTagClickListener {
    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数
    }

    private lateinit var mHotkeyList: MutableList<HotSearchInfo>
    private lateinit var mDataList: MutableList<ArticleInfo>
    private lateinit var mArticleAdapter: ArticleAdapter
    private lateinit var mKey: String
    private var mPosition: Int = 0

    private lateinit var mEditText: EditText


    override fun initData() {
        vm.getHotSearchInfo.observe(this) {
            mHotkeyList = it.data!!
            db.flowLayout.adapter = object : TagAdapter<HotSearchInfo>(mHotkeyList) {
                override fun getView(
                    parent: FlowLayout,
                    position: Int,
                    s: HotSearchInfo
                ): View {
                    val tvTag = LayoutInflater.from(this@SearchActivity).inflate(
                        R.layout.item_navi,
                        db.flowLayout, false
                    ) as TextView
                    tvTag.text = s.name
                    tvTag.setTextColor(randomColor())
                    return tvTag
                }
            }
        }

        vm.getSearchList.observe(this) {
            db.rvSearch.visibility = View.VISIBLE
            db.llHotkey.visibility = View.GONE

            mDataList.addAll(it.data!!.datas!!)
            mArticleAdapter.addData(it.data!!.datas!!)
            if (CURRENT_PAGE > 0) {
                mArticleAdapter.loadMoreModule.loadMoreComplete()
            }

//            CURRENT_SIZE = data.data.datas.size
//            mDataList = data.data.datas
//
//            mArticleAdapter.setList(mDataList)
            vm.toCollect.observe(this) {
                if (-1001 == it.errorCode) {
                    toLogin(it.errorMsg)
                } else {
                    mDataList[mPosition].collect = true
                    mArticleAdapter.notifyDataSetChanged()
                    ToastUtils.showShort("收藏成功")
                }
            }

            vm.unCollect.observe(this) {
                mDataList[mPosition].collect = false
                mArticleAdapter.notifyDataSetChanged()
                ToastUtils.showShort("取消收藏")
            }
        }
    }

    override fun onLoad() {
        vm.getHotSearchInfo()
    }

    override fun onClick(v: View?) {}
    override fun initListener() {
        db.flowLayout.setOnTagClickListener(this)
        mArticleAdapter = ArticleAdapter().apply {
            animationEnable = true
            //item点击事件
            setOnItemClickListener(this@SearchActivity)
            //item子view点击事件
            setOnItemChildClickListener(this@SearchActivity)
            //加载更多
            loadMoreModule.setOnLoadMoreListener(this@SearchActivity)
        }
    }

    override fun initView() {
        title = "搜索"
//        setBackEnabled()
        db.rvSearch.adapter = mArticleAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //引用menu文件
        menuInflater.inflate(R.menu.menu_search, menu)

        //找到SearchView并配置相关参数
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView: androidx.appcompat.widget.SearchView =
            MenuItemCompat.getActionView(searchItem) as androidx.appcompat.widget.SearchView

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
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            // 当搜索内容改变时触发该方法
            override fun onQueryTextChange(newText: String): Boolean {
                //当没有输入任何内容的时候显示搜索热词，看实际需求
                db.llHotkey.visibility = View.VISIBLE
                db.rvSearch.visibility = View.GONE
                return false
            }

            // 当点击搜索按钮时触发该方法
            override fun onQueryTextSubmit(query: String): Boolean {
                LogUtils.e("搜索内容", "搜索内容===$query")
                mKey = query
                CURRENT_PAGE = 0 //重置分页，避免二次加载分页混乱
                //搜索请求
                vm.getSearchList(CURRENT_PAGE, mKey)
                //清除焦点，收软键盘
                searchView.clearFocus()
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
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

    private fun toLogin(msg: String) {
        AlertDialog.Builder(this@SearchActivity).apply {
            setTitle(R.string.tip)
            setMessage(msg)
            setPositiveButton(R.string.sure) { _, _ ->
                startActivity(Intent(this@SearchActivity, LoginActivity::class.java))
            }
            setNegativeButton(R.string.cancel, null)
        }.create().show()
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        startActivity(Intent(this, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, mDataList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, mDataList[position].title)
        })
    }

    override fun onLoadMore() {
        if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
            mArticleAdapter.loadMoreModule.loadMoreEnd(true)
        } else {
            CURRENT_PAGE++
            vm.getSearchList(CURRENT_PAGE, mKey)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position
        if (mDataList[position].collect) {
            vm.unCollect(mDataList[position].id)
        } else {
            vm.toCollect(mDataList[position].id)
        }
    }

    override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        KeyboardUtils.hideSoftInput(this)
        mKey = mHotkeyList[position].name
        //填充搜索框
        mEditText.setText(mKey)
        CURRENT_PAGE = 0 //重置分页，避免二次加载分页混乱
        vm.getSearchList(CURRENT_PAGE, mKey)
        return true
    }

    override fun vmClass(): Class<SearchVM> {
        return SearchVM::class.java
    }



}
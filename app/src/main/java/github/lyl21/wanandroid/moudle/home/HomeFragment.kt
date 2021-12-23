package github.lyl21.wanandroid.moudle.home

import android.content.Intent
import android.view.View
import coil.load
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.adapter.ArticleAdapter
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.bean.IndexBannerInfo
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.httpResult.DataState.*
import github.lyl21.wanandroid.base.loadsir.*
import github.lyl21.wanandroid.base.ui.BaseRefreshVMFragment
import github.lyl21.wanandroid.bean.ArticleInfo
import github.lyl21.wanandroid.databinding.FragmentHomeBinding
import github.lyl21.wanandroid.http.datamanager.DownloadListener
import github.lyl21.wanandroid.http.download.DownloadManager
import github.lyl21.wanandroid.util.AlertDialogUtil
import github.lyl21.wanandroid.util.ImageUtil
import kotlin.math.roundToInt


/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 16:48
 */
class HomeFragment : BaseRefreshVMFragment<FragmentHomeBinding, HomeVM>(),
    OnItemClickListener, OnItemChildClickListener, OnBannerListener<String>,
    OnLoadMoreListener {

    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数
    }

    private var articleList: MutableList<ArticleInfo> = mutableListOf()
    private var bannerList: MutableList<IndexBannerInfo> = mutableListOf()
    private val mArticleAdapter by lazy { ArticleAdapter() }

    private var mPosition: Int = 0
    private lateinit var imgLists: MutableList<String>
    private var isLoaded=false

    override fun initData() {
        //保存到本地,必应壁纸集
        vm.getBingImgList.observe(this) {
            val data = it.images
            var index = -1
            for (images in data) {
                index++
                MMKV.defaultMMKV()
                    .encode("bingImgList[$index]", ConstantParam.BingBaseUrl + data[index].url)
            }
        }

        //设置banner
        vm.getBannerList.observe(this) {
            when (it.dataState) {
                LOADING -> db.bannerStatus.showLoading()
                SUCCESS -> {
                    db.bannerStatus.showSuccess()
                    bannerList = it.data!!
                    val images: MutableList<String> = ArrayList()
                    for (i in bannerList.indices) {
                        images.add(bannerList[i].imagePath)
                    }
                    db.banner.run {
                        addBannerLifecycleObserver(this@HomeFragment)
                        indicator = CircleIndicator(context)
                        setBannerRound(10f)
                        setAdapter(object : BannerImageAdapter<String>(images) {
                            override fun onBindView(
                                holder: BannerImageHolder?,
                                data: String?,
                                position: Int,
                                size: Int
                            ) {
                                holder?.let {
                                    it.imageView.load(data) {
                                        crossfade(true)
                                        transformations(RoundedCornersTransformation(30f))
                                    }
                                }
                            }
                        })
                    }
                }
                COMPLETED -> {}
                EMPTY -> db.bannerStatus.showEmpty()
                FAILED -> db.bannerStatus.showError() {
                    it.retry {
                        vm.getBannerList()
                    }
                }
                UNKNOWN,
                null -> ToastUtils.showShort("网络繁忙，请稍后再试")
            }
        }

        vm.getArticleList.observe(this) {
            if (isLoaded){
                CURRENT_PAGE=it.data!!.datas!!.size
                if (CURRENT_PAGE > 0) {
                    if (it.data!!.datas!!.isEmpty()) {
                        db.fgHome.finishLoadMoreWithNoMoreData()
                    } else {
                        articleList.addAll(it.data!!.datas!!)
                        mArticleAdapter.addData(it.data!!.datas!!)
                    }
                } else {
                    articleList = it.data!!.datas!!
                    mArticleAdapter.setList(articleList)
                }
                db.fgHome.finishLoadMore()
            }else{
                when (it.dataState) {
                    LOADING -> {
                        db.rvStatus.showLoading()
                    }
                    SUCCESS -> {
                        db.rvStatus.showSuccess()
                        isLoaded=true
                        CURRENT_PAGE=it.data!!.datas!!.size
                        if (CURRENT_PAGE > 0) {
                            if (it.data!!.datas!!.isEmpty()) {
                                db.fgHome.finishLoadMoreWithNoMoreData()
                            } else {
                                articleList.addAll(it.data!!.datas!!)
                                mArticleAdapter.addData(it.data!!.datas!!)
                            }
                        } else {
                            articleList = it.data!!.datas!!
                            mArticleAdapter.setList(articleList)
                        }
                        db.fgHome.finishLoadMore()
                    }
                    COMPLETED -> {
                        LogUtils.e("lll",articleList.toString())
                    }
                    EMPTY -> {
                        db.rvStatus.showEmpty()
                    }
                    FAILED -> db.rvStatus.showError() {
                        it.retry{
                            vm.getArticleList(0)
                        }
                    }
                    UNKNOWN,
                    null -> ToastUtils.showShort("网络繁忙，请稍后再试")
                }
            }


        }
        vm.toCollect.observe(this) {
            if (-1001 == it.errorCode) {
                AlertDialogUtil.show(context)
            } else {
                ToastUtils.showShort("收藏成功")
                articleList[mPosition].collect = true
                mArticleAdapter.notifyDataSetChanged()
            }
        }

        vm.unCollect.observe(this) {
            articleList[mPosition].collect = false
            mArticleAdapter.notifyDataSetChanged()
        }

    }

    override fun onLoad() {
        vm.getBingImgList(ConstantParam.BingImgListUrl)
        vm.getBannerList()
        vm.getArticleList(0)
        imgLists = mutableListOf()
        for (index in 0..7) {
            val decodeString = MMKV.defaultMMKV().decodeString("bingImgList[$index]")
            decodeString?.let { imgLists.add(index, it) }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        CURRENT_PAGE=0
        articleList.clear()
        super.onRefresh(refreshLayout)
    }

    override fun onClick(v: View?) {}


    override fun initListener() {
        db.banner.setOnBannerListener(this)
        db.fgHome.setOnRefreshListener(this)
        db.fgHome.setOnLoadMoreListener(this)
        mArticleAdapter.run {
            animationEnable = true
            setOnItemClickListener(this@HomeFragment)
            setOnItemChildClickListener(this@HomeFragment)
        }
    }


    override fun initView() {
        //动态适配高度
        val bannerLp = db.banner.layoutParams
        bannerLp.height = (ScreenUtils.getScreenHeight() / 4.0).roundToInt()
        //文章item
        db.rvHome.adapter = mArticleAdapter

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
            db.fgHome.finishLoadMoreWithNoMoreData()
        } else {
            db.fgHome.autoLoadMore()
            CURRENT_PAGE++
            ToastUtils.showShort(CURRENT_PAGE)
            vm.getArticleList(CURRENT_PAGE)
        }
    }



    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        startActivity(Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, articleList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, articleList[position].title)
        })
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position

        if (view.id == R.id.iv_home_article_download) {
            val url = ConstantParam.BingBaseUrl + imgLists[position]
            val filePath = ImageUtil.createImgPath()
            DownloadManager.downloadFile(
                url,
                filePath,
                object : DownloadListener {
                    override fun onProgress(progress: Int) {}
                    override fun onCompleted() {
                        ToastUtils.showLong(getString(R.string.down_finish_save_to) + filePath)
                    }

                    override fun onError(msg: String) {
                        ToastUtils.showLong(getString(R.string.down_image_failed))
                    }
                })
        } else if (view.id == R.id.iv_home_article_like) {
            if (articleList[position].collect) {
                vm.unCollect(articleList[position].id)
            } else {
                vm.toCollect(articleList[position].id)
            }
        }

    }

    override fun OnBannerClick(data: String?, position: Int) {
        LogUtils.e("lll", "OnBannerClick")
        startActivity(Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, bannerList[position].url)
            putExtra(AgentWebViewActivity.WEB_TITLE, bannerList[position].title)
        })
    }


    override fun onStart() {
        super.onStart()
        //开始轮播
        db.banner.start()
    }

    override fun onStop() {
        super.onStop()
        //停止轮播
        db.banner.stop()
    }

    override fun onDestroyView() {
        //结束轮播
        db.banner.destroy()
        super.onDestroyView()
    }

    override fun vmClass(): Class<HomeVM> {
        return HomeVM::class.java
    }


}


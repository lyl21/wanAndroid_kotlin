package github.lyl21.wanandroid.moudle.home

import BaseFragment
import HomeView
import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.ArticleAdapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.config.ConstantParam
import github.lyl21.wanandroid.databinding.FragmentHomeBinding
import github.lyl21.wanandroid.entity.ArticleDetail
import github.lyl21.wanandroid.entity.ArticleInfo
import github.lyl21.wanandroid.entity.IndexBannerInfo
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.webView.AgentWebViewActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import kotlin.math.roundToInt


/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 16:48
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeView, OnLoadMoreListener,
    OnItemClickListener, OnItemChildClickListener,
    OnBannerListener<String> {

    companion object {
        private var CURRENT_SIZE = 0//当前加载数量
        private var CURRENT_PAGE = 0//当前加载页数
    }

    private lateinit var mHomePresenter: HomePresenter
    private lateinit var articleList: MutableList<ArticleDetail>
    private lateinit var bannerList: List<IndexBannerInfo>
    private lateinit var mArticleAdapter: ArticleAdapter
    private var mPosition: Int = 0


    override fun createPresenter() {
        mHomePresenter = HomePresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {

    }

    override fun initData() {
        mHomePresenter.getBanner()
        mHomePresenter.getArticleList(CURRENT_PAGE)
    }

    override fun getBanner(res: Response<MutableList<IndexBannerInfo>>) {
        bannerList = res.data

        val images: MutableList<String> = ArrayList()
        val titles: MutableList<String> = ArrayList()
        for (i in bannerList.indices) {
            images.add(bannerList[i].imagePath)
            titles.add(bannerList[i].title)
        }

//        mBanner = binding.banner as Banner<String, BannerImageAdapter<String>>
        //动态适配高度
        val bannerLp = binding.banner.layoutParams
        bannerLp.height = (ScreenUtils.getScreenHeight() / 4.0).roundToInt()

        binding.banner.apply {
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
                        Glide.with(this@HomeFragment)
                            .load(data)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                            .into(it.imageView)
                    }
                }

            })
        }
        binding.banner.setOnBannerListener(this)


    }


    override fun getBannerError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun getArticleList(article: Response<ArticleInfo>) {
        CURRENT_SIZE = article.data.datas.size
        articleList = article.data.datas
        mArticleAdapter = ArticleAdapter().apply {
            animationEnable = true
            setOnItemClickListener(this@HomeFragment)
            setOnItemChildClickListener(this@HomeFragment)
            loadMoreModule.setOnLoadMoreListener(this@HomeFragment)
        }
        binding.rvHome.adapter = mArticleAdapter
        mArticleAdapter.setList(articleList)
    }

    override fun getArticleError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun getArticleMoreList(article: Response<ArticleInfo>) {
        articleList.addAll(article.data.datas)
        CURRENT_SIZE = article.data.datas.size
        mArticleAdapter.addData(article.data.datas)
        mArticleAdapter.loadMoreModule.loadMoreComplete()
    }

    override fun getArticleMoreError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun login(msg: String) {
        val showLoginDialogBuilder = AlertDialog.Builder(context).apply {
            setTitle(github.lyl21.wanandroid.R.string.tips)
            setMessage(msg)
            setPositiveButton(R.string.sure) { _, _ ->
                startActivity(Intent(mContext, LoginActivity::class.java))
            }
            setNegativeButton(R.string.cancel, null)
        }
        showLoginDialogBuilder.create().show()
    }


    override fun toCollection(msg: String) {
        ToastUtils.showLong(msg)
        articleList[mPosition].collect = true
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun unCollection(msg: String) {
        LogUtils.e("lll", "$mPosition---unCollectionaaaaaaaaa")

        ToastUtils.showLong(msg)
        articleList[mPosition].collect = false
        LogUtils.e("lll", "$mPosition---unCollection")
        mArticleAdapter.notifyDataSetChanged()
    }

    override fun onLoadMore() {
        binding.rvHome.postDelayed({
            if (CURRENT_SIZE < ConstantParam.PAGE_SIZE) {
                mArticleAdapter.loadMoreModule.loadMoreEnd(true)
            } else {
                CURRENT_PAGE++
                mHomePresenter.getArticleMoreList(CURRENT_PAGE)
            }
        }, 0)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val intent = Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, articleList[position].link)
            putExtra(AgentWebViewActivity.WEB_TITLE, articleList[position].title)
        }
        startActivity(intent)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mPosition = position

        if (view.id == R.id.iv_home_article_download) {
            ToastUtils.showLong("xxxxx")
            LogUtils.e("!!!!!!!!!!")
//            getFilePermission()
            //保存到本地文件
            //保存二维码到相册
//            val img = BitmapFactory.decodeResource(resources, R.mipmap.download)
//            saveBitmap2Gallery2(mContext,img)

            requireContext().getDir("myfileeeeeeeeeeeeeeeeeeeeeeeeeee", Context.MODE_PRIVATE)
            val filename = "myfileeeeeeeeeeeeeeeeeeeeeeeeeee"
            val fileContents = "Hello world!"
            requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContents.toByteArray())
            }

            LogUtils.e("mContext!!!!!!")

        } else if (view.id == R.id.iv_home_article_like) {
            if (articleList[position].collect) {
                mHomePresenter.unCollection(articleList[position].id)
            } else {
                mHomePresenter.toCollection(articleList[position].id)
            }
        }

    }

    //获取文件权限
    @AfterPermissionGranted(100)
    fun getFilePermission() {
        ToastUtils.showLong("QQQ")
        if (EasyPermissions.hasPermissions(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            //  有上述权限, 执行该操作
            ToastUtils.showLong("权限申请通过")
        } else {
            //  没有上述权限 , 那么申请权限
            EasyPermissions.requestPermissions(
                this,
                "权限申请原理对话框 : 描述申请权限的原理",
                100,

                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }


    fun saveBitmap2Gallery2(context: Context, bitmap: Bitmap): Boolean {
        val name = System.currentTimeMillis().toString()
        val photoPath = Environment.DIRECTORY_DCIM + "/Camera"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, photoPath)//保存路径
                put(MediaStore.MediaColumns.IS_PENDING, true)
            }
        }
        val insert = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return false //为空的话 直接失败返回了
        //这个打开了输出流  直接保存图片就好了
        context.contentResolver.openOutputStream(insert).use {
            it ?: return false
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, false)
        }
        return true
    }


    override fun onStart() {
        super.onStart()
        //开始轮播
        binding.banner.start();
    }

    override fun onStop() {
        super.onStop()
        //停止轮播
        binding.banner.stop();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //结束轮播
        binding.banner.destroy()
    }

    override fun OnBannerClick(data: String?, position: Int) {
        LogUtils.e("lll", "OnBannerClick")
        val intent = Intent(context, AgentWebViewActivity::class.java).apply {
            putExtra(AgentWebViewActivity.WEB_URL, bannerList[position].url)
            putExtra(AgentWebViewActivity.WEB_TITLE, bannerList[position].title)
        }
        startActivity(intent)
    }


}
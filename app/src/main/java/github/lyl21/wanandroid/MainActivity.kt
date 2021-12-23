package github.lyl21.wanandroid

import android.Manifest
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import github.lyl21.wanandroid.base.adapter.CommonViewpager2Adapter
import github.lyl21.wanandroid.moudle.about.AboutActivity
import github.lyl21.wanandroid.moudle.collect.CollectActivity
import github.lyl21.wanandroid.moudle.home.HomeFragment
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.navi.NaviFragment
import github.lyl21.wanandroid.moudle.project.ProjectFragment
import github.lyl21.wanandroid.moudle.search.SearchActivity
import github.lyl21.wanandroid.moudle.tree.TreeFragment
import com.google.android.material.navigation.NavigationView
import com.tencent.mmkv.MMKV
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.permissionx.guolindev.PermissionX
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.moudle.live.RoleActivity
import github.lyl21.wanandroid.util.DarkModeUtil
import android.view.*
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import coil.load
import com.blankj.utilcode.util.*
import com.google.android.material.snackbar.Snackbar
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import github.lyl21.wanandroid.base.ui.BaseVMActivity
import github.lyl21.wanandroid.databinding.ActivityMainBinding
import github.lyl21.wanandroid.util.ImageUtil
import splitties.views.textColorResource


class MainActivity : BaseVMActivity<ActivityMainBinding, MainVM>() {
    // 保存用户按返回键的时间
    private var mExitTime: Long = 0

    private lateinit var snackBarLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navHeaderLayout: View

    private lateinit var avatarImgView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var logoutView: MenuItem
    private lateinit var darkModeView: MenuItem

    private val aboutActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            run {
                if (activityResult.resultCode == Activity.RESULT_OK) {
                    val info = activityResult.data?.getStringExtra("info")
                    ToastUtils.showLong("回传的数据：$info")
                }
            }
        }


    override fun initData() {
        if (DarkModeUtil.isDarkMode(this)) {
            darkModeView.title="日间模式"
        } else {
            darkModeView.title="暗黑模式"
        }
        //获取用户信息
        vm.getUserInfo.observe(this) {
            logoutView.isVisible = it.data!!.userInfo.username.isNotEmpty()
            if (MMKV.defaultMMKV().decodeBool("isLogin")) {
                avatarImgView.load(it.data!!.userInfo.icon) {
                    crossfade(true)
                    placeholder(R.mipmap.ic_launcher_round)
                    error(R.mipmap.icon)
                    transformations()
                }
                usernameTextView.text = it.data!!.userInfo.username
                usernameTextView.textColorResource = R.color.money_yellow
            }
        }

        //登出
        vm.toLogout.observe(this) {
            MMKV.defaultMMKV().encode("isLogin", false)
            avatarImgView.load("") {
                placeholder(R.mipmap.ic_launcher_round)
                error(R.mipmap.ic_launcher_round)
            }
            usernameTextView.textColorResource = R.color.text_white
            logoutView.isVisible = false

            Snackbar.make(snackBarLayout, "登出成功", Snackbar.LENGTH_LONG)
                .setAction(R.string.sure, View.OnClickListener {
                }).show()
        }
    }

    override fun onLoad() {
        if (MMKV.defaultMMKV().decodeBool("isLogin")) {
            val username = MMKV.defaultMMKV().decodeString("username")!!
            vm.getUserInfo(username)
        }
    }

    override fun initView() {
        setSupportActionBar(db.toolbar)
        initActionBarDrawer()
        initFragments()
        initDrawerLayoutData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    private fun initDrawerLayoutData() {
        snackBarLayout = findViewById(R.id.drawer_main)

        navView = findViewById(R.id.main_drawer_layout)
        navHeaderLayout = navView.getHeaderView(0)
        //侧边栏头部
        avatarImgView = navHeaderLayout.findViewById(R.id.iv_drawer_avatar)
        usernameTextView = navHeaderLayout.findViewById(R.id.tv_drawer_name)
        //侧边栏内容
        logoutView = navView.menu.findItem(R.id.nav_logout)
        darkModeView = navView.menu.findItem(R.id.nav_dark_mode)

    }


    private fun initFragments() {
        val viewPagerAdapter = CommonViewpager2Adapter(supportFragmentManager, lifecycle).apply {
            addFragment(HomeFragment())
//            addFragment(TreeFragment())
//            addFragment(NaviFragment())
//            addFragment(ProjectFragment())
        }
        db.vp2Main.adapter = viewPagerAdapter
        db.vp2Main.apply {
            offscreenPageLimit = OFFSCREEN_PAGE_LIMIT_DEFAULT
            currentItem = 0
            isUserInputEnabled = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }


    /**
     * toolbar菜单事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> startActivity(Intent(this, SearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Drawer关联Toolbar
     */
    private fun initActionBarDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            db.drawerMain,
            db.toolbar,
            R.string.drawer_left_open,
            R.string.drawer_left_close
        )
        db.drawerMain.addDrawerListener(toggle)
        toggle.syncState()
    }

    /**
     * 拦截返回事件，自处理
     */
    override fun onBackPressed() {
        if (db.drawerMain.isDrawerOpen(GravityCompat.START)) {
            db.drawerMain.closeDrawer(GravityCompat.START)
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.showLong("再按一次退出" + resources.getString(R.string.app_name))
                mExitTime = System.currentTimeMillis()
            } else {
                ActivityUtils.finishAllActivities()
            }
        }
        //super.onBackPressed()
    }


    /**
     * 调用系统的分享功能
     */
    private fun shareProject() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "玩安卓")
            putExtra(Intent.EXTRA_TEXT, "https://github.com/lyl21")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(Intent.createChooser(intent, "玩安卓"))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RESULT_OK == resultCode) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                if (data != null) {
                    val localMediaList: MutableList<LocalMedia> =
                        PictureSelector.obtainMultipleResult(data)
                    if (localMediaList.isNotEmpty()) {
                        //上传头像
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
    }

    override fun initListener() {
        avatarImgView.setOnClickListener {
            if (MMKV.defaultMMKV().decodeBool("isLogin")) {
                //选择图片
                ImageUtil.getImagePictureSelector(this, PictureMimeType.ofAll(), 1, true, true)
            } else {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }

        /**
         * 侧边栏点击事件
         */
        db.mainDrawerLayout.setNavigationItemSelectedListener {
            // Handle navigation view item clicks here.
            when (it.itemId) {
                R.id.nav_collect -> {
                    if (MMKV.defaultMMKV().decodeBool("isLogin")) {
                        startActivity(Intent(this@MainActivity, CollectActivity::class.java))
                    } else {
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.tip)
                            setMessage(R.string.toLogin)
                            setPositiveButton(R.string.sure) { _, _ ->
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            }
                            setNegativeButton(R.string.cancel, null)
                        }
                            .create().show()
                    }
                }
                R.id.nav_share -> {
                    shareProject()
                }
                R.id.nav_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    intent.putExtra("info", "info")
                    aboutActivityResult.launch(intent)
                }
                R.id.nav_location -> {
                    // 获取地址
                    //startActivity(Intent(this, LocationActivity::class.java))
                }
                R.id.nav_live -> {
                    //申请权限
                    PermissionX.init(this)
                        .permissions(
                            listOf(
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                        .explainReasonBeforeRequest()
                        .onExplainRequestReason { scope, deniedList ->
                            val message = ConstantParam.APK_NAME + "需要您同意以下权限才能正常使用"
                            scope.showRequestReasonDialog(deniedList, message, "同意", "拒绝")
                        }
                        .request { allGranted, grantedList, deniedList ->
                            if (allGranted) {
                                startActivity(Intent(this, RoleActivity::class.java))
                            } else {
                                ToastUtils.showLong("您拒绝了如下权限：$deniedList")
                            }
                        }
                }
                R.id.nav_dark_mode -> {
                    if (DarkModeUtil.isDarkMode(this)) {
                        DarkModeUtil.applyDayMode(this@MainActivity)
                    } else {
                        DarkModeUtil.applyNightMode(this@MainActivity)
                    }
                }
                R.id.nav_check_update -> {
                    //检查更新
                    //申请权限
                    PermissionX.init(this)
                        .permissions(
                            listOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                        .explainReasonBeforeRequest()
                        .onExplainRequestReason { scope, deniedList ->
                            val message = ConstantParam.APK_NAME + "需要您同意以下权限才能正常使用"
                            scope.showRequestReasonDialog(deniedList, message, "同意", "拒绝")
                        }
                        .request { allGranted, grantedList, deniedList ->
                            if (!allGranted) {
                                ToastUtils.showLong("您拒绝了如下权限：$deniedList")
                            }
                        }
                }
                R.id.nav_logout -> {
                    vm.toLogout()
                }
            }
            //关闭侧边栏
            db.drawerMain.closeDrawer(GravityCompat.START)
            true
        }

        /**
         * view_pager 滑动监听
         */
        db.vp2Main.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                db.bottomNav.menu.getItem(position).isChecked = true
                //设置checked为true，但是不能触发ItemSelected事件，所以滑动时也要设置一下标题
                when (position) {
                    0 -> db.toolbar.title = resources.getString(R.string.app_name)
                    1 -> db.toolbar.title = resources.getString(R.string.main_tree)
                    2 -> db.toolbar.title = resources.getString(R.string.main_nav)
                    3 -> db.toolbar.title = resources.getString(R.string.main_project)
                }
            }
        })

        /**
         * bottom_navigation 点击事件
         */
        db.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fragment_home -> {
                    db.toolbar.title = resources.getString(R.string.app_name)
                    db.vp2Main.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.fragment_tree -> {
                    db.toolbar.title = resources.getString(R.string.main_tree)
                    db.vp2Main.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.fragment_navi -> {
                    db.toolbar.title = resources.getString(R.string.main_nav)
                    db.vp2Main.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.fragment_project -> {
                    db.toolbar.title = resources.getString(R.string.main_project)
                    db.vp2Main.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }


    override fun vmClass(): Class<MainVM> {
        return MainVM::class.java
    }


}





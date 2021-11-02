package github.lyl21.wanandroid

import BaseActivity
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import github.lyl21.wanandroid.adapter.CommonViewpager2Adapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.entity.UserDataInfo
import github.lyl21.wanandroid.entity.UserInfo
import github.lyl21.wanandroid.moudle.about.AboutActivity
import github.lyl21.wanandroid.moudle.collect.CollectActivity
import github.lyl21.wanandroid.moudle.home.HomeFragment
import github.lyl21.wanandroid.moudle.login.LoginActivity
import github.lyl21.wanandroid.moudle.login.LoginPresenter
import github.lyl21.wanandroid.moudle.login.LoginView
import github.lyl21.wanandroid.moudle.map.LocationActivity
import github.lyl21.wanandroid.moudle.navi.NaviFragment
import github.lyl21.wanandroid.moudle.project.ProjectFragment
import github.lyl21.wanandroid.moudle.search.SearchActivity
import github.lyl21.wanandroid.moudle.tree.TreeFragment
import github.lyl21.wanandroid.moudle.user.UserInfoPresenter
import github.lyl21.wanandroid.moudle.user.UserInfoView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.databinding.ActivityMainBinding
import splitties.views.textColorResource


class MainActivity : BaseActivity<ActivityMainBinding>(), UserInfoView, LoginView{
    // 保存用户按返回键的时间
    private var mExitTime: Long = 0
    private lateinit var userInfoPresenter: UserInfoPresenter
    private lateinit var loginPresenter: LoginPresenter

    private lateinit var snackBarLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navHeaderLayout: View

    private lateinit var avatarImgView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var logoutView: MenuItem

    override fun createPresenter() {
        userInfoPresenter = UserInfoPresenter(this)
        loginPresenter = LoginPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        setSupportActionBar(binding.toolbar)
        initActionBarDrawer()
        initFragments()
        initDrawerLayoutData()
        initListener()
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

        if (MMKV.defaultMMKV().decodeBool("isLogin")) {
            val username = MMKV.defaultMMKV().decodeString("username")!!
            //获取用户信息
            userInfoPresenter.getUserInfo(username)
            logoutView.isVisible = true
        } else {
            logoutView.isVisible = false
        }
    }

    override fun getUserInfo(userInfo: Response<UserDataInfo>) {
        LogUtils.e("lll", userInfo.toString())
        if (MMKV.defaultMMKV().decodeBool("isLogin")) {
            Glide.with(navView)
                .load(userInfo.data.userInfo.icon)
                .placeholder(R.mipmap.ic_launcher_round)//图片加载出来前，显示的图片
                .error(R.mipmap.icon)//图片加载失败后，显示的图片
                .into(avatarImgView)
            usernameTextView.text = userInfo.data.userInfo.username
            usernameTextView.textColorResource = R.color.money_yellow
        }
    }

    override fun getUserInfoErr(msg: String) {
        Snackbar.make(snackBarLayout, msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {
            }).show()
    }

    override fun login(msg: String) {

    }

    private fun initListener() {
        avatarImgView.setOnClickListener {
            if (!MMKV.defaultMMKV().decodeBool("isLogin")) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }

        /**
         * 侧边栏点击事件
         */
        binding.mainDrawerLayout.setNavigationItemSelectedListener {
            // Handle navigation view item clicks here.
            when (it.itemId) {
                R.id.nav_collect -> {
                    if (MMKV.defaultMMKV().decodeBool("isLogin")) {
                        startActivity(Intent(this@MainActivity, CollectActivity::class.java))
                    } else {
                        val showLoginDialogBuilder = AlertDialog.Builder(this).apply {
                            setTitle(R.string.tips)
                            setMessage(R.string.toLogin)
                            setPositiveButton(R.string.sure) { _, _ ->
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            }
                            setNegativeButton(R.string.cancel, null)
                        }
                        showLoginDialogBuilder.create().show()
                    }
                }
                R.id.nav_share -> {
                    shareProject()
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                R.id.nav_location -> {
                    // 获取地址
                    startActivity(Intent(this, LocationActivity::class.java))
                }
                R.id.nav_customer_service->{
                    //跳客服

                }
                R.id.nav_logout -> {
                    loginPresenter.logout()
                }
            }
            //关闭侧边栏
            binding.drawerMain.closeDrawer(GravityCompat.START)
            true
        }

        /**
         * view_pager 滑动监听
         */
        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.menu.getItem(position).isChecked = true
                //设置checked为true，但是不能触发ItemSelected事件，所以滑动时也要设置一下标题
                when (position) {
                    0 -> binding.toolbar.title = resources.getString(R.string.app_name)
                    1 -> binding.toolbar.title = resources.getString(R.string.main_tree)
                    2 -> binding.toolbar.title = resources.getString(R.string.main_nav)
                    else -> binding.toolbar.title = resources.getString(R.string.main_project)
                }
            }
        })

        /**
         * bottom_navigation 点击事件
         */
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    binding.vpMain.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_tree -> {
                    binding.vpMain.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_navi -> {
                    binding.vpMain.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_project -> {
                    binding.vpMain.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }


    private fun initFragments() {
        val viewPagerAdapter = CommonViewpager2Adapter(supportFragmentManager, lifecycle).apply {
            addFragment(HomeFragment())
            addFragment(TreeFragment())
            addFragment(NaviFragment())
            addFragment(ProjectFragment())
        }
        binding.vpMain.adapter = viewPagerAdapter
        binding.vpMain.apply {
            offscreenPageLimit = 4
            currentItem = 0
            isUserInputEnabled = true
        }
    }

    /**
     * 添加toolbar菜单
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
            binding.drawerMain,
            binding.toolbar,
            R.string.drawer_left_open,
            R.string.drawer_left_close
        )
        binding.drawerMain.addDrawerListener(toggle)
        toggle.syncState()
    }

    /**
     * 拦截返回事件，自处理
     */
    override fun onBackPressed() {
        if (binding.drawerMain.isDrawerOpen(GravityCompat.START)) {
            binding.drawerMain.closeDrawer(GravityCompat.START)
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

    override fun showLoginSuccess(msg: String) {
    }

    override fun showLoginFailed(msg: String) {
    }

    override fun doSuccess(user: Response<UserInfo>) {
    }

    override fun showLogoutSuccess(msg: String) {
        MMKV.defaultMMKV().encode("isLogin", false)

        Glide.with(this)
            .load("")
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
            .into(avatarImgView)
        usernameTextView.textColorResource = R.color.text_white
        logoutView.isVisible = false

        Snackbar.make(snackBarLayout, "登出成功", Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {
            }).show()
    }

    override fun showLogoutFailed(msg: String) {
        Snackbar.make(snackBarLayout, msg, Snackbar.LENGTH_LONG)
            .setAction(R.string.sure, View.OnClickListener {
            }).show()
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


    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}





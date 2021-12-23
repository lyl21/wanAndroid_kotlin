package github.lyl21.wanandroid.moudle.tree.child

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.adapter.CommonViewpager2Adapter
import github.lyl21.wanandroid.databinding.ActivityTreeChildBinding
import github.lyl21.wanandroid.bean.SysTreeChildren
import com.google.android.material.tabs.TabLayoutMediator
import github.lyl21.wanandroid.base.ui.BaseActivity
import github.lyl21.wanandroid.base.ui.BaseVMActivity
import github.lyl21.wanandroid.util.LoadingDialogUtil


class TreeChildActivity : BaseActivity<ActivityTreeChildBinding>() {

    private lateinit var mediator: TabLayoutMediator

    companion object {
        var TITLE: String = "title"
        var CID: String = "cid"
        var POSITION: String = "position"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }
    override fun initView() {
        setBackEnabled()

        val childList: ArrayList<SysTreeChildren> =
            intent.getSerializableExtra(CID) as ArrayList<SysTreeChildren>
        val titles = ArrayList<String>()
        for (i in childList.indices) {
            titles.add(childList[i].name)
        }

        val commonViewpageAdapter = CommonViewpager2Adapter(supportFragmentManager, lifecycle)
        for (i in titles.indices) {
            commonViewpageAdapter.addFragment(TreeChildFragment.newInstance(childList[i].id))
        }

        db.vpTreeChild.adapter = commonViewpageAdapter
        db.vpTreeChild.offscreenPageLimit = titles.size


        val index = intent.getIntExtra(POSITION, 0)
        db.vpTreeChild.currentItem = index


        //连接tab vp2
        mediator = TabLayoutMediator(
            db.tabTreeChild,
            db.vpTreeChild
        ) { tab, position ->
            //  为Tab设置Text
            tab.text = titles[position]
        }
        mediator.attach()
        //viewPager2 页面切换监听
        db.vpTreeChild.registerOnPageChangeCallback(changeCallback)

    }

    override  fun initListener() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_tree_child
    }

    private val changeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //可以来设置选中时tab的大小
//                val tabCount: Int = binding.tabTreeChild.tabCount
//                for (i in 0 until tabCount) {
//                    val tab: TabLayout.Tab = binding.tabTreeChild.getTabAt(i)!!
//                    val tabView = tab.customView as TextView?
//                    if (tab.position == position) {
//                        tabView!!.textSize = 20F
//                        tabView.setTypeface(Typeface.DEFAULT_BOLD)
//                    } else {
//                        tabView!!.textSize = 14F
//                        tabView.setTypeface(Typeface.DEFAULT)
//                    }
//                }
            }
        }

    override fun onDestroy() {
//        mediator.detach()
        db.vpTreeChild.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy()
    }



}
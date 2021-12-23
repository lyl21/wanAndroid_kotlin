package github.lyl21.wanandroid.moudle.project

import android.view.View
import github.lyl21.wanandroid.base.adapter.CommonViewpager2Adapter
import github.lyl21.wanandroid.databinding.FragmentProjectBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.ui.BaseRefreshVMFragment
import github.lyl21.wanandroid.moudle.project.child.ProjectChildFragment


class ProjectFragment : BaseRefreshVMFragment<FragmentProjectBinding, ProjectVM>() {

    private lateinit var commonViewpage2Adapter: CommonViewpager2Adapter
    private val titles: MutableList<String> = ArrayList()

    override fun initData() {
            vm.getProjectInfo.observe(this){
                val data=it.data!!
                //得到标题集合
                for (i in data.indices) {
                    titles.add(data[i].name)
                    commonViewpage2Adapter.addFragment(ProjectChildFragment.newInstance(data[i].id))
                    //viewPager2 初始化
                    db.vp2Project.apply {
                        offscreenPageLimit = titles.size
                        currentItem = 0
                        isUserInputEnabled = true
                    }
                    TabLayoutMediator(
                        db.tabProject,
                        db.vp2Project,
                        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                            //  为Tab设置Text
                            tab.text = titles[position]
                        }).attach()
                }
            }
    }
    override fun onLoad() {
        vm.getProjectInfo()
    }
    override fun onClick(v: View?) {}
    override fun initListener() {
        db.vp2Project.registerOnPageChangeCallback(changeCallback)
    }
    override fun initView() {
        //创建Fragment集合 并设置为ViewPager
        commonViewpage2Adapter = CommonViewpager2Adapter(childFragmentManager, lifecycle)
        db.vp2Project.adapter = commonViewpage2Adapter
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    private val changeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
//            //可以来设置选中时tab的大小
//            val tabCount: Int = binding.tabProject.tabCount
//            for (i in 0 until tabCount) {
//                val tab: TabLayout.Tab = binding.tabProject.getTabAt(i)!!
//                val tabView = tab.customView as TextView?
//                if (tab.position == position) {
//                    tabView!!.textSize = 20F
//                    tabView.setTypeface(Typeface.DEFAULT_BOLD)
//                } else {
//                    tabView!!.textSize = 14F
//                    tabView.setTypeface(Typeface.DEFAULT)
//                }
//            }
        }
    }




    override fun onDestroy() {
        super.onDestroy()
//        mediator.detach()
//        binding.vp2Project.unregisterOnPageChangeCallback(changeCallback)
    }


    override fun vmClass(): Class<ProjectVM> {
        return ProjectVM::class.java
    }



}
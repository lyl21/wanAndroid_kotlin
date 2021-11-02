package github.lyl21.wanandroid.moudle.project

import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.CommonViewpager2Adapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.databinding.FragmentProjectBinding
import github.lyl21.wanandroid.entity.ProjectClassInfo
import github.lyl21.wanandroid.moudle.project.child.ProjectChildFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList
import BaseFragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.blankj.utilcode.util.ToastUtils


class ProjectFragment : BaseFragment<FragmentProjectBinding>(), ProjectView {

    private lateinit var mediator: TabLayoutMediator
    private lateinit var mProjectPresenter: ProjectPresenter
    private lateinit var commonViewpage2Adapter: CommonViewpager2Adapter
    private val titles: MutableList<String> = ArrayList()


    override fun createPresenter() {
        mProjectPresenter = ProjectPresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    override fun initView() {
        //创建Fragment集合 并设置为ViewPager
        commonViewpage2Adapter = CommonViewpager2Adapter(childFragmentManager, lifecycle)
        binding.vpProject.adapter = commonViewpage2Adapter
    }


    override fun initData() {
        mProjectPresenter.getProjectInfo()
    }

    override fun getProject(project: Response<MutableList<ProjectClassInfo>>) {
        //得到标题集合
        for (i in project.data.indices) {
            titles.add(project.data[i].name)
            commonViewpage2Adapter.addFragment(ProjectChildFragment.newInstance(project.data[i].id))
        }
        binding.vpProject.apply {
            offscreenPageLimit = titles.size
            currentItem = 0
            isUserInputEnabled = true
        }

        mediator = TabLayoutMediator(
            binding.tabProject,
            binding.vpProject,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                //  为Tab设置Text
                tab.text = titles[position]
            })
        mediator.attach()

        //viewPager2 页面切换监听
        binding.vpProject.registerOnPageChangeCallback(changeCallback)
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

    override fun getProjectError(msg: String) {
        ToastUtils.showLong(msg)
    }

    override fun onDestroy() {
        mediator.detach()
        binding.vpProject.unregisterOnPageChangeCallback(changeCallback)
        super.onDestroy()
    }

}
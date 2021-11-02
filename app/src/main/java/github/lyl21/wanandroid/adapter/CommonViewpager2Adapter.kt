package github.lyl21.wanandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 13:00
 */
class CommonViewpager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mFragments = mutableListOf<Fragment>()
    override fun getItemCount(): Int = mFragments.size
    override fun createFragment(position: Int): Fragment = mFragments[position]

    fun addFragment(fragment:Fragment){
        mFragments.add(fragment)
    }

}
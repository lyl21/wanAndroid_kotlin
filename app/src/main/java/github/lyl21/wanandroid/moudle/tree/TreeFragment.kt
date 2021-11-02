package github.lyl21.wanandroid.moudle.tree

import BaseFragment
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.adapter.TreeAdapter
import github.lyl21.wanandroid.base.Response
import github.lyl21.wanandroid.databinding.FragmentTreeBinding
import github.lyl21.wanandroid.entity.SysTree
import github.lyl21.wanandroid.moudle.tree.child.TreeChildActivity
import com.yechaoa.yutilskt.ToastUtil
import com.zhy.view.flowlayout.FlowLayout

class TreeFragment : BaseFragment<FragmentTreeBinding>(), TreeView, TreeAdapter.OnItemTagClickListener {

    private lateinit var mTreePresenter: TreePresenter
    private lateinit var mTreeList: MutableList<SysTree>
    private var mPosition: Int = 0

    override fun createPresenter() {
        mTreePresenter = TreePresenter(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tree
    }

    override fun initView() {
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        binding.fgTree.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )
        binding.fgTree.setOnRefreshListener {
            binding.fgTree.postDelayed({
                mTreePresenter.getTree()
                binding.fgTree.isRefreshing = false
            }, 1000)
        }
    }

    override fun initData() {
        mTreePresenter.getTree()
    }

    override fun getTree(tree: Response<MutableList<SysTree>>) {
        mTreeList = tree.data

        val treeAdapter = TreeAdapter().apply {
            setOnItemClickListener { _, _, position ->
                mPosition = position

                if (tree.data[position].isShow) {
                    for (i in tree.data.indices) {
                        tree.data[i].isShow = false
                    }
                } else {
                    for (i in tree.data.indices) {
                        tree.data[i].isShow = false
                    }
                    tree.data[position].isShow = true
                }
                notifyDataSetChanged()
            }
            //子view标签点击事件
            setOnItemTagClickListener(this@TreeFragment)
        }
        binding.rvTree.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        binding.rvTree.adapter = treeAdapter
        treeAdapter.setList(tree.data)
    }

    /**
     * 标签点击事件
     */
    override fun onItemTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        val intent = Intent(context, TreeChildActivity::class.java).apply {
            putExtra(TreeChildActivity.TITLE, mTreeList[mPosition].name)
            putExtra(TreeChildActivity.CID, mTreeList[mPosition].children)
            putExtra(TreeChildActivity.POSITION, position)
        }
        startActivity(intent)
        return true
    }

    override fun getTreeError(msg: String) {
        ToastUtil.show(msg)
    }
}
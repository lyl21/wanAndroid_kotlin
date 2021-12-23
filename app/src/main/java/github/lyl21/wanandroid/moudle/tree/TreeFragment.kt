package github.lyl21.wanandroid.moudle.tree

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import github.lyl21.wanandroid.adapter.TreeAdapter
import github.lyl21.wanandroid.databinding.FragmentTreeBinding
import github.lyl21.wanandroid.bean.SysTree
import github.lyl21.wanandroid.moudle.tree.child.TreeChildActivity
import com.zhy.view.flowlayout.FlowLayout
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.base.httpResult.BaseResult
import github.lyl21.wanandroid.base.httpResult.DataState
import github.lyl21.wanandroid.base.httpResult.DataState.*
import github.lyl21.wanandroid.base.ui.BaseRefreshVMFragment

class TreeFragment : BaseRefreshVMFragment<FragmentTreeBinding, TreeVM>(),
    TreeAdapter.OnItemTagClickListener {

    private lateinit var mTreeList: MutableList<SysTree>
    private var mPosition: Int = 0

    override fun initData() {
        vm.getTree.observe(this) {
            getTree(it)
        }

    }

    override fun onLoad() {
        vm.getTree()
    }

    override fun onClick(v: View?) {
    }
    override fun initListener() {

    }

    override fun initView() {
        initSwipeRefreshLayout()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tree
    }




    private fun initSwipeRefreshLayout() {
        db.fgTree.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )
        db.fgTree.setOnRefreshListener {
            db.fgTree.postDelayed({
                onLoad()
                db.fgTree.isRefreshing = false
            }, 1000)
        }
    }


    fun getTree(tree: BaseResult<MutableList<SysTree>>) {
        when(tree.dataState){
            LOADING -> {}
            SUCCESS -> {
                mTreeList = tree.data!!
                val treeAdapter = TreeAdapter().apply {
                    setOnItemClickListener { _, _, position ->
                        mPosition = position

                        if (mTreeList[position].isShow) {
                            for (i in mTreeList.indices) {
                                mTreeList[i].isShow = false
                            }
                        } else {
                            for (i in mTreeList.indices) {
                                mTreeList[i].isShow = false
                            }
                            mTreeList[position].isShow = true
                        }
                        notifyDataSetChanged()
                    }
                    //子view标签点击事件
                    setOnItemTagClickListener(this@TreeFragment)
                }
                db.rvTree.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                db.rvTree.adapter = treeAdapter
                treeAdapter.setList(tree.data)}
            COMPLETED -> {}
            EMPTY -> {}
            FAILED -> {}
            UNKNOWN ,
            null -> {}
        }

    }

    /**
     * 标签点击事件
     */
    override fun onItemTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        startActivity(Intent(context, TreeChildActivity::class.java).apply {
            putExtra(TreeChildActivity.TITLE, mTreeList[mPosition].name)
            putExtra(TreeChildActivity.CID, mTreeList[mPosition].children)
            putExtra(TreeChildActivity.POSITION, position)
        })
        return true
    }

    override fun vmClass(): Class<TreeVM> {
        return TreeVM::class.java
    }



}
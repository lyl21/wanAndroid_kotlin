package github.lyl21.wanandroid.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.databinding.ItemTreeBinding
import github.lyl21.wanandroid.entity.SysTree
import github.lyl21.wanandroid.entity.SysTreeChildren
import github.lyl21.wanandroid.moudle.tree.TreeFragment
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import org.w3c.dom.Text
import java.util.*

class TreeAdapter : BaseBindingQuickAdapter<SysTree, ItemTreeBinding>(R.layout.item_tree),
    TagFlowLayout.OnTagClickListener {


    override fun convert(holder: BaseBindingHolder, item: SysTree) {
        holder.setText(R.id.tv_tree_title, item.name)

        val flowLayout = holder.getView<TagFlowLayout>(R.id.flow_tree)

        //根据状态处理显示结果
        if (item.isShow) {
            flowLayout.visibility = View.VISIBLE
            holder.setImageResource(R.id.iv_tree_down, R.mipmap.up)
        } else {
            flowLayout.visibility = View.GONE
            holder.setImageResource(R.id.iv_tree_down, R.mipmap.down)
        }

        flowLayout.adapter = object : TagAdapter<SysTreeChildren>(item.children) {
            override fun getView(parent: FlowLayout?, position: Int, t: SysTreeChildren?): View {
                val tvTag = LayoutInflater.from(context).inflate(
                    R.layout.item_tree_tag,
                    flowLayout, false
                ) as TextView
                if (t != null) {
                    tvTag.text = t.name
                }
                tvTag.setTextColor(randomColor())
                return tvTag
            }
        }
        flowLayout.setOnTagClickListener(this)
    }


    private var mOnItemTagClickListener: OnItemTagClickListener? = null

    /**
     * 定义接口,比原来接口多一个parentPosition参数，父view的position
     */
    interface OnItemTagClickListener {
        fun onItemTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean
    }

    /**
     * 给adapter添加事件方法
     */
    fun setOnItemTagClickListener(listener: OnItemTagClickListener) {
        mOnItemTagClickListener = listener
    }

    override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
        mOnItemTagClickListener?.onItemTagClick(view, position, parent)
        return true
    }

    fun randomColor(): Int {
        Random().run {
            val red = nextInt(210)
            val green = nextInt(210)
            val blue = nextInt(210)
            return Color.rgb(red, green, blue)
        }
    }

}
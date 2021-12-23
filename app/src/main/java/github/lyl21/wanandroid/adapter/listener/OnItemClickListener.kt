package github.lyl21.wanandroid.adapter.listener

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

public  interface OnItemClickListener {
    fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)
}
package github.lyl21.wanandroid.adapter.listener

import android.view.View
import github.lyl21.wanandroid.adapter.BaseBindingQuickAdapter

public  interface OnItemClickListener {
    fun onItemClick(adapter: BaseBindingQuickAdapter<*, *>, view: View, position: Int)
}
package github.lyl21.wanandroid.adapter.listener

import android.view.View
import github.lyl21.wanandroid.adapter.BaseBindingQuickAdapter

public interface OnItemChildClickListener {
    fun onItemChildClick(adapter: BaseBindingQuickAdapter<*, *>, view: View, position: Int)
}
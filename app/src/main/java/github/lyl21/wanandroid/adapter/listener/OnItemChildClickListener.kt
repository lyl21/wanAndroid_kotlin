package github.lyl21.wanandroid.adapter.listener

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

public interface OnItemChildClickListener {
    fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)
}
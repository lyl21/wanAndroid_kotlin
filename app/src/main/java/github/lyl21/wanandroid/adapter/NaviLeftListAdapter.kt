package github.lyl21.wanandroid.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.bean.NaviInfo
import github.lyl21.wanandroid.databinding.ItemTextBinding

class NaviLeftListAdapter : BaseQuickAdapter<NaviInfo, BaseDataBindingHolder<ItemTextBinding>>
    (R.layout.item_text) {

    override fun convert(holder: BaseDataBindingHolder<ItemTextBinding>, item: NaviInfo) {
        holder.setText(R.id.tv, item.name)
        if (item.isChecked) {
            holder.dataBinding!!.tv.setBackgroundColor(context.resources.getColor(R.color.shallow_gray))
            item.isChecked=false
        } else {
            holder.dataBinding!!.tv.setBackgroundColor(context.resources.getColor(R.color.background))
        }
    }
}
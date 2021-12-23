package github.lyl21.wanandroid.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.databinding.ItemLocationBinding

class LocationAdapter : BaseQuickAdapter<String, BaseDataBindingHolder<ItemLocationBinding>>(R.layout.item_location) {

    override fun convert(holder: BaseDataBindingHolder<ItemLocationBinding>, item: String) {

        holder.setText(R.id.tv_item_location_title, item)
        holder.setText(R.id.tv_item_location_des, item)

    }

}
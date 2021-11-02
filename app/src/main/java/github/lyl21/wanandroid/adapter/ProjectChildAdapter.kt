package github.lyl21.wanandroid.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.databinding.ItemProjectChildBinding
import github.lyl21.wanandroid.entity.ProjectClassInfoChild
import github.lyl21.wanandroid.entity.ProjectClassInfoChildData

class ProjectChildAdapter : BaseBindingQuickAdapter
<ProjectClassInfoChildData, ItemProjectChildBinding>(R.layout.item_project_child),
    LoadMoreModule {


    override fun convert(holder: BaseBindingHolder, item: ProjectClassInfoChildData) {
        Glide.with(context).load(item.envelopePic).into(holder.getView(R.id.iv_project_img))
        holder.setText(R.id.tv_project_title, item.title)
        holder.setText(R.id.tv_project_desc, item.desc)
        holder.setText(R.id.tv_project_date, item.niceDate)
        holder.setText(R.id.tv_project_author, item.author)
    }


}
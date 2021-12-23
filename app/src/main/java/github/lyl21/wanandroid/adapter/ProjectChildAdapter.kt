package github.lyl21.wanandroid.adapter

import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.bean.ProjectClassInfoChild
import github.lyl21.wanandroid.databinding.ItemProjectChildBinding

class ProjectChildAdapter :
    BaseQuickAdapter<ProjectClassInfoChild, BaseDataBindingHolder<ItemProjectChildBinding>>(R.layout.item_project_child){


    override fun convert(
        holder: BaseDataBindingHolder<ItemProjectChildBinding>,
        item: ProjectClassInfoChild
    ) {
        holder.dataBinding!!.ivProjectImg
            .load(item.envelopePic) {
                crossfade(true)
            }
        holder.setText(R.id.tv_project_title, item.title)
        holder.setText(R.id.tv_project_desc, item.desc)
        holder.setText(R.id.tv_project_date, item.niceDate)
        holder.setText(R.id.tv_project_author, item.author)
    }


}
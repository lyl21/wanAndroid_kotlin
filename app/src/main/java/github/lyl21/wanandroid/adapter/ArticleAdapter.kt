package github.lyl21.wanandroid.adapter

import android.text.Html
import android.widget.ImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.bean.ArticleInfo
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.databinding.ItemArticleBinding

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 17:05
 */
class ArticleAdapter :
    BaseQuickAdapter<ArticleInfo, BaseDataBindingHolder<ItemArticleBinding>>(R.layout.item_article){

    private var imgLists: MutableList<String> = mutableListOf()

    init {
        addChildClickViewIds(R.id.iv_home_article_like)
        addChildClickViewIds(R.id.iv_home_article_download)
    }

    override fun convert(holder: BaseDataBindingHolder<ItemArticleBinding>, item: ArticleInfo) {

        for (index in 0..7) {
            val decodeString = MMKV.defaultMMKV().decodeString("bingImgList[$index]")
            decodeString?.let { imgLists.add(index, it) }
        }
        holder.dataBinding!!.ivHomeArticle
            .load(imgLists[(0..7).random()]) {
                crossfade(true)
                transformations(RoundedCornersTransformation(15f, 15f, 15f, 15f))
            }

                holder.dataBinding!!.tvHomeArticleTitle.text = Html.fromHtml(item.title)
                holder.setText(R.id.tv_home_article_tag, item.chapterName)

                if (item.niceDate.contains("-")) {
                    val length = item.niceDate.length
                    holder.setText(
                        R.id.tv_home_article_time,
                        item.niceDate.substring(0, length - 5)
                    )
                } else {
                    holder.setText(R.id.tv_home_article_time, item.niceDate)
                }

    }
}
package github.lyl21.wanandroid.adapter

import android.text.Html
import coil.load
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.tencent.mmkv.MMKV
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.databinding.ItemArticleBinding
import github.lyl21.wanandroid.bean.CollectInfo
import github.lyl21.wanandroid.common.ConstantParam

/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/29 17:05
 */
class CollectAdapter :
    BaseQuickAdapter<CollectInfo, BaseDataBindingHolder<ItemArticleBinding>>(R.layout.item_article),
    LoadMoreModule {

    private var imgLists: MutableList<String>

    init {
        addChildClickViewIds(R.id.iv_home_article_like)
        imgLists = mutableListOf()
        for (index in 0..7){
            val decodeString = MMKV.defaultMMKV().decodeString("bingImgList[$index]")
            decodeString?.let { imgLists.add(index, it) };
        }
    }


    override fun convert(holder: BaseDataBindingHolder<ItemArticleBinding>, item: CollectInfo) {
        LogUtils.e("lll", item.author)
        holder.setVisible(R.id.card_home_article, true)

        holder.dataBinding!!.ivHomeArticle
            .load(ConstantParam.BingBaseUrl + imgLists[(0..7).random()]) {
                crossfade(true)
                transformations(RoundedCornersTransformation(15f, 15f, 15f, 15f))
            }
        holder.setText(R.id.tv_home_article_title, Html.fromHtml(item.title))
        holder.setText(R.id.tv_home_article_tag, item.chapterName)
        holder.setText(R.id.tv_home_article_time, item.niceDate)

        holder.dataBinding!!.ivHomeArticleLike
            .load(R.mipmap.like_checked){
                crossfade(true)
            }

    }
}
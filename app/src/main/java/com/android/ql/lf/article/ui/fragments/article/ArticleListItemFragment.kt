package com.android.ql.lf.article.ui.fragments.article

import android.view.View
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.ui.adapters.ArticleListAdapter
import com.android.ql.lf.article.utils.ARTICLE_LIST_ACT
import com.android.ql.lf.article.utils.ARTICLE_MODULE
import com.android.ql.lf.article.utils.getBaseParamsWithPage
import com.android.ql.lf.baselibaray.ui.fragment.AbstractLazyLoadFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf
import java.util.ArrayList

class ArticleListItemFragment : AbstractLazyLoadFragment<ArticleItem>() {

    companion object {

        const val INVALID_ID = 0

        fun startArticleListItem(classifyId: Int, ageId: Int, addressId: Int,like:Int = 1): ArticleListItemFragment {
            val articleListItemFragment = ArticleListItemFragment()
            articleListItemFragment.arguments = bundleOf(Pair("classifyId", classifyId), Pair("ageId", ageId), Pair("addressId", addressId),Pair("like",like)   )
            return articleListItemFragment
        }
    }

    override fun createAdapter(): BaseQuickAdapter<ArticleItem, BaseViewHolder> = ArticleListAdapter(mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
    }

    private val classifyId by lazy {
        arguments?.getInt("classifyId", INVALID_ID) ?: INVALID_ID
    }

    private val ageId by lazy {
        arguments?.getInt("ageId", INVALID_ID) ?: INVALID_ID
    }

    private val addressId by lazy {
        arguments?.getInt("addressId", INVALID_ID) ?: INVALID_ID
    }

    private val like by lazy {
        arguments?.getInt("like", INVALID_ID) ?: INVALID_ID
    }

    override fun loadData() {
        mPresent.getDataByPost(
            0x0, getBaseParamsWithPage(ARTICLE_MODULE, ARTICLE_LIST_ACT, currentPage)
                .addParam("classify", classifyId)
                .addParam("age", ageId)
                .addParam("address", addressId)
                .addParam("like",like)
                .addParam("reuid", "")
        )
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun getEmptyMessage(): String {
        return "暂没有相关文章哦~"
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            isLoad = true
            processList(result as String, ArticleItem::class.java)
        }
    }

    override fun actionTempList(tempList: ArrayList<ArticleItem>?) {
        tempList?.forEach {
            it.mType = if (it.articles_picCount > 2) ArticleListAdapter.MULTI_IMAGE_TYPE else ArticleListAdapter.SINGLE_IMAGE_TYPE
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val tempItem = mArrayList[position]
        ArticleInfoForNormalFragment.startArticleInfoForNormal(mContext,
            tempItem.articles_id!!,
            tempItem.articles_uid!!,
            tempItem.articles_like,
            tempItem.articles_love)
    }
}
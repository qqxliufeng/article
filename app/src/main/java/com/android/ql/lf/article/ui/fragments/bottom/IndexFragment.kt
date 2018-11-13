package com.android.ql.lf.article.ui.fragments.bottom

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.ui.fragments.article.*
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.article.utils.ARTICLENAV_ACT
import com.android.ql.lf.article.utils.ARTICLE_MODULE
import com.android.ql.lf.article.utils.getBaseParamsWithModAndAct
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_bottom_index_layout.*
import org.json.JSONObject

class IndexFragment : BaseNetWorkingFragment() {

    val titles = arrayListOf("类别", "年龄", "地域")

    private val typeList = arrayListOf<Classify>()
    private val addressList = arrayListOf<Classify>()
    private val ageList = arrayListOf<Classify>()

    override fun getLayoutId() = R.layout.fragment_bottom_index_layout

    override fun initView(view: View?) {
        mIvBottomIndexSearch.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "搜索", "search.html", ArticleType.OTHER.type)
        }
        mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(ARTICLE_MODULE, ARTICLENAV_ACT))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0){
            val check = checkResultCode(result)
            if (check!=null && check.code == SUCCESS_CODE){
                (check.obj as JSONObject).optJSONObject(RESULT_OBJECT).let {
                    val addressJsonArray = it.optJSONArray("address")
                    val classifyJsonAddress = it.optJSONArray("classify")
                    val ageJsonAddress = it.optJSONArray("age")
                    (0 until addressJsonArray.length()).forEach {
                        val json = addressJsonArray.optJSONObject(it)
                        val classify = Classify(json.optInt("address_id"),json.optString("address_name"))
                        addressList.add(classify)
                    }
                    (0 until classifyJsonAddress.length()).forEach {
                        val json = classifyJsonAddress.optJSONObject(it)
                        val classify = Classify(json.optInt("classify_id"),json.optString("classify_title"))
                        typeList.add(classify)
                    }
                    (0 until ageJsonAddress.length()).forEach {
                        val json = ageJsonAddress.optJSONObject(it)
                        val classify = Classify(json.optInt("age_id"),json.optString("age_title"))
                        ageList.add(classify)
                    }
                }
                mVpIndexBottom.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
                    override fun getItem(position: Int): Fragment {
                        return when(position){
                            0-> ArticleListFragment.startTypeFragment(position,typeList)
                            1-> ArticleListFragment.startTypeFragment(position,ageList)
                            2-> ArticleListFragment.startTypeFragment(position,addressList)
                            else-> ArticleListFragment.startTypeFragment(position,typeList)
                        }
                    }

                    override fun getCount() = titles.size

                    override fun getPageTitle(position: Int) = titles[position]
                }
                mVpIndexBottom.offscreenPageLimit = 3
                mTlIndexBottom.setupWithViewPager(mVpIndexBottom)
            }
        }
    }
}
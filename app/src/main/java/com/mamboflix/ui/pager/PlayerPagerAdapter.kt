package com.mamboflix.ui.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mamboflix.model.contentdetails.RelatedContentModel
import com.mamboflix.ui.fragment.PlayerMoreLikeFragment
import java.util.ArrayList


class PlayerPagerAdapter(
    fragmentManager: FragmentManager,
    var listData: ArrayList<RelatedContentModel> = ArrayList(),var content_Id:String/*,var episode_Id: String*/
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        return  PlayerMoreLikeFragment.newInstance(content_Id,/*episode_Id,*/listData[position].id,listData[position].is_episode)

    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listData[position].name
    }
}
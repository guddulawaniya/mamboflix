package com.mamboflix.model.hometab

import com.mamboflix.model.hometab.becauseyou.BecauseYouContentModel
import com.mamboflix.model.hometab.continuewatch.ContinueWatchModel
import com.mamboflix.model.hometab.mostwatched.MostWatchedModel
import com.mamboflix.model.hometab.mylist.MyListModel
import com.mamboflix.model.hometab.preview.PreviewModel
import com.mamboflix.model.hometab.topbanner.TopBannerModel
import com.mamboflix.model.hometab.trending.TrendingModel

class HomeTabModel(
    val catgorie_content : ArrayList<CategoryContentModel>?=null,
    val continue_watch : ArrayList<ContinueWatchModel>?=null,
    val last_based_content : ArrayList<BecauseYouContentModel>?=null,
    val most_watched : ArrayList<MostWatchedModel>?=null,
    val mylist : ArrayList<MyListModel>?=null,
    val preview : ArrayList<PreviewModel>?=null,
    val trending : ArrayList<TrendingModel>?=null,
    val mambo_original : ArrayList<MamboOriginalmODEL>?=null,
    val top_banner : TopBannerModel?=null
)
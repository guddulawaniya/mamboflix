package com.mamboflix.model.shows

import com.mamboflix.model.MoviesAndShowsCategory
import com.mamboflix.model.hometab.CategoryContentModel
import com.mamboflix.model.hometab.preview.PreviewModel
import com.mamboflix.model.hometab.topbanner.TopBannerModel

class ShowsTabModel(
    val main_data : ShowsData,
    val shows_category : ArrayList<MoviesAndShowsCategory>
)

class ShowsData(
        val catgorie_content : ArrayList<CategoryContentModel>,
        val preview : ArrayList<PreviewModel>,
        val top_banner : TopBannerModel?=null
)
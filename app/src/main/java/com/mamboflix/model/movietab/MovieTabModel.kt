package com.mamboflix.model.movietab

import com.mamboflix.model.MoviesAndShowsCategory
import com.mamboflix.model.PreviewMovieTabModelClass
import com.mamboflix.model.hometab.topbanner.TopBannerModel
import com.mamboflix.model.movieTabModel.CategoryContentMovieTabModel

class MovieTabModel(
        val main_data : MoviesData,
        val movies_category : ArrayList<MoviesAndShowsCategory>
)


class MoviesData(
        val catgorie_content : ArrayList<CategoryContentMovieTabModel>,
        val preview : ArrayList<PreviewMovieTabModelClass>,
        val top_banner : TopBannerModel?=null
)
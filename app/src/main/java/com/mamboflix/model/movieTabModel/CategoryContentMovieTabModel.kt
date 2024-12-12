package com.mamboflix.model.movieTabModel

import com.mamboflix.model.hometab.CategoryModel

data class CategoryContentMovieTabModel(val content : ArrayList<CategoryModel>,
                                        val created_at : String,
                                        val description : String,
                                        val id : String,
                                        val status : String,
                                        val title : String,
                                        val updated_at : String)

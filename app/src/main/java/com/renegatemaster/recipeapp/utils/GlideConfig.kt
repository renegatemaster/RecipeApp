package com.renegatemaster.recipeapp.utils

import com.bumptech.glide.request.RequestOptions
import com.renegatemaster.recipeapp.R

object GlideConfig {
    val sharedOptions = RequestOptions()
        .placeholder(R.drawable.img_placeholder)
        .error(R.drawable.img_error)
}
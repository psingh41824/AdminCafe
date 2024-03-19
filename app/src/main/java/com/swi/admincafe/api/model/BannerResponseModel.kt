package com.swi.admincafe.api.model

data class BannerResponseModel(
    val `data`: List<Banner>,
    val msg: String,
    val success: Boolean
)
package com.swi.admincafe.api.model

data class CategoryResponseModel(
    val `data`: List<Category>,
    val msg: String,
    val success: Boolean
)
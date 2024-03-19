package com.swi.admincafe.api.model

data class ProductResponseModel(
    val product: List<Product>,
    val success: Boolean
)
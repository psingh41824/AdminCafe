package com.swi.admincafe.api.model

data class Product(
    val category: Category,
    val description: String,
    val image: String,
    val name: String,
    val numReviews: Int,
    val price: Int,
    val rating: String,
    val size: String
)
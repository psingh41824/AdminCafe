package com.swi.admincafe.api.model

import com.swi.admincafe.api.model.Category

data class Product(
    val __v: Int,
    val _id: String,
    val category: Category,
    val dateCreated: String,
    val description: String,
    val image: String,
    val name: String,
    val numReviews: Int,
    val price: Int,
    val rating: String,
    val size: String
)
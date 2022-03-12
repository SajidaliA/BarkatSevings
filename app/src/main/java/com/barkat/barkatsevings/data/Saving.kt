package com.barkat.barkatsevings.data

/**
 * Created by Sajid Ali Suthar
 */
data class Saving(
    val id: Int,
    val month: String,
    val year: String,
    val dateOfPayment: String,
    val status: String,
    val amount: String
)

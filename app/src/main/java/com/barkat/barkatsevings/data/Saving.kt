package com.barkat.barkatsevings.data

/**
 * Created by Sajid Ali Suthar
 */
data class Saving(
    val id: Int?= null,
    var month: String?= null,
    var year: String?= null,
    var dateOfPayment: String?= null,
    var status: String?= null,
    var amount: String?= null
){
    constructor() : this(null)
}

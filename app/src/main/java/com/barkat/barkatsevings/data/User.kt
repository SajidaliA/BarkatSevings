package com.barkat.barkatsevings.data

/**
 * Created by Sajid Ali Suthar
 */
data class User(
    val id: String? = null,
    val email: String? = null,
    val mobileNumber: String? = null,
    val name: String? = null,
    var totalSavings: String? = null,
    val joinDate: String? = null,
    var profileImage: String? = null
){
    constructor() : this(null)
}

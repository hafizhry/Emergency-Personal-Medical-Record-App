package com.capstone.personalmedicalrecord.core.domain.model

data class Staff(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val hospital: String,
)
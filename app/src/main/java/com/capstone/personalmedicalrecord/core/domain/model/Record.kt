package com.capstone.personalmedicalrecord.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Record(
    val date: String,
    val description: String,
    val place: String
) : Parcelable
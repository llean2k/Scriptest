package dev.firecrown.scriptest.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal data class ResultEntity(
    val textField: String?,
    val snapshot: String?,
    val option: String?,
    val exceptionMessage: String?,
    val timestamp: String
)

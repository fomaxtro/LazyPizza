package com.fomaxtro.core.data.session.model

import kotlinx.serialization.Serializable

@Serializable
data class ToppingSelectionSession(
    val id: Long,
    val quantity: Int
)

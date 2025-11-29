package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.util.Error
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.ui.Resource

fun <D, E : Error> Result<D, E>.toResource(): Resource<D> {
    return when (this) {
        is Result.Error -> Resource.Error
        is Result.Success -> Resource.Success(data)
    }
}
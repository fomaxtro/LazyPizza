package com.fomaxtro.core.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.fomaxtro.core.presentation.R

object AppIcons {
    object Outlined {
        val Phone: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.phone)

        val Search: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.search)

        val Trash: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.trash)
    }

    object Filled {
        val Minus: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.minus)

        val Plus: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.plus)

        val Phone: ImageVector
            @Composable get() = ImageVector.vectorResource(R.drawable.phone_filled)
    }
}
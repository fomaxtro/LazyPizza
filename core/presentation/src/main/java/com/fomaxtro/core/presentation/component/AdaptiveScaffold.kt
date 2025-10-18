package com.fomaxtro.core.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textPrimary
import com.fomaxtro.core.presentation.ui.ScreenType
import com.fomaxtro.core.presentation.ui.currentScreenType

@Composable
fun AdaptiveScaffold(
    navigation: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val screenType = currentScreenType()

    when (screenType) {
        ScreenType.MOBILE -> {
            Scaffold(
                topBar = topBar,
                bottomBar = {
                    val navigationBarShape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )

                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .clip(navigationBarShape)
                            .dropShadow(
                                shape = navigationBarShape,
                                shadow = Shadow(
                                    radius = 16.dp,
                                    color = MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.06f),
                                    offset = DpOffset(0.dp, (-4).dp)
                                )
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement
                                .spacedBy(8.dp, alignment = Alignment.CenterHorizontally)
                        ) {
                            navigation()
                        }
                    }
                },
                content = content,
                modifier = modifier
            )
        }

        ScreenType.WIDE_SCREEN -> {

        }
    }
}

@Preview
@Composable
private fun AdaptiveScaffoldPreview() {
    LazyPizzaTheme {
        AdaptiveScaffold(
            navigation = {
                NavigationButton(
                    selected = true,
                    label = "Menu",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                )

                NavigationButton(
                    selected = false,
                    label = "Cart",
                    icon = {
                        BadgedBox(
                            badge = {
                                NotificationBadge(
                                    value = 2
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null
                            )
                        }
                    }
                )

                NavigationButton(
                    selected = false,
                    label = "History",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null
                        )
                    }
                )
            }
        ) {}
    }
}
package com.fomaxtro.core.presentation.screen.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.component.AdaptiveScaffold
import com.fomaxtro.core.presentation.component.NavigationButton
import com.fomaxtro.core.presentation.component.NotificationBadge
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body1Regular
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentDestination: HomeDestination,
    onDestinationClick: (HomeDestination) -> Unit,
    content: @Composable () -> Unit
) {
    val isInPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    AdaptiveScaffold(
        topBar = {
            LazyPizzaTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = stringResource(R.string.app_name),
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(stringResource(R.string.app_name))
                    }
                },
                actions = {
                    Icon(
                        imageVector = AppIcons.Filled.Phone,
                        contentDescription = stringResource(R.string.contact)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    val contactPhoneNumber = stringResource(R.string.contact_phone_number)

                    Text(
                        text = contactPhoneNumber,
                        style = MaterialTheme.typography.body1Regular,
                        modifier = Modifier
                            .clickable {
                                if (!isInPreview) {
                                    val dialerIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = "tel:$contactPhoneNumber".toUri()
                                    }

                                    context.startActivity(dialerIntent)
                                }
                            }
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            )
        },
        navigation = {
            NavigationButton(
                selected = currentDestination == HomeDestination.MENU,
                onClick = {
                    if (currentDestination != HomeDestination.MENU) {
                        onDestinationClick(HomeDestination.MENU)
                    }
                },
                icon = {
                    Icon(
                        imageVector = AppIcons.Filled.Menu,
                        contentDescription = stringResource(R.string.menu)
                    )
                },
                label = stringResource(R.string.menu)
            )

            NavigationButton(
                selected = currentDestination == HomeDestination.CART,
                onClick = {
                    if (currentDestination != HomeDestination.CART) {
                        onDestinationClick(HomeDestination.CART)
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            NotificationBadge(
                                value = 2
                            )
                        }
                    ) {
                        Icon(
                            imageVector = AppIcons.Filled.Cart,
                            contentDescription = stringResource(R.string.cart)
                        )
                    }
                },
                label = stringResource(R.string.cart)
            )

            NavigationButton(
                selected = currentDestination == HomeDestination.HISTORY,
                onClick = {
                    if (currentDestination != HomeDestination.HISTORY) {
                        onDestinationClick(HomeDestination.HISTORY)
                    }
                },
                icon = {
                    Icon(
                        imageVector = AppIcons.Filled.History,
                        contentDescription = stringResource(R.string.history)
                    )
                },
                label = stringResource(R.string.history)
            )
        },
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            onDestinationClick = {},
            currentDestination = HomeDestination.MENU
        ) {

        }
    }
}
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    content: @Composable () -> Unit
) {
    val isInPreview = LocalInspectionMode.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
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

                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = AppIcons.Filled.Phone,
                            contentDescription = stringResource(R.string.contact)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        val contactPhoneNumber = stringResource(R.string.contact_phone_number)

                        Text(
                            text = contactPhoneNumber,
                            style = MaterialTheme.typography.bodyLarge,
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
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                }
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
        HomeScreen {

        }
    }
}
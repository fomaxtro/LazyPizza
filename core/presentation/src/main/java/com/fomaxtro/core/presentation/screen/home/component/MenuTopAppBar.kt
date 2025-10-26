package com.fomaxtro.core.presentation.screen.home.component

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.AppIcons
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body1Regular
import com.fomaxtro.core.presentation.designsystem.top_bar.LazyPizzaTopAppBar

@Composable
fun MenuTopAppBar(
    modifier: Modifier = Modifier
) {
    val isInPreview = LocalInspectionMode.current
    val context = LocalContext.current

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
                    .clickable(enabled = isInPreview) {
                        val dialerIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:$contactPhoneNumber".toUri()
                        }

                        context.startActivity(dialerIntent)
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun MenuTopAppBarPreview() {
    LazyPizzaTheme {
        MenuTopAppBar()
    }
}
package com.fomaxtro.core.presentation.designsystem.top_bar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body1Medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyPizzaCenteredAlignedTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.body1Medium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun LazyPizzaCenteralignedTopAppBarPreview() {
    LazyPizzaTheme {
        LazyPizzaCenteredAlignedTopAppBar(
            title = "Title"
        )
    }
}
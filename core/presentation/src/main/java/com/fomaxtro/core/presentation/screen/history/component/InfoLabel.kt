package com.fomaxtro.core.presentation.screen.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fomaxtro.core.presentation.designsystem.theme.InstrumentSans
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.textOnPrimary

@Composable
fun InfoLabel(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.textOnPrimary,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = shape
            )
            .padding(
                horizontal = 8.dp,
                vertical = 2.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = InstrumentSans,
                fontStyle = FontStyle.Normal,
                fontSize = 10.sp,
                lineHeight = 16.sp,
                color = contentColor
            )
        )
    }
}

@Preview
@Composable
private fun InfoLabelPreview() {
    LazyPizzaTheme {
        InfoLabel(
            containerColor = MaterialTheme.colorScheme.primary,
            text = "In Progress"
        )
    }
}
package com.fomaxtro.core.presentation.designsystem.text_field

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body2Regular

@Composable
fun LazyPizzaOutlinedFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        colors = LazyPizzaOutlinedFormTextFieldDefaults.colors(),
        shape = CircleShape,
        placeholder = placeholder?.let { placeholder ->
            @Composable {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.body2Regular
                )
            }
        },
        textStyle = MaterialTheme.typography.body2Regular,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine
    )
}

@Preview
@Composable
private fun LazyPizzaOutlinedFormTextFieldPreview() {
    LazyPizzaTheme {
        LazyPizzaOutlinedFormTextField(
            value = "Foo",
            onValueChange = {},
            placeholder = "Placeholder"
        )
    }
}
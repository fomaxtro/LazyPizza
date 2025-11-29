package com.fomaxtro.core.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaButton
import com.fomaxtro.core.presentation.designsystem.button.LazyPizzaTextButton
import com.fomaxtro.core.presentation.designsystem.theme.LazyPizzaTheme
import com.fomaxtro.core.presentation.designsystem.theme.body4Regular
import com.fomaxtro.core.presentation.designsystem.theme.label2Semibold
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHighest
import com.fomaxtro.core.presentation.designsystem.theme.textSecondary
import com.fomaxtro.core.presentation.ui.UiText
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    onDismissRequest: () -> Unit,
    onDateTimeSelected: (ZonedDateTime) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    initialUtcMillis: Long = Instant.now().toEpochMilli(),
    error: UiText? = null
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            val todayUtc = LocalDate.now()
                .atStartOfDay(ZoneOffset.UTC)
                .toEpochSecond() * 1000

            override fun isSelectableYear(year: Int): Boolean {
                return LocalDate.now().year <= year
            }

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayUtc
            }
        },
        initialSelectedDate = Instant.ofEpochMilli(initialUtcMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    )

    val initialLocalTime = Instant.ofEpochMilli(initialUtcMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = initialLocalTime.hour,
        initialMinute = initialLocalTime.minute
    )

    var dateSelected by rememberSaveable {
        mutableStateOf(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            dateSelected = false
        }
    }

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .clip(AlertDialogDefaults.shape)
                .fillMaxWidth(),
            color = containerColor
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    targetState = dateSelected,
                ) { dateSelected ->
                    if (dateSelected) {
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Min),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.select_time).uppercase(),
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .padding(top = 24.dp)
                                    .align(Alignment.Start),
                                style = MaterialTheme.typography.label2Semibold,
                                color = MaterialTheme.colorScheme.textSecondary,
                            )

                            TimeInput(
                                state = timePickerState,
                                modifier = Modifier
                                    .padding(16.dp),
                                colors = TimePickerDefaults.colors(
                                    timeSelectorSelectedContainerColor = containerColor,
                                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceHighest
                                )
                            )

                            if (error != null) {
                                Text(
                                    text = error.asString(),
                                    style = MaterialTheme.typography.body4Regular,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    } else {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                containerColor = containerColor
                            )
                        )
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    LazyPizzaTextButton(
                        onClick = {
                            onDismissRequest()

                            dateSelected = false
                        },
                        text = stringResource(R.string.cancel)
                    )

                    LazyPizzaButton(
                        onClick = {
                            if (dateSelected) {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val time = LocalTime.of(
                                        timePickerState.hour,
                                        timePickerState.minute
                                    )

                                    val dateTime = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC)
                                        .with(time)
                                        .withZoneSameLocal(ZoneId.systemDefault())

                                    onDateTimeSelected(dateTime)
                                }

                            } else {
                                dateSelected = true
                            }
                        },
                        text = stringResource(R.string.ok),
                        enabled = datePickerState.selectedDateMillis != null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DateTimePickerPreview() {
    LazyPizzaTheme {
        DateTimePicker(
            onDismissRequest = {},
            onDateTimeSelected = {},
            error = UiText.DynamicString("asjdhasjkdhjashdkjahsdjhaskjdhjaksajkshdajkshdkhaskdj")
        )
    }
}
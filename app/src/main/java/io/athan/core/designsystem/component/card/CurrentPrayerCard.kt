package io.athan.core.designsystem.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.athan.core.designsystem.component.feedback.PrayerStatusTag
import io.athan.core.designsystem.theme.AthanSpacing
import io.athan.core.designsystem.theme.AthanTheme

@Composable
fun CurrentPrayerCard(
    name: String,
    time: String,
    modifier: Modifier = Modifier,
    isNow: Boolean = false,
    useDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val containerColor = when {
        isNow -> MaterialTheme.colorScheme.primaryContainer
        useDarkTheme -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when {
        isNow -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val border = if (useDarkTheme && !isNow) {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    } else {
        null
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier.padding(AthanSpacing.large) // 24.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                )
                if (isNow) {
                    PrayerStatusTag()
                }
            }

            Text(
                text = time,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@PreviewLightDark
@Composable
fun CurrentPrayerCardPreview() {
    AthanTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(AthanSpacing.medium),
            modifier = Modifier.padding(AthanSpacing.medium)
        ) {
            CurrentPrayerCard(
                name = "Dhuhr",
                time = "13:39",
                isNow = true
            )
            CurrentPrayerCard(
                name = "Asr",
                time = "16:45",
                isNow = false
            )
        }
    }
}
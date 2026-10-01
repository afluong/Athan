package io.athan.core.designsystem.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.athan.core.designsystem.theme.AthanSpacing
import io.athan.core.designsystem.theme.AthanTheme

@Composable
fun PrayerItemRow(
    name: String,
    time: String,
    modifier: Modifier = Modifier,
    isPast: Boolean = false,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val containerColor = if (isPast) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (isPast) {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    } else {
        color.copy(alpha = 0.6f)
    }

    val textColor = if (isPast) {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AthanSpacing.medium,
                    horizontal = AthanSpacing.large
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isPast) FontWeight.Normal else FontWeight.SemiBold,
                color = textColor
            )
            Text(
                text = time,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isPast) FontWeight.Normal else FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PrayerItemRowPreview() {
    AthanTheme {
        Column(
            modifier = Modifier.padding(AthanSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(AthanSpacing.small)
        ) {
            PrayerItemRow("Fajr", "05:42", isPast = true)
            PrayerItemRow("Dhuhr", "13:39", isPast = false)
        }
    }
}
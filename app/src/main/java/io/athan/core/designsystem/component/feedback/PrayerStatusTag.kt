package io.athan.core.designsystem.component.feedback

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import io.athan.core.designsystem.theme.AthanSpacing
import io.athan.core.designsystem.theme.AthanTheme

@Composable
fun PrayerStatusTag(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = "Now",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = AthanSpacing.medium,
                vertical = AthanSpacing.small
            )
        )
    }
}

@PreviewLightDark
@Composable
fun PrayerStatusTagPreview() {
    AthanTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.padding(AthanSpacing.medium)
        ) {
            PrayerStatusTag()
        }
    }
}
package io.athan.core.designsystem.component.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.athan.core.designsystem.theme.AthanSpacing
import io.athan.core.designsystem.theme.AthanTheme

@Composable
fun LocationSuggestionItem(
    name: String,
    country: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemShape = MaterialTheme.shapes.medium

    Surface(
        shape = itemShape,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .fillMaxWidth()
            .clip(itemShape)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AthanSpacing.medium,
                    vertical = AthanSpacing.small
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AthanSpacing.medium)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(AthanSpacing.extraSmall)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (country.isNotBlank()) {
                    Text(
                        text = country,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun LocationSuggestionItemPreview() {
    AthanTheme {
        Column(
            modifier = Modifier.padding(AthanSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(AthanSpacing.small)
        ) {
            LocationSuggestionItem(
                name = "Paris",
                country = "France",
                onClick = {}
            )
        }
    }
}
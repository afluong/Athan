package io.athan.core.designsystem.component.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.athan.core.designsystem.theme.AthanSpacing
import io.athan.core.designsystem.theme.AthanTheme

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    val errorShape = MaterialTheme.shapes.extraLarge

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AthanSpacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AthanSpacing.medium)
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Box(
            modifier = Modifier
                .clip(errorShape)
                .background(color = MaterialTheme.colorScheme.errorContainer)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    horizontal = AthanSpacing.large,
                    vertical = AthanSpacing.medium
                )
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ErrorMessagePreview() {
    AthanTheme {
        ErrorMessage(
            message = "Aucune connexion Internet. Veuillez vérifier votre réseau puis réespérer."
        )
    }
}
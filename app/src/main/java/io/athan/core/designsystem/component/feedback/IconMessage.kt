package io.athan.core.designsystem.component.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IconMessage(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentDescription: String? = null,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.padding(bottom = 5.dp))

        Text(
            text = message,
            modifier = modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )

        )
    }
}

@Preview
@Composable
fun IconMessagePreview() {
    IconMessage(
        message = "No prediction found",
        icon = Icons.Default.LocationOff
    )
}
package com.gurkha.hr.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun ERPButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White,
    shape: Shape = MaterialTheme.shapes.medium,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    disableBackgroundColor: Color = Color.Gray,
    disableTextColor: Color = MaterialTheme.colorScheme.onBackground,
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    Button(
        enabled = enabled,
        modifier = modifier.height(48.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = disableBackgroundColor,
            disabledContentColor = disableTextColor
        ),
        shape = shape,
        border = border
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        trackColor = backgroundColor,
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 1.dp,
                        color = Color.Blue
                    )
                }
            } else {

                leadingIcon?.let {
                    leadingIcon()
                }
                Text(
                    text = text,
                    style = textStyle
                )
                trailingIcon?.let {
                    trailingIcon()
                }
            }
        }
    }
}
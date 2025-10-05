package com.gurkha.hr.chat_room.components.triangle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gurkha.hr.res.theme.dimens

@Composable
fun Triangle(
    risingToTheRight: Boolean,
    background: Color,
    bottomPadding: Dp = MaterialTheme.dimens.small2 + MaterialTheme.dimens.small1 / 2
) {
    Box(
        Modifier
            .padding(
                bottom = bottomPadding,
                start = 0.dp
            )
            .clip(TriangleEdgeShape(risingToTheRight))
            .background(background)
            .size(MaterialTheme.dimens.small2 + MaterialTheme.dimens.small1 / 2)
    )
}

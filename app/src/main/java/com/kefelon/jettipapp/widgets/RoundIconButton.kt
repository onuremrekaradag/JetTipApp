package com.kefelon.jettipapp.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kefelon.jettipapp.R


@Composable
fun RoundIconButton(
    modifier: Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp
) {
    Card(
        modifier = modifier
            .size(40.dp), shape = CircleShape, elevation = elevation, backgroundColor = backgroundColor
    ) {
        Image(
            modifier = modifier.clickable {
                onClick.invoke()
            }.then(Modifier.size(100.dp)), imageVector = imageVector,
            contentDescription = "",
            colorFilter = ColorFilter.tint(tint)
        )
    }
}

@Preview
@Composable
fun previewRoundIconButton() {
    RoundIconButton(modifier = Modifier,
        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
        onClick = { })
}
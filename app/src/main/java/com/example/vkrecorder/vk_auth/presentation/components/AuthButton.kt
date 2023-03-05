package com.example.vkrecorder.vk_auth.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.vkrecorder.R

@Composable
fun AuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.vk_compact_logo),
            tint = Color.Unspecified,
            contentDescription = stringResource(R.string.vk_description)
        )
    }
}
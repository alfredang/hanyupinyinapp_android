package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Reusable white card surface: rounded, soft shadow — the iOS `appCard()` equivalent. */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    val colors = CardDefaults.cardColors(containerColor = Brand.card)
    val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = shape,
            colors = colors, elevation = elevation) { content() }
    } else {
        Card(modifier = modifier.fillMaxWidth(), shape = shape,
            colors = colors, elevation = elevation) { content() }
    }
}

/** Rounded-square tinted icon badge — the iOS 48pt icon tile. */
@Composable
fun IconBadge(icon: ImageVector, color: Color, size: Int = 48) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size((size / 2).dp))
    }
}

/** Icon + title + subtitle + chevron navigation card row. */
@Composable
fun NavCard(
    icon: ImageVector,
    color: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBadge(icon, color)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Brand.ink)
                Text(subtitle, fontSize = 14.sp, color = Brand.mutedInk)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Brand.mutedInk)
        }
    }
}

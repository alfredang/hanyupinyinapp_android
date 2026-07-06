package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.hanyupinyin.BuildConfig

private const val DEVELOPER_URL = "https://www.tertiaryinfotech.com"

/** 关于 (About) — app card, developer card, version row. House style per android-feedback-about. */
@Composable
fun AboutScreen() {
    val uri = LocalUriHandler.current
    Scaffold(topBar = { LearnTopBar("关于") }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // App card
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconBadge(Icons.Filled.ChatBubble, Brand.accent, size = 52)
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text("汉语拼音", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Brand.ink)
                            Text("Hanyu Pinyin", fontSize = 14.sp, color = Brand.mutedInk)
                        }
                    }
                    Text("学习汉语拼音的声母、韵母和声调，并通过多种练习快速掌握拼音输入。",
                        fontSize = 14.sp, color = Brand.mutedInk)
                }
            }

            // Developer card
            Text("开发者 · DEVELOPER", fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                color = Brand.mutedInk)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Business, contentDescription = null,
                            tint = Brand.ink, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Tertiary Infotech Academy Pte. Ltd.", color = Brand.ink,
                            fontWeight = FontWeight.SemiBold)
                    }
                    HorizontalDivider()
                    Row(
                        Modifier.fillMaxWidth().clickable { uri.openUri(DEVELOPER_URL) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Filled.Language, contentDescription = null,
                            tint = Brand.secondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("tertiaryinfotech.com", color = Brand.secondary,
                            textDecoration = TextDecoration.Underline)
                    }
                }
            }

            // Version row
            AppCard {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("版本", fontWeight = FontWeight.Medium, color = Brand.ink)
                    Spacer(Modifier.weight(1f))
                    Text("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})", color = Brand.mutedInk)
                }
            }
        }
    }
}

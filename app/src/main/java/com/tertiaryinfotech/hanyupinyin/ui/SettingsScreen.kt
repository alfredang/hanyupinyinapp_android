package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.hanyupinyin.speech.Speaker
import com.tertiaryinfotech.hanyupinyin.speech.VoiceGender

/** 设置 (Settings) — pick the female/male reading voice, mirrors iOS SettingsView. */
@Composable
fun SettingsScreen(speaker: Speaker) {
    val selected = speaker.gender
    Scaffold(topBar = { LearnTopBar("设置") }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text("设置发音朗读使用的声音。", fontSize = 14.sp, color = Brand.mutedInk)

            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null,
                            tint = Brand.ink, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("朗读声音", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Brand.ink)
                    }

                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        VoiceGender.entries.forEachIndexed { i, g ->
                            SegmentedButton(
                                selected = selected == g,
                                onClick = { speaker.chooseGender(g) },
                                shape = SegmentedButtonDefaults.itemShape(i, VoiceGender.entries.size),
                            ) {
                                Text("${g.label} · ${g.englishLabel}")
                            }
                        }
                    }

                    if (!speaker.isAvailable(selected)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Warning, contentDescription = null,
                                tint = Brand.highlight, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "此设备未安装${selected.label}，将使用可用的中文语音。可在 设置 › 系统 › 语言和输入 › 文字转语音 中下载中文语音。",
                                fontSize = 13.sp, color = Brand.highlight,
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null,
                                tint = Brand.success, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("当前使用${selected.label}（${selected.englishLabel}）",
                                fontSize = 13.sp, color = Brand.success)
                        }
                    }

                    Button(
                        onClick = { speaker.speak("你好，欢迎学习汉语拼音") },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Brand.accent),
                    ) {
                        Icon(Icons.Filled.PlayCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("试听发音", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

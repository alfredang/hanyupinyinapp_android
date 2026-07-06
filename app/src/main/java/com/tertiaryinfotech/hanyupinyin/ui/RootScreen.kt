package com.tertiaryinfotech.hanyupinyin.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tertiaryinfotech.hanyupinyin.data.PracticeSession
import com.tertiaryinfotech.hanyupinyin.speech.Speaker

/** In-tab destinations of the 练习 (Practice) tab. */
private sealed interface PracticeDestination {
    data object Home : PracticeDestination
    data class Session(val session: PracticeSession) : PracticeDestination
}

/** Root bottom-tab navigation: 学习 · 练习 · 设置 · 反馈 · 关于 — mirrors the iOS MainTabView. */
@Composable
fun RootScreen(speaker: Speaker) {
    var tab by remember { mutableIntStateOf(0) }
    var learnDestination by remember { mutableStateOf<LearnDestination>(LearnDestination.Home) }
    var practiceDestination by remember { mutableStateOf<PracticeDestination>(PracticeDestination.Home) }

    // Hardware/gesture back pops to the tab's home screen.
    BackHandler(enabled = tab == 0 && learnDestination != LearnDestination.Home) {
        learnDestination = LearnDestination.Home
    }
    BackHandler(enabled = tab == 1 && practiceDestination != PracticeDestination.Home) {
        practiceDestination = PracticeDestination.Home
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    Triple("学习", Icons.AutoMirrored.Filled.MenuBook, 0),
                    Triple("练习", Icons.Filled.Keyboard, 1),
                    Triple("设置", Icons.Filled.Settings, 2),
                    Triple("反馈", Icons.AutoMirrored.Filled.Chat, 3),
                    Triple("关于", Icons.Filled.Info, 4),
                ).forEach { (label, icon, i) ->
                    NavigationBarItem(
                        selected = tab == i,
                        onClick = { tab = i },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                    )
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            when (tab) {
                0 -> when (val d = learnDestination) {
                    LearnDestination.Home -> LearnHomeScreen(onOpen = { learnDestination = it })
                    LearnDestination.Initials -> UnitGridScreen(
                        "声母", "23 个声母 · Initials",
                        com.tertiaryinfotech.hanyupinyin.data.PinyinData.initials,
                        speaker, onBack = { learnDestination = LearnDestination.Home })
                    LearnDestination.Finals -> UnitGridScreen(
                        "韵母", "常用韵母 · Finals",
                        com.tertiaryinfotech.hanyupinyin.data.PinyinData.finals,
                        speaker, onBack = { learnDestination = LearnDestination.Home })
                    LearnDestination.Tones -> TonesScreen(
                        speaker, onBack = { learnDestination = LearnDestination.Home })
                    LearnDestination.InputMethod -> InputMethodScreen(
                        onOpenSession = { learnDestination = LearnDestination.Session(it) },
                        onBack = { learnDestination = LearnDestination.Home })
                    LearnDestination.FastTyping -> FastTypingScreen(
                        onBack = { learnDestination = LearnDestination.Home })
                    is LearnDestination.Session -> PracticeSessionScreen(
                        d.session, speaker,
                        onBack = { learnDestination = LearnDestination.InputMethod })
                }
                1 -> when (val d = practiceDestination) {
                    PracticeDestination.Home -> PracticeHomeScreen(
                        onOpenSession = { practiceDestination = PracticeDestination.Session(it) })
                    is PracticeDestination.Session -> PracticeSessionScreen(
                        d.session, speaker,
                        onBack = { practiceDestination = PracticeDestination.Home })
                }
                2 -> SettingsScreen(speaker)
                3 -> FeedbackScreen()
                else -> AboutScreen()
            }
        }
    }
}

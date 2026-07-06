package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.hanyupinyin.data.PracticeCatalog
import com.tertiaryinfotech.hanyupinyin.data.PracticeSession

/** 练习 (Practice) home — the list of all drill sessions. */
@Composable
fun PracticeHomeScreen(onOpenSession: (PracticeSession) -> Unit) {
    Scaffold(topBar = { LearnTopBar("练习") }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Column {
                Text("练习输入", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Brand.ink)
                Text("选择一个练习，输入拼音，越快越好！", fontSize = 14.sp, color = Brand.mutedInk)
            }
            PracticeCatalog.sessions.forEach { session ->
                NavCard(session.icon, session.color, session.title, session.subtitle) {
                    onOpenSession(session)
                }
            }
        }
    }
}

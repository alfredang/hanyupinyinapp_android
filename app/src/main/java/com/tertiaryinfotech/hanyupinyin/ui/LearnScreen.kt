package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.hanyupinyin.data.PinyinData
import com.tertiaryinfotech.hanyupinyin.data.PinyinUnit
import com.tertiaryinfotech.hanyupinyin.data.PracticeCatalog
import com.tertiaryinfotech.hanyupinyin.data.PracticeSession
import com.tertiaryinfotech.hanyupinyin.speech.Speaker

/** In-tab destinations of the 学习 (Learn) tab. */
sealed interface LearnDestination {
    data object Home : LearnDestination
    data object Initials : LearnDestination
    data object Finals : LearnDestination
    data object Tones : LearnDestination
    data object InputMethod : LearnDestination
    data object FastTyping : LearnDestination
    data class Session(val session: PracticeSession) : LearnDestination
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnTopBar(title: String, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Brand.background),
    )
}

// MARK: - Learn home

@Composable
fun LearnHomeScreen(onOpen: (LearnDestination) -> Unit) {
    Scaffold(topBar = { LearnTopBar("学习拼音") }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Column {
                Text("汉语拼音", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Brand.ink)
                Text("从声母、韵母到声调，掌握拼音输入。", fontSize = 14.sp, color = Brand.mutedInk)
            }
            NavCard(Icons.Filled.Abc, Brand.accent, "声母", "23 个声母 · Initials") {
                onOpen(LearnDestination.Initials)
            }
            NavCard(Icons.Filled.Abc, Brand.secondary, "韵母", "常用韵母 · Finals") {
                onOpen(LearnDestination.Finals)
            }
            NavCard(Icons.Filled.GraphicEq, Brand.highlight, "声调", "四个声调 · Four tones") {
                onOpen(LearnDestination.Tones)
            }
            NavCard(Icons.Filled.Keyboard, Brand.success, "拼音输入法", "如何用拼音打字 · Typing") {
                onOpen(LearnDestination.InputMethod)
            }
            NavCard(Icons.Filled.Bolt, Brand.orange, "快速输入技巧", "简拼与快捷打字 · Fast typing") {
                onOpen(LearnDestination.FastTyping)
            }
        }
    }
}

// MARK: - Initials / Finals grid

@Composable
fun UnitGridScreen(
    title: String,
    subtitle: String,
    units: List<PinyinUnit>,
    speaker: Speaker,
    onBack: () -> Unit,
) {
    Scaffold(topBar = { LearnTopBar(title, onBack) }, containerColor = Brand.background) { p ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier.fillMaxSize().padding(p),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                Column {
                    Text(subtitle, fontSize = 14.sp, color = Brand.mutedInk)
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null,
                            tint = Brand.secondary, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("点一下卡片即可朗读发音", fontSize = 12.sp, color = Brand.secondary)
                    }
                }
            }
            items(units) { unit ->
                AppCard(onClick = { speaker.speak(unit.exampleHanzi) }) {
                    Column(
                        Modifier.fillMaxWidth().height(110.dp).padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(unit.symbol, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Brand.accent)
                        Text(unit.example, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Brand.ink)
                        Text(unit.exampleMeaning, fontSize = 11.sp, color = Brand.mutedInk,
                            textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// MARK: - Tones

@Composable
fun TonesScreen(speaker: Speaker, onBack: () -> Unit) {
    Scaffold(topBar = { LearnTopBar("声调", onBack) }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("普通话有四个声调，声调不同，意思不同。", fontSize = 14.sp, color = Brand.mutedInk)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null,
                    tint = Brand.secondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("点一下卡片即可朗读发音", fontSize = 12.sp, color = Brand.secondary)
            }
            PinyinData.tones.forEachIndexed { idx, tone ->
                AppCard(onClick = { speaker.speak(tone.mark.split(" ").last()) }) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier.size(48.dp).clip(CircleShape).background(Brand.highlight),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("${idx + 1}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(tone.mark, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Brand.ink)
                            Text(tone.name, fontSize = 14.sp, color = Brand.ink)
                            Text(tone.desc, fontSize = 12.sp, color = Brand.mutedInk)
                        }
                    }
                }
            }
            Text(
                "提示：用拼音输入法打字时，一般不用输入声调，直接打字母即可。",
                fontSize = 13.sp, color = Brand.mutedInk,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

// MARK: - Input method how-to

@Composable
fun InputMethodScreen(onOpenSession: (PracticeSession) -> Unit, onBack: () -> Unit) {
    val steps = listOf(
        "输入字母" to "打出拼音字母，例如 “nihao” 表示「你好」。",
        "无需声调" to "拼音输入法不需要输入声调符号，直接打字母。",
        "整词输入" to "可以一次输入整个词语，例如 “zhongguo” → 中国。",
        "选字" to "用数字键或空格选择候选字，常用字通常排在最前。",
        "动手练习" to "光看不够，马上动手输入拼音，越练越快！",
    )
    Scaffold(topBar = { LearnTopBar("拼音输入法", onBack) }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("拼音输入法把拼音字母转换成汉字，是最常用的中文输入方式。",
                fontSize = 14.sp, color = Brand.mutedInk)
            steps.forEachIndexed { idx, (title, desc) ->
                AppCard {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            Modifier.size(32.dp).clip(CircleShape).background(Brand.success),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("${idx + 1}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Brand.ink)
                            Text(desc, fontSize = 14.sp, color = Brand.mutedInk)
                        }
                    }
                }
            }
            Text("立即练习", fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Brand.ink,
                modifier = Modifier.padding(top = 6.dp))
            NavCard(Icons.Filled.Timer, Brand.magenta, "限时挑战", "60 秒内尽量多打 · 测速度") {
                onOpenSession(PracticeCatalog.speedChallenge)
            }
            NavCard(Icons.Filled.RocketLaunch, Brand.teal, "简拼速打", "只打首字母 · 练快捷输入") {
                onOpenSession(PracticeCatalog.shortcutDrill)
            }
        }
    }
}

// MARK: - Fast typing tips

@Composable
fun FastTypingScreen(onBack: () -> Unit) {
    val tips = listOf(
        "整句输入" to "一次连续打出整句拼音，不用空格、不用声调。例如 “woaini” → 我爱你，输入法会自动分词。",
        "简拼（首字母）" to "只打每个字的第一个字母即可。例如 “bjdx” → 北京大学，“wan” → 我爱你。常用词用简拼最快。",
        "空格选词" to "打完拼音按空格键就选中第一个候选词；常用字一般排在最前面。",
        "数字选字" to "候选栏里用数字键 1–9 快速选择对应的字或词。",
        "ü 用 v 代替" to "韵母 ü 在键盘上打字母 v。例如 “lv” → 绿，“nv” → 女。",
        "善用联想" to "选完一个词后，输入法会联想下一个常用词，直接按数字即可，省去重复输入。",
        "多用长句练习" to "到「练习 › 长句练习」里用整句和简拼来训练，速度会越来越快！",
    )
    Scaffold(topBar = { LearnTopBar("快速输入技巧", onBack) }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("掌握这些技巧，拼音输入又快又准。", fontSize = 14.sp, color = Brand.mutedInk)
            tips.forEach { (title, desc) ->
                AppCard {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            Modifier.size(32.dp).clip(CircleShape).background(Brand.orange),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Filled.Bolt, contentDescription = null,
                                tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Brand.ink)
                            Text(desc, fontSize = 14.sp, color = Brand.mutedInk)
                        }
                    }
                }
            }
        }
    }
}

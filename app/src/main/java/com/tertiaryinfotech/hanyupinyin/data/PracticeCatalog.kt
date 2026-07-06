package com.tertiaryinfotech.hanyupinyin.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tertiaryinfotech.hanyupinyin.ui.Brand

/** A practice session definition — mirrors the iOS PracticeSession. */
data class PracticeSession(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val prompts: List<PinyinPrompt>,
    /** If set, this is a timed speed challenge (seconds) instead of a fixed-count round. */
    val timed: Int? = null,
    /** If true, only the 简拼 (first-letter shortcut) is accepted — trains fast input. */
    val shortcutOnly: Boolean = false,
) {
    /** Build a list of [count] randomly-ordered prompts, repeating the pool if needed. */
    fun round(count: Int = 10): List<PinyinPrompt> {
        if (prompts.isEmpty()) return emptyList()
        val out = mutableListOf<PinyinPrompt>()
        while (out.size < count) out += prompts.shuffled()
        return out.take(count)
    }
}

object PracticeCatalog {
    val sessions: List<PracticeSession> = listOf(
        PracticeSession("汉字练习", "看汉字，打拼音 · Characters",
            Icons.AutoMirrored.Filled.MenuBook, Brand.accent, PinyinData.characters),
        PracticeSession("词语练习", "看词语，打拼音 · Words",
            Icons.Filled.FontDownload, Brand.secondary, PinyinData.words),
        PracticeSession("声母练习", "声母拼读 · Initials",
            Icons.Filled.Abc, Brand.highlight, PinyinData.initials.map { it.asPrompt() }),
        PracticeSession("韵母练习", "韵母拼读 · Finals",
            Icons.Filled.Abc, Brand.success, PinyinData.finals.map { it.asPrompt() }),
        PracticeSession("长句练习", "整句快打 · 可用简拼 · Sentences",
            Icons.Filled.Bolt, Brand.orange, PinyinData.sentences),
        PracticeSession("简拼速打", "只打首字母 · Shortcut drill",
            Icons.Filled.RocketLaunch, Brand.teal, PinyinData.sentences, shortcutOnly = true),
        PracticeSession("长信息练习", "整句快打 · 可用简拼 · Long messages",
            Icons.AutoMirrored.Filled.Notes, Brand.blue, PinyinData.paragraphs),
        PracticeSession("ü → v 练习", "韵母 ü 打字母 v · Type ü as v",
            Icons.Filled.SwapHoriz, Brand.purple, PinyinData.vWords),
        PracticeSession("限时挑战", "60 秒速度挑战 · Speed challenge",
            Icons.Filled.Timer, Brand.magenta,
            PinyinData.characters + PinyinData.words + PinyinData.sentences, timed = 60),
        PracticeSession("综合练习", "汉字 + 词语混合 · Mixed",
            Icons.Filled.Shuffle, Brand.violet, PinyinData.characters + PinyinData.words),
    )

    val speedChallenge: PracticeSession get() = sessions.first { it.title == "限时挑战" }
    val shortcutDrill: PracticeSession get() = sessions.first { it.title == "简拼速打" }
}

package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.hanyupinyin.data.PinyinPrompt
import com.tertiaryinfotech.hanyupinyin.data.PracticeSession
import com.tertiaryinfotech.hanyupinyin.data.pinyinPlain
import com.tertiaryinfotech.hanyupinyin.speech.Speaker
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private sealed interface Feedback {
    data object Correct : Feedback
    data class Wrong(val answer: String) : Feedback
}

/** One practice round: show prompt, type pinyin, instant feedback — mirrors iOS PracticeSessionView. */
@Composable
fun PracticeSessionScreen(session: PracticeSession, speaker: Speaker, onBack: () -> Unit) {
    val isTimed = session.timed != null
    var prompts by remember { mutableStateOf(session.round(if (isTimed) 100 else 10)) }
    var index by remember { mutableIntStateOf(0) }
    var entry by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf<Feedback?>(null) }
    var correctCount by remember { mutableIntStateOf(0) }
    var attempted by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(session.timed ?: 0) }
    var startMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var restartKey by remember { mutableIntStateOf(0) }
    val focusRequester = remember { FocusRequester() }

    val current: PinyinPrompt? = prompts.getOrNull(index)

    fun next() {
        if (!isTimed && index + 1 >= prompts.size) {
            finished = true
            return
        }
        // Timed: refill the pool so we never run out before the clock does.
        if (isTimed && index + 1 >= prompts.size) prompts = prompts + session.round(100)
        index += 1
        entry = ""
        feedback = null
        prompts.getOrNull(index)?.let { speaker.speak(it.prompt) }
    }

    fun check() {
        val cur = current ?: return
        val typed = entry.pinyinPlain()
        if (typed.isEmpty()) return
        attempted += 1
        val abbr = cur.abbr.lowercase()
        val ok = if (session.shortcutOnly) {
            abbr.isNotEmpty() && typed == abbr
        } else {
            typed == cur.answer || (abbr.isNotEmpty() && typed == abbr)
        }
        feedback = if (ok) {
            correctCount += 1
            Feedback.Correct
        } else {
            Feedback.Wrong(if (session.shortcutOnly) abbr else cur.answer)
        }
        speaker.speak(cur.prompt)
    }

    fun handleSubmit() {
        if (feedback == null) check() else next()
    }

    fun restart() {
        prompts = session.round(if (isTimed) 100 else 10)
        index = 0
        entry = ""
        feedback = null
        correctCount = 0
        attempted = 0
        finished = false
        timeLeft = session.timed ?: 0
        startMillis = System.currentTimeMillis()
        restartKey += 1
    }

    // Countdown for timed challenges.
    LaunchedEffect(restartKey) {
        if (!isTimed) return@LaunchedEffect
        while (timeLeft > 0 && !finished) {
            delay(1000)
            timeLeft -= 1
        }
        finished = true
    }

    // In a speed challenge, keep momentum: auto-advance on a correct answer.
    LaunchedEffect(feedback) {
        if (isTimed && feedback == Feedback.Correct) {
            delay(350)
            if (!finished) next()
        }
    }

    // Focus the field and read the first prompt on entry / restart.
    LaunchedEffect(restartKey) {
        delay(400)
        runCatching { focusRequester.requestFocus() }
        current?.let { speaker.speak(it.prompt) }
    }

    // Re-focus the input when advancing to the next prompt (the field re-enables).
    LaunchedEffect(index) {
        if (index > 0 && !finished) {
            delay(150)
            runCatching { focusRequester.requestFocus() }
        }
    }

    Scaffold(topBar = { LearnTopBar(session.title, onBack) }, containerColor = Brand.background) { p ->
        if (finished) {
            ResultView(
                total = if (isTimed) attempted else prompts.size,
                correct = correctCount,
                seconds = if (isTimed) (session.timed ?: 1).toDouble()
                else (System.currentTimeMillis() - startMillis) / 1000.0,
                onRestart = ::restart,
                modifier = Modifier.padding(p),
            )
            return@Scaffold
        }
        val cur = current ?: return@Scaffold
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState())
                .imePadding().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Progress / timer
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isTimed) {
                    val total = (session.timed ?: 1).toFloat()
                    LinearProgressIndicator(
                        progress = { (total - timeLeft) / total },
                        modifier = Modifier.fillMaxWidth(),
                        color = Brand.accent,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Timer, contentDescription = null, modifier = Modifier.size(18.dp),
                            tint = if (timeLeft <= 10) Brand.error else Brand.mutedInk)
                        Spacer(Modifier.width(4.dp))
                        Text("${timeLeft}s", fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                            color = if (timeLeft <= 10) Brand.error else Brand.mutedInk)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            modifier = Modifier.size(18.dp), tint = Brand.success)
                        Spacer(Modifier.width(4.dp))
                        Text("$correctCount", fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                            color = Brand.success)
                    }
                } else {
                    LinearProgressIndicator(
                        progress = { index.toFloat() / prompts.size.coerceAtLeast(1) },
                        modifier = Modifier.fillMaxWidth(),
                        color = Brand.accent,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("第 ${index + 1} / ${prompts.size} 题", fontSize = 14.sp, color = Brand.mutedInk)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.CheckCircle, contentDescription = null,
                            modifier = Modifier.size(18.dp), tint = Brand.success)
                        Spacer(Modifier.width(4.dp))
                        Text("$correctCount", fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                            color = Brand.success)
                    }
                }
            }

            // Prompt card — tap to hear it again.
            AppCard(onClick = { speaker.speak(cur.prompt) }) {
                Column(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(cur.prompt, fontSize = if (cur.prompt.length > 6) 34.sp else 52.sp,
                        fontWeight = FontWeight.Bold, color = Brand.ink, textAlign = TextAlign.Center)
                    Text(cur.meaning, fontSize = 14.sp, color = Brand.mutedInk, textAlign = TextAlign.Center)
                    if (session.shortcutOnly) {
                        Text("只打首字母简拼：${cur.abbr}", fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold, color = Brand.highlight)
                    } else if (cur.abbr.isNotEmpty()) {
                        Text("简拼快捷键：${cur.abbr}（输入首字母也算对）", fontSize = 12.sp, color = Brand.highlight)
                    }
                    if (feedback == Feedback.Correct) {
                        Text(cur.toned, fontSize = 19.sp, fontWeight = FontWeight.SemiBold, color = Brand.highlight)
                    }
                    Text("点一下朗读", fontSize = 11.sp, color = Brand.secondary)
                }
            }

            // Input
            val borderColor = when (feedback) {
                Feedback.Correct -> Brand.success
                is Feedback.Wrong -> Brand.error
                null -> Color.Transparent
            }
            OutlinedTextField(
                value = entry,
                onValueChange = { entry = it },
                placeholder = {
                    Text(if (session.shortcutOnly) "输入首字母…" else "输入拼音…",
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                },
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                    .border(2.dp, borderColor, RoundedCornerShape(14.dp)),
                enabled = feedback == null,
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 22.sp, textAlign = TextAlign.Center, color = Brand.ink),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Brand.surface,
                    unfocusedContainerColor = Brand.surface,
                    disabledContainerColor = Brand.surface,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Go,
                ),
                keyboardActions = KeyboardActions(onGo = { handleSubmit() }),
            )

            when (val f = feedback) {
                Feedback.Correct -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Brand.success)
                    Spacer(Modifier.width(8.dp))
                    Text("正确！", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Brand.success)
                }
                is Feedback.Wrong -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Cancel, contentDescription = null, tint = Brand.error)
                    Spacer(Modifier.width(8.dp))
                    Text("正确答案：", fontSize = 14.sp, color = Brand.ink)
                    Text(f.answer, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Brand.error)
                }
                null -> {}
            }

            Button(
                onClick = { handleSubmit() },
                enabled = feedback != null || entry.trim().isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand.accent),
            ) {
                Text(if (feedback == null) "确认" else "下一题",
                    fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// MARK: - Result

@Composable
private fun ResultView(
    total: Int,
    correct: Int,
    seconds: Double,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accuracy = if (total == 0) 0 else (correct.toDouble() / total * 100).roundToInt()
    val perMinute = if (seconds <= 0) 0 else (correct / seconds * 60).roundToInt()

    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Icon(
            if (accuracy >= 80) Icons.Filled.Stars else Icons.Filled.Verified,
            contentDescription = null,
            tint = if (accuracy >= 80) Brand.highlight else Brand.success,
            modifier = Modifier.size(80.dp).padding(top = 8.dp),
        )
        Text(if (accuracy >= 80) "太棒了！" else "完成练习",
            fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Brand.ink)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("$correct/$total", "正确", Modifier.weight(1f))
            StatCard("$accuracy%", "准确率", Modifier.weight(1f))
            StatCard("$perMinute", "字/分钟", Modifier.weight(1f))
        }
        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Brand.accent),
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("再来一次", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    AppCard(modifier = modifier) {
        Column(
            Modifier.fillMaxWidth().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Brand.accent)
            Text(label, fontSize = 12.sp, color = Brand.mutedInk)
        }
    }
}

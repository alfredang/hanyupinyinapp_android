package com.tertiaryinfotech.hanyupinyin.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

enum class VoiceGender(val label: String, val englishLabel: String) {
    FEMALE("女声", "Female"),
    MALE("男声", "Male");

    companion object {
        fun from(raw: String?): VoiceGender = if (raw == MALE.name) MALE else FEMALE
    }
}

/**
 * Speaks Chinese text aloud with Android's on-device speech synthesizer (Mandarin, zh-CN).
 * The user can pick a female or male voice in Settings; when the engine exposes two distinct
 * Chinese voices they are used directly, otherwise the pitch is shifted as a fallback —
 * mirrors the iOS Speaker.
 */
class Speaker(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("settings", Context.MODE_PRIVATE)

    var gender: VoiceGender by mutableStateOf(VoiceGender.from(prefs.getString("voiceGender", null)))
        private set

    private var ready by mutableStateOf(false)
    private var femaleVoice: Voice? = null
    private var maleVoice: Voice? = null

    private val tts: TextToSpeech = TextToSpeech(context.applicationContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
            ready = true
            pickChineseVoices()
        }
    }

    fun chooseGender(g: VoiceGender) {
        gender = g
        prefs.edit().putString("voiceGender", g.name).apply()
    }

    /** True only if a distinct voice for that gender is actually installed. */
    fun isAvailable(g: VoiceGender): Boolean =
        if (g == VoiceGender.FEMALE) femaleVoice != null else maleVoice != null

    /** Speak a Chinese word/character with the correct pronunciation. */
    fun speak(text: String) {
        if (!ready) return
        val clean = text.trim()
        if (clean.isEmpty()) return
        tts.stop()
        tts.language = Locale.SIMPLIFIED_CHINESE
        val voice = if (gender == VoiceGender.MALE) maleVoice ?: femaleVoice
        else femaleVoice ?: maleVoice
        if (voice != null) tts.voice = voice
        // If only one physical voice exists, shift pitch so 女声/男声 still differ audibly.
        val distinct = femaleVoice != null && maleVoice != null && femaleVoice != maleVoice
        tts.setPitch(
            when {
                distinct -> 1.0f
                gender == VoiceGender.MALE -> 0.75f
                else -> 1.05f
            }
        )
        tts.setSpeechRate(0.85f)
        tts.speak(clean, TextToSpeech.QUEUE_FLUSH, null, "pinyin")
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }

    /** Pick the best installed Chinese voices, splitting them into a female/male pair. */
    private fun pickChineseVoices() {
        val zh = runCatching {
            tts.voices?.filter { v ->
                v.locale.language == "zh" && !v.isNetworkConnectionRequired
            }?.sortedBy { it.latency }
        }.getOrNull().orEmpty()
        if (zh.isEmpty()) return

        val femaleNames = listOf("female", "#female", "xiaoyan", "ting")
        val maleNames = listOf("male", "#male", "xiaofeng", "kang")
        femaleVoice = zh.firstOrNull { v -> femaleNames.any { v.name.lowercase().contains(it) } }
        maleVoice = zh.firstOrNull { v -> maleNames.any { v.name.lowercase().contains(it) } }

        // Fall back: use the two lowest-latency distinct voices as the pair.
        if (femaleVoice == null) femaleVoice = zh.firstOrNull()
        if (maleVoice == null) maleVoice = zh.firstOrNull { it != femaleVoice } ?: femaleVoice
    }
}

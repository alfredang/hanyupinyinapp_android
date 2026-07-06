package com.tertiaryinfotech.hanyupinyin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tertiaryinfotech.hanyupinyin.speech.Speaker
import com.tertiaryinfotech.hanyupinyin.ui.HanyuPinyinTheme
import com.tertiaryinfotech.hanyupinyin.ui.RootScreen

class MainActivity : ComponentActivity() {

    private lateinit var speaker: Speaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        speaker = Speaker(this)
        setContent {
            HanyuPinyinTheme {
                RootScreen(speaker = speaker)
            }
        }
    }

    override fun onDestroy() {
        speaker.shutdown()
        super.onDestroy()
    }
}

package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLEncoder

private const val FEEDBACK_WHATSAPP = "6588666375"   // +65 8866 6375, no "+"/spaces

/** 反馈 (Feedback) — Title + Message, sent via WhatsApp. House style per android-feedback-about. */
@Composable
fun FeedbackScreen() {
    val uri = LocalUriHandler.current
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val canSend = title.isNotBlank() || message.isNotBlank()

    Scaffold(topBar = { LearnTopBar("反馈") }, containerColor = Brand.background) { p ->
        Column(
            Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState())
                .imePadding().padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text("欢迎反馈意见或建议，我们会尽快回复。", fontSize = 14.sp, color = Brand.mutedInk)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("标题", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Brand.mutedInk)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("标题") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("内容", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Brand.mutedInk)
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("你的留言…") },
                    minLines = 6,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
            }

            Button(
                onClick = {
                    val body = buildString {
                        if (title.isNotBlank()) append("*").append(title.trim()).append("*\n")
                        append(message.trim())
                    }
                    val text = URLEncoder.encode(body, "UTF-8")
                    uri.openUri("https://wa.me/$FEEDBACK_WHATSAPP?text=$text")
                },
                enabled = canSend,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Brand.accent),
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("通过 WhatsApp 发送", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

package com.example.chattutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import io.getstream.chat.android.compose.ui.messages.ChannelScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamDesign
import io.getstream.chat.android.compose.viewmodel.messages.ChannelViewModelFactory
import io.getstream.chat.android.compose.viewmodel.messages.MessageListOptions

/**
 * Demonstrates customizing the [ChatTheme] typography. The same pattern works for any
 * [StreamDesign.Typography] field; here we swap the font family and weights on a few text styles
 * via [copy].
 */
class ChannelActivity2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)
        if (channelId == null) {
            finish()
            return
        }

        setContent {
            val baseTypography = StreamDesign.Typography.default(fontFamily = FontFamily.Serif)
            ChatTheme(
                typography = baseTypography.copy(
                    bodyEmphasis = baseTypography.bodyEmphasis.copy(fontWeight = FontWeight.Bold),
                    headingMedium = baseTypography.headingMedium.copy(fontWeight = FontWeight.Black),
                )
            ) {
                ChannelScreen(
                    viewModelFactory = ChannelViewModelFactory(
                        context = this,
                        channelId = channelId,
                        messageListOptions = MessageListOptions(messageLimit = 30),
                    ),
                    onBackPressed = { finish() }
                )
            }
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, ChannelActivity2::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}

package com.example.chattutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import io.getstream.chat.android.compose.ui.messages.ChannelScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamDesign
import io.getstream.chat.android.compose.viewmodel.messages.ChannelViewModelFactory
import io.getstream.chat.android.compose.viewmodel.messages.MessageListOptions

class ChannelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the ID of the selected channel from Intent Extras.
        // If there is no channelId, then we finish the Activity and return.
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)
        if (channelId == null) {
            finish()
            return
        }

        // Set the UI
        setContent {
            val baseColors = if (isSystemInDarkTheme()) {
                StreamDesign.Colors.defaultDark()
            } else {
                StreamDesign.Colors.default()
            }
            ChatTheme(
                colors = baseColors.copy(
                    accentPrimary = Color(0xFF005FFF),
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

    // Define a helper function to build an Intent for this Activity.
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, ChannelActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}

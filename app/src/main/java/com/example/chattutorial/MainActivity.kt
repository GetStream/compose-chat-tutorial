package com.example.chattutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.InitializationState

class MainActivity : ComponentActivity() {

    private val chatClient = ChatClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Observe the client connection state
            val clientInitialisationState by chatClient.clientState.initializationState.collectAsState()

            ChatTheme {
                when (clientInitialisationState) {
                    InitializationState.COMPLETE -> {
                        ChannelsScreen(
                            title = stringResource(id = R.string.app_name),
                            isShowingSearch = true,
                            onItemClick = { channel ->
                                startActivity(MessagesActivity4.getIntent(this@MainActivity, channel.cid))
                            },
                            onBackPressed = { finish() }
                        )
                    }
                    InitializationState.INITIALIZING -> {
                        Text(text = "Initialising...")
                    }
                    InitializationState.NOT_INITIALIZED -> {
                        Text(text = "Not initialized...")
                    }
                }
            }
        }
    }
}

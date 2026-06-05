package com.example.chattutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.SearchMode
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.InitializationState
import io.getstream.chat.android.models.User

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the client for API calls with offline storage and state management
        val client = ChatClient.Builder("uun7ywwamhs9", applicationContext)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()

        // Authenticate and connect the user
        val user = User(
            id = "tutorial-droid",
            name = "Tutorial Droid",
            image = "https://bit.ly/2TIt8NR"
        )

        client.connectUser(
            user = user,
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.WwfBzU1GZr0brt_fXnqKdKhz3oj0rbDUm2DqJO_SS5U"
        ).enqueue()

        setContent {
            // Observe the client connection state
            val clientInitialisationState by client.clientState.initializationState.collectAsState()

            ChatTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                ) {
                    when (clientInitialisationState) {
                        InitializationState.COMPLETE -> {
                            ChannelsScreen(
                                title = stringResource(id = R.string.app_name),
                                isShowingHeader = true,
                                searchMode = SearchMode.Messages,
                                onChannelClick = { channel ->
                                    startActivity(ChannelActivity.getIntent(this@MainActivity, channel.cid))
                                },
                                onBackPressed = { finish() }
                            )
                        }
                        InitializationState.INITIALIZING -> {
                            Text(text = "Initializing...")
                        }
                        InitializationState.NOT_INITIALIZED -> {
                            Text(text = "Not initialized...")
                        }
                    }
                }
            }
        }
    }
}

package com.example.chattutorial

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1 - Set up the OfflinePlugin for offline storage
        val offlinePluginFactory = StreamOfflinePluginFactory(
            appContext = applicationContext,
        )
        val statePluginFactory = StreamStatePluginFactory(
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true,
            ),
            appContext = this,
        )

        // 2 - Set up the client for API calls and with the plugin for offline storage
        val client = ChatClient.Builder("uun7ywwamhs9", applicationContext)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()

        // 3 - Authenticate and connect the user
        val user = User(
            id = "tutorial-droid",
            name = "Tutorial Droid",
            image = "https://bit.ly/2TIt8NR"
        )

        client.connectUser(
            user = user,
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.WwfBzU1GZr0brt_fXnqKdKhz3oj0rbDUm2DqJO_SS5U"
        ).enqueue {
            if (it.isSuccess) {
                // 4 - Set up the Channels Screen UI
                setContent {
                    ChatTheme {
                        ChannelsScreen(
                            title = stringResource(id = R.string.app_name),
                            isShowingSearch = true,
                            onItemClick = { channel ->
                                startActivity(MessagesActivity4.getIntent(this@MainActivity, channel.cid))
                            },
                            onBackPressed = { finish() }
                        )
                    }
                }
            } else {
                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

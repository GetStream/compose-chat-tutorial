package com.example.chattutorial

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channel.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.offline.ChatDomain

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1 - Set up the client for API calls and the domain for offline storage
        val client = ChatClient.Builder("b67pax5b2wdq", applicationContext)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()
        ChatDomain.Builder(client, applicationContext).build()

        // 2 - Authenticate and connect the user
        val user = User(id = "tutorial-droid").apply {
            name = "Tutorial Droid"
            image = "https://bit.ly/2TIt8NR"
        }
        client.connectUser(
            user = user,
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"
        ).enqueue()

        // 3 - Set up the Channels Screen UI
        setContent {
            ChatTheme {
                ChannelsScreen(
                    title = stringResource(id = R.string.app_name),
                    onItemClick = { channel ->
                        startActivity(MessagesActivity4.getIntent(this, channel.cid))
                    },
                    onBackPressed = { finish() }
                )
            }
        }
    }
}

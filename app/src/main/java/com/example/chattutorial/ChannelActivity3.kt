package com.example.chattutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.getstream.chat.android.compose.ui.components.messageactions.MessageActions
import io.getstream.chat.android.compose.ui.components.messageactions.ReactionsMenu
import io.getstream.chat.android.compose.ui.components.messageoptions.defaultMessageOptionsState
import io.getstream.chat.android.compose.ui.messages.attachments.AttachmentPickerMenu
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.ChannelViewModelFactory
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListOptions
import io.getstream.chat.android.ui.common.state.messages.MessageMode
import io.getstream.chat.android.ui.common.state.messages.list.SelectedMessageOptionsState
import io.getstream.chat.android.ui.common.state.messages.list.SelectedMessageReactionsState

/**
 * Demonstrates building the chat screen from low-level, bound + stateless components instead of
 * using [io.getstream.chat.android.compose.ui.messages.ChannelScreen]. The selected-message
 * overlay is rendered with [MessageActions] / [ReactionsMenu].
 */
class ChannelActivity3 : ComponentActivity() {

    private val factory by lazy {
        ChannelViewModelFactory(
            context = this,
            channelId = intent.getStringExtra(KEY_CHANNEL_ID) ?: "",
            messageListOptions = MessageListOptions(messageLimit = 30),
        )
    }

    private val listViewModel: MessageListViewModel by viewModels { factory }
    private val attachmentsPickerViewModel: AttachmentsPickerViewModel by viewModels { factory }
    private val composerViewModel: MessageComposerViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)
        if (channelId == null) {
            finish()
            return
        }

        setContent {
            ChatTheme {
                MyCustomUi()
            }
        }
    }

    @Composable
    private fun MyCustomUi() {
        val messagesState by listViewModel.currentMessagesState
        val selectedMessageState = messagesState.selectedMessageState
        val user by listViewModel.user.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .consumeWindowInsets(WindowInsets.ime),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = ChatTheme.colors.backgroundCoreApp,
                bottomBar = {
                    Column {
                        MessageComposer(
                            viewModel = composerViewModel,
                            onAttachmentsClick = {
                                attachmentsPickerViewModel.setPickerVisible(true)
                            }
                        )
                        AttachmentPickerMenu(
                            attachmentsPickerViewModel = attachmentsPickerViewModel,
                            composerViewModel = composerViewModel,
                        )
                    }
                }
            ) { contentPadding ->
                MessageList(
                    modifier = Modifier
                        .background(ChatTheme.colors.backgroundCoreApp)
                        .padding(contentPadding)
                        .fillMaxSize(),
                    viewModel = listViewModel,
                    onThreadClick = { message ->
                        composerViewModel.setMessageMode(MessageMode.MessageThread(message))
                        listViewModel.openMessageThread(message)
                    }
                )
            }

            if (selectedMessageState is SelectedMessageOptionsState) {
                val selectedMessage = selectedMessageState.message
                MessageActions(
                    message = selectedMessage,
                    messageOptions = defaultMessageOptionsState(
                        selectedMessage = selectedMessage,
                        currentUser = user,
                        isInThread = listViewModel.isInThread,
                        channel = selectedMessageState.channel,
                    ),
                    ownCapabilities = selectedMessageState.ownCapabilities,
                    onMessageAction = { action ->
                        composerViewModel.performMessageAction(action)
                        listViewModel.performMessageAction(action)
                    },
                    onShowMoreReactionsSelected = {
                        listViewModel.selectExtendedReactions(selectedMessage)
                    },
                    onDismiss = { listViewModel.removeOverlay() },
                    currentUser = user,
                )
            } else if (selectedMessageState is SelectedMessageReactionsState) {
                val selectedMessage = selectedMessageState.message
                ReactionsMenu(
                    message = selectedMessage,
                    currentUser = user,
                    ownCapabilities = selectedMessageState.ownCapabilities,
                    onMessageAction = { action ->
                        composerViewModel.performMessageAction(action)
                        listViewModel.performMessageAction(action)
                    },
                    onShowMoreReactionsSelected = {
                        listViewModel.selectExtendedReactions(selectedMessage)
                    },
                    onDismiss = { listViewModel.removeOverlay() },
                )
            }
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, ChannelActivity3::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}
